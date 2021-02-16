/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.floref.core.flow.build;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.config.consumer.FlowMetrics;
import org.floref.core.config.consumer.MetricsHelper;
import org.floref.core.dsl.flow.data.FlowDefinition;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.reference.LambdaMeta.MethodParameter;
import org.floref.core.flow.run.CommandRunner;
import org.floref.core.flow.run.FlowSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * For each interface for which we define flows there will be a single proxy(start instance) doing the magic.
 * <p>
 * JDK dynamic proxying infrastructure (from the java.lang.reflect package) that is only capable of creating proxies for interfaces.
 * https://spring.io/blog/2007/07/19/debunking-myths-proxies-impact-performance/
 * <p>
 * Repackage cglib inside so that no conflicts will arise. See: https://stackoverflow.com/questions/41478307/whats-the-difference-between-spring-cglib-and-cglib
 * https://docs.spring.io/spring/docs/2.5.x/reference/aop.html#aop-proxying
 * http://stackoverflow.com/questions/10664182/what-is-the-difference-between-jdk-dynamic-proxy-and-cglib
 * https://gist.github.com/premraj10/3a3eac42a72c32de3a41ec13ef3d56ad
 * https://github.com/edc4it/jpa-case/blob/master/src/main/java/util/JPAUtil.java
 * If you will want more flexible behavior like having the flows inside a non interface then you will need to use cglib or bytebuddy
 *
 * @author Cristian Donoiu
 */
public class FlowInvocationHandler implements InvocationHandler {
  private static final Log LOG = LogFactory.getLog(FlowInvocationHandler.class);

  private FlowInstanceData flowInstanceData;

  public void setFlowInstanceData(FlowInstanceData flowInstanceData) {
    this.flowInstanceData = flowInstanceData;
  }

  public FlowInstanceData getFlowInstanceData() {
    return flowInstanceData;
  }

  private Object runFlow(FlowDefinition flowDefinition, Object[] args, LambdaMeta lambdaMeta) throws Exception {
    String flowDefinitionId = flowDefinition.getId();
    // If this is a top level flow execution (that might aggregate other flows) only the top one should init session.
    final boolean isGate = FlowSession.isGate();

    try {
      if (isGate) {
        // 1. Init session map since this is top level.
        FlowSession.initIfNeeded();
      }

      // Validate lazily it on first run, since not all dependencies(beans) might be available when the flow is defined.
      // Can also be validated when the application is started (e.g. Spring Context loaded) by calling
      // Flows.validateAll() if configuration is set on true.
      flowDefinition.validate();

      // MetricsHelper.
      MetricsHelper.beforeFlow(new FlowMetrics.Builder(flowDefinitionId).params(args).originator(isGate).build());

      if (isGate) {
        // 2. Save some args in session only if this is top level.
        List<MethodParameter> parameters = lambdaMeta.getParameters();
        for (int i = 0; i < parameters.size(); i++) {
          MethodParameter methodParameter = parameters.get(i);
          if (methodParameter.isFlowVar()) {
            String sessionProperty = methodParameter.getFlowVar().value();
            FlowSession.set(sessionProperty, args[i]);
          }
        }
      }

      Object result = CommandRunner.run(flowDefinition, args);

      MetricsHelper.afterFlow(new FlowMetrics.Builder(flowDefinitionId).params(args).originator(isGate).result(result).build());
      return result;

    } catch (Exception exception) {
      MetricsHelper
          .afterFlow(new FlowMetrics.Builder(flowDefinitionId).params(args).exception(exception).originator(isGate).build());
      throw exception; // does not alter stacktrace
    } finally {
      LOG.debug("Clear session = " + isGate);
      // Cleaning session important to not leak data into subsequent users of this thread.
      if (isGate) {
        FlowSession.clearSession(); // Async actions will save their own copy.
      }
    }
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String flowDefinitionId = FlowDefinition.getIdFromFlowRef(method);

    // Each time get the flow definition since it might have been updated in the mean time.
    FlowDefinition flowDefinition = flowInstanceData.getFlowDefinition(flowDefinitionId);
    if (flowDefinition == null) {
      // If an Object method is called.
      if (method.getDeclaringClass() == Object.class) {
        if (method.getName().equals(FlowObjectMethods.EQUALS)) {
          return FlowObjectMethods.equals(proxy, args[0]);
        } else if (method.getName().equals(FlowObjectMethods.HASHCODE)) {
          return FlowObjectMethods.hashCode(proxy);
        } else if (method.getName().equals(FlowObjectMethods.TOSTRING)) {
          return flowInstanceData.getStringValue();
        } else {
          throw new FlowDefinitionException("Flow not yet defined for " + flowDefinitionId);
        }
      }
      throw new FlowDefinitionException("Flow not yet defined for " + flowDefinitionId);
    }

    LambdaMeta lambdaMeta = flowDefinition.getFlowReference();


    if (Future.class == lambdaMeta.getReturnType()) {
      Map session = FlowSession.get(); // get session map from parent thread.
      Future future = CommandRunner.submit(() -> {
        FlowSession.set(session);  // Copy session from parent since this may be launched from within a flow step.
        return runFlow(flowDefinition, args, lambdaMeta);
      });
      return new FlowFuture(future, session);

    } else if (CompletableFuture.class == lambdaMeta.getReturnType()) {
      Map session = FlowSession.get(); // get session map from parent thread.
      FlowCompletableFuture completableFuture = new FlowCompletableFuture(session);
      CommandRunner.submit(() -> {
        FlowSession.set(session);  // Copy session from parent since this may be launched from within a flow step.
        try {
          Object result = runFlow(flowDefinition, args, lambdaMeta);
          completableFuture.complete(result);
        } catch (ExecutionException e) {
          completableFuture.completeExceptionally(e.getCause());
        } catch (Exception e) {
          completableFuture.completeExceptionally(e);
        }
      });
      return completableFuture;
    } else {
      return runFlow(flowDefinition, args, lambdaMeta);
    }
  }

}
