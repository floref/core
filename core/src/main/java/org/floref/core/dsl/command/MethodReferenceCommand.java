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

package org.floref.core.dsl.command;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.config.FlowConfiguration;
import org.floref.core.config.consumer.MethodRefMetrics;
import org.floref.core.config.consumer.MetricsHelper;
import org.floref.core.config.injector.BeanInjector;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.dsl.flow.impex.Aliases;
import org.floref.core.dsl.flow.impex.FlowStep;
import org.floref.core.exception.FlowCancelledException;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.exception.MissingBeanFlowException;
import org.floref.core.flow.annotation.FlowVar;
import org.floref.core.flow.reference.*;
import org.floref.core.flow.reference.LambdaMeta.MethodParameter;
import org.floref.core.flow.run.CommandContext;
import org.floref.core.flow.run.FlowSession;
import org.floref.core.flow.run.FlowUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Base flow command class that takes a method reference as a parameter. E.g. .from .to .when .async etc
 *
 * @author Cristian Donoiu
 */
public class MethodReferenceCommand extends ChildCommand {

  private static final Log LOG = LogFactory.getLog(MethodReferenceCommand.class);

  protected LambdaMeta lambdaMeta;
  protected MethodReferenceCommand revertBy;
  protected String keyword;

  public MethodReferenceCommand() {

  }
  public MethodReferenceCommand(MethodReference methodReference) {
    lambdaMeta = LambdaMetaBuilder.getLambdaMeta(methodReference);
  }

  public MethodReferenceCommand(LambdaMeta lambdaMeta) {
    this.lambdaMeta = lambdaMeta;
  }

  public MethodReferenceCommand(String keyword, MethodReference methodReference) {
    this(methodReference);
    this.keyword = keyword;
  }

  public MethodReferenceCommand(String keyword, LambdaMeta lambdaMeta) {
    this(lambdaMeta);
    this.keyword = keyword;
  }

  public MethodReferenceCommand(ParamVoidConsumerVoidSupplier consumer) {
    this((MethodReference)consumer);
  }
  public <T> MethodReferenceCommand(ParamSupplier<T> consumer) {
    this((MethodReference)consumer);
  }
  public <T> MethodReferenceCommand(ParamConsumer<T> consumer) {
    this((MethodReference)consumer);
  }
  public <T, U> MethodReferenceCommand(ParamBiConsumer<T, U> consumer) {
    this((MethodReference)consumer);
  }
  public <T, U, V> MethodReferenceCommand(ParamTriConsumer<T, U, V> consumer) {
    this((MethodReference)consumer);
  }

  public <T, U, V, X> MethodReferenceCommand(ParamTetraConsumer<T, U, V, X> consumer) {
    this((MethodReference)consumer);
  }

  public LambdaMeta getLambdaMeta() {
    return lambdaMeta;
  }

  public void setRevertBy(MethodReference methodReference) {
    revertBy = new MethodReferenceCommand(methodReference);
  }
  public void setRevertBy(LambdaMeta lambdaMeta) {
    revertBy = new MethodReferenceCommand(lambdaMeta);
  }

  public MethodReferenceCommand getRevertBy() {
    return revertBy;
  }

  @Override
  public String getId() {
    return lambdaMeta.getMethodReferenceAsString();
  }

  /**
   * Used for displaying a flow definition when hovering over a flow.
   */
  public String toString() {
    String string = lambdaMeta.getLambdaClass().getSimpleName() + "::" + lambdaMeta.getLambdaMethod().getName();
    if (lambdaMeta.getTarget() != null) {
      string = string.substring(0,1).toLowerCase() + string.substring(1);
    }
    return getKeyword() + "(" + string + ")";
  }

  @Override
  public String getKeyword() {
    return keyword;
  }

  @Override
  public void definitionExport(FlowStep flowStep) {
    String alias = Aliases.getAlias(getLambdaMeta());
    if (alias == null) {
      alias = getLambdaMeta().getMethodReferenceAsString();
    }
    flowStep.setRef(alias);
  };

  /**
   * Runs a method reference.
   */
  protected void run(CommandContext commandContext, LambdaMeta lambdaMeta) throws Exception {
    String id = lambdaMeta.getActualMethodReferenceAsString();

    if (FlowUtil.isCancelledFlow()) {
      String message =
          "Flow " + commandContext.getFlowDefinition().getId() + " has been cancelled before running " + id;
      LOG.error(message);
      throw new FlowCancelledException(message);
    }
    Object[] args = commandContext.getArguments();

    try {
      Method method = lambdaMeta.getLambdaActualMethod();
      Class lambdaClass = lambdaMeta.getLambdaClass();
      String methodName = method.getName();
      Object target = lambdaMeta.getTarget();  // The target may be a flow or a non flow.

      Object flow = Flows.getFlow(lambdaClass);

      if (target != null) {
        MetricsHelper.beforeMethod(new MethodRefMetrics.Builder(id).params(args).isFlow(flow != null).build());

      } else if (target == null) {
        /**
         * If target is null then this might be:
         * 1. another flow
         * 2. a bean that needs to be autowired from Spring or other dependency injection framework via BeanInjector
         * 3. a method that can be executed on the return of the previous step. E.g.  .to(T::getString).to(String::length)
         * 4.
         * 5. a static method.
         */

        // 1. Check if it is a flow.
        target = flow;

        if (target == null) {

          // 2. If still null then try to invoke any bean injectors that might have been set (e.g. the Spring one).
          BeanInjector beanInjector = FlowConfiguration.getBeanInjector();
          if (beanInjector != null) {
            target = beanInjector.getBean(lambdaClass);
            if (target != null) {
              //method = getMethod(target.getClass(), method); // Get the corresponding method from actual class. Is this needed ?.
              lambdaMeta.refreshActualMethod(method);
              id = lambdaMeta.getActualMethodReferenceAsString();
            }
          }

          // 3. If it can be executed on the return of the previous step
          if (target == null) {
            if (args.length > 0 && args[0] != null && lambdaClass.isAssignableFrom(args[0].getClass())) {
              target = args[0];
              args = Arrays.copyOfRange(args, 1, args.length); // Remove it since it has become the target.
            }
            // 5. Static method.
            if (target == null) {
              if (!Modifier.isStatic(method.getModifiers())) {
                throw new MissingBeanFlowException(lambdaClass);
              }
            }
          }

          MetricsHelper.beforeMethod(new MethodRefMetrics.Builder(id).params(args).isFlow(flow != null).build());
        }
      }

      List<MethodParameter> methodParameters = lambdaMeta.getParameters();
      Object[] params = new Object[methodParameters.size()];  // Init with nulls.

      List argList = new ArrayList(Arrays.asList(args));

      // Map parameters. Consider caching the mapping result so that next time is not attempted again. Flowvars first.
      List<MethodParameter> neededParams = new ArrayList<>();
      for (int i = 0; i < methodParameters.size(); i++) {
        MethodParameter methodParameter = methodParameters.get(i);
        if (methodParameter.isFlowVar()) {
          String sessionProperty = methodParameter.getFlowVar().value();
          // Try first to get it from the session.
          params[i] = FlowSession.get(sessionProperty);
        } else {
          neededParams.add(methodParameter);
        }
      }

      Iterator<Object> argIterator = argList.iterator();
      while (argIterator.hasNext()) {
        Object arg = argIterator.next();
        if (arg == null) {
          continue;
        }
        boolean found = false;
        MethodParameter candidate = null;
        // Find best match.
        for (MethodParameter methodParameter : neededParams) {
          Class parameterClass = methodParameter.getClazz();
          if (parameterClass == arg.getClass()) {
            candidate = methodParameter;
            break;
          } else if (parameterClass.isAssignableFrom(arg.getClass())) {
            if (candidate == null) {
              candidate = methodParameter;
            } else if (candidate.getClazz().isAssignableFrom(parameterClass)) {
              candidate = methodParameter; // Keep the nearest.
            }
          }
        }
        if (candidate != null) {
          params[candidate.getPosition()] = arg;
          argIterator.remove();
          neededParams.remove(candidate);
        }
      }

      // Non null parameters that were not matched.
      for (Object arg : argList) {
        if (arg != null && neededParams.size() > 0) {
          throw new FlowDefinitionException("Method parameter mismatch for '" + id + "' found '"
              + arg.getClass() + "' but a '" + neededParams.get(0).getClazz().getName() + "' is expected");
        }
      }


      // Invoke.
      Object result = lambdaMeta.getLambdaActualMethod().invoke(target, params);

      MetricsHelper
          .afterMethod(new MethodRefMetrics.Builder(id).params(args).isFlow(flow != null).result(result).build());

      FlowVar returnFlowVar = lambdaMeta.getReturnFlowVar();
      if (returnFlowVar != null) {
        FlowSession.set(returnFlowVar.value(), result);
      }

      commandContext.setResult(result);
      commandContext.moveResultToArguments();
      commandContext.setException(null);

    } catch (InvocationTargetException exception) {
      Throwable cause = exception.getCause();
      LOG.error(cause.getMessage(), cause);

      if (cause instanceof Exception) {
        commandContext.setException((Exception)cause);
        MetricsHelper
            .afterMethod(new MethodRefMetrics.Builder(id).params(args).exception((Exception) cause).build());
        throw (Exception)cause;
      } else {
        commandContext.setException(exception);
        throw exception; // Contains Error.
      }
    }
  }

  @Override
  public void run(CommandContext commandContext) throws Exception {
    run(commandContext, lambdaMeta);
  }

  @Override
  public void alias(String alias) {
    Aliases.add(alias, this);
  }
}
