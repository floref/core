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

package org.floref.core.dsl.flow.data;


import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.dsl.command.group.ParentCommand;
import org.floref.core.dsl.validator.FlowValidator;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.reference.LambdaMetaBuilder;
import org.floref.core.flow.reference.MethodReference;
import org.floref.core.flow.reference.Methods;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * The flow definition contains all the instructions for running a flow. The flow definition id is
 * <code>FlowClass::method</code>
 * Thus two different definitions can exist for the same method but their id will be the same.
 *
 * @param <T> is the flow class.
 * @author Cristian Donoiu
 */
public class FlowDefinition<T> {
  String id;
  boolean validated;
  FlowCommand startCommand;
  LambdaMeta flowReference;  // This is the flow reference.

  public FlowDefinition(MethodReference<T> flowRef) {
    init(flowRef);
  }

  public FlowDefinition(LambdaMeta lambdaMeta) {
    init(lambdaMeta);
  }


  public FlowDefinition() {

  }

  protected void init(MethodReference<T> flowRef) {
    flowReference = LambdaMetaBuilder.getLambdaMeta(flowRef);
    if (flowReference.getTarget() != null) {
      throw new FlowDefinitionException("Flow reference must refer flow class: 'from(FlowClass::method)'");
    }
    id = getIdFromFlowRef(flowReference.getLambdaMethod());
  }

  protected void init(LambdaMeta lambdaMeta) {
    flowReference = lambdaMeta;
    if (flowReference.getTarget() != null) {
      throw new FlowDefinitionException("Flow reference must refer flow class: 'from(FlowClass::method)'");
    }
    id = getIdFromFlowRef(flowReference.getLambdaMethod());
  }

  public LambdaMeta<T> getFlowReference() {
    return flowReference;
  }

  public String getId() {
    return id;
  }

  public FlowCommand getStartCommand() {
    return startCommand;
  }

  public void setStartCommand(FlowCommand startCommand) {
    this.startCommand = startCommand;
  }

  public Class<T> getFlowClass() {
    return getFlowReference().getLambdaClass();
  }

  public String getFlowClassCanonicalName() {
    return getFlowClass().getCanonicalName();
  }

  public void validate() {
    if (!validated) {
      FlowValidator.validate(this);
      validated = true;
    }
  }

  public static String getIdFromFlowRef(Method method) {
    return Methods.getMethodReferenceAsString(method);
  }

  private void navigate(FlowCommand flowCommand, List<FlowCommand> flowCommands) {
    flowCommands.add(flowCommand);
    if (flowCommand instanceof ParentCommand) {
      for (FlowCommand child : ((ParentCommand) flowCommand).getChildren()) {
        navigate(child, flowCommands);
      }
    }
  }

  /**
   * @return the list of commands by navigating thru all.
   */
  public List<FlowCommand> getFlowCommands() {
    List<FlowCommand> flowCommands = new ArrayList<>();
    navigate(startCommand, flowCommands);
    return flowCommands;
  }

  public void setId(String id) {
    this.id = id;
  }
}
