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
import org.floref.core.dsl.flow.impex.FlowStep;
import org.floref.core.flow.reference.Methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class FlowInstanceData {
  Object flow;
  Class flowClass;
  LinkedHashMap<String, FlowDefinition> flowDefinitions = new LinkedHashMap<>();  // preserve order of definition for toString().
  boolean isValidated;
  String stringValue;
  FlowInvocationHandler flowInvocationHandler;

  public Object getFlow() {
    return flow;
  }

  public Class getFlowClass() {
    return flowClass;
  }

  public void setFlowClass(Class flowClass) {
    this.flowClass = flowClass;
  }

  public void setFlow(Object flow) {
    this.flow = flow;
  }

  public boolean isValidated() {
    return isValidated;
  }

  public void setValidated(boolean validated) {
    isValidated = validated;
  }

  public void add(FlowDefinition flowDefinition) {
    flowDefinitions.put(flowDefinition.getId(), flowDefinition);
    stringValue = null;
  }

  public FlowDefinition getFlowDefinition(String flowDefinitionId) {
    return flowDefinitions.get(flowDefinitionId);
  }

  public FlowDefinition getFlowDefinitionFromMethod(Method method) {
    return flowDefinitions.get(Methods.getMethodReferenceAsString(flowClass, method));
  }

  /**
   * @return a copy of the flow definitions.
   */
  public List<FlowDefinition> getFlowDefinitions() {
    return new ArrayList<FlowDefinition>(flowDefinitions.values());
  }

  public boolean contains(String flowDefinitionId) {
    return flowDefinitions.containsKey(flowDefinitionId);
  }
  public void update(FlowDefinition flowDefinition) {
    flowDefinitions.put(flowDefinition.getId(), flowDefinition);
    stringValue = null;
  }

  public FlowInvocationHandler getFlowInvocationHandler() {
    return flowInvocationHandler;
  }

  public void setFlowInvocationHandler(FlowInvocationHandler flowInvocationHandler) {
    this.flowInvocationHandler = flowInvocationHandler;
  }

  public String getStringValue() {
//    if (stringValue == null) {
      stringValue = FlowObjectMethods.toString(this); // cache it.
//    }
    return stringValue;
  }

  public <T> List<FlowStep> definitionExport() {
    List definitions = new ArrayList();
    for (FlowDefinition flowDefinition : getFlowDefinitions()) {
      FlowStep flowStep = new FlowStep();
      definitions.add(flowStep);
      // Fill the map.
      flowDefinition.getStartCommand().definitionExport(flowStep);
    }
    return definitions;
  }
}
