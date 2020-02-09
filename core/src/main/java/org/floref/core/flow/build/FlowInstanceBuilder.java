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

import org.floref.core.dsl.flow.data.FlowDefinition;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.exception.FlowNotAnInterfaceException;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.registry.FlowRegistry;

import java.lang.reflect.Proxy;

/**
 *
 * @author Cristian Donoiu
 */
public class FlowInstanceBuilder {

  public static void build(FlowDefinition flowDefinition, boolean updateDefinition) {
    LambdaMeta lambdaMeta = flowDefinition.getFlowReference();
    Class flowClass = lambdaMeta.getLambdaClass();

    // If this is the first flow defined on the flow class then create the proxy and flow instance data, and return.
    try {
      if (!FlowRegistry.isFlowInstanceRegistered(flowDefinition)) {
        synchronized (flowClass) {
          if (!FlowRegistry.isFlowInstanceRegistered(flowDefinition)) {
            FlowInvocationHandler flowInvocationHandler = new FlowInvocationHandler();
            // https://docs.oracle.com/javase/8/docs/technotes/guides/reflection/proxy.html
            Object flowInstance = Proxy.newProxyInstance(
                flowClass.getClassLoader(),
                new Class[] {flowClass},
                flowInvocationHandler);

            // Register the flow.
            FlowRegistry.registerNewFlow(flowDefinition, flowInstance, flowInvocationHandler);
            return;
          }
        }
      }

      FlowInstanceData flowInstanceData = FlowRegistry.getFlowInstanceData(flowDefinition);
      synchronized (flowInstanceData) {
        if (!updateDefinition) {
          // If flow instance already exists then check that the flow definition is not already defined.
          if (flowInstanceData.contains(flowDefinition.getId())) {
            throw new FlowDefinitionException("Flow already defined for " + flowDefinition.getId());
          }
          flowInstanceData.add(flowDefinition);    // Add first time definition on that method.
        } else {
          flowInstanceData.update(flowDefinition); // Replaces the old definition.
        }
      }

    } catch (IllegalArgumentException e) {
      if (e.getMessage().contains("not an interface")) {
        throw new FlowNotAnInterfaceException(flowClass, e);
      } else {
        throw new FlowDefinitionException(e);
      }
    }
  }

  public static void build(FlowDefinition flowDefinition) {
    build(flowDefinition, false);
  }

}
