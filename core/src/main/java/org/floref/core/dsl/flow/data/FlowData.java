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
import org.floref.core.dsl.command.FlowCommandBuilders;
import org.floref.core.dsl.command.group.ParentCommand;

/**
 * Keeps the flow definition and can add commands to it.
 *
 * @author Cristian Donoiu
 */
public class FlowData<I, F> {   // I flow instruction, F flow class

  protected FlowDefinition<F> flowDefinition;
  protected ParentCommand currentParent;
  protected FlowCommand currentCommand;
  protected I instruction;

  public FlowDefinition<F> getFlowDefinition() {
    return flowDefinition;
  }

  public void setFlowDefinition(FlowDefinition<F> flowDefinition) {
    this.flowDefinition = flowDefinition;
  }

  public ParentCommand getCurrentParent() {
    return currentParent;
  }

  public void setCurrentParent(ParentCommand flowCommand) {
    currentParent = flowCommand;
  }

  public void setCurrentParentAndRelations(ParentCommand flowCommand) {
    ParentCommand currentParent = getCurrentParent();
    if (currentParent != null) {
      flowCommand.setParent(currentParent);  // set the relation both ways.
      currentParent.addChild(flowCommand);
    }
    setCurrentParent(flowCommand);
  }

  public FlowCommand getCurrentCommand() {
    return currentCommand;
  }

  public void setCurrentCommand(FlowCommand flowCommand) {
    currentCommand = flowCommand;
  }

  public I addChild(FlowCommand flowCommand) {
    getCurrentParent().addChild(flowCommand);  // command set currentParent from dsl and inverse
    setCurrentCommand(flowCommand);
    return instruction;
  }

  public I addChild(String id, Object... params) {
    FlowCommand flowCommand = FlowCommandBuilders.build(id, params);
    this.setCurrentCommand(flowCommand);
    return addChild(flowCommand);
  }

  public I addParent(String id, Object... params) {
    ParentCommand flowCommand = (ParentCommand)FlowCommandBuilders.build(id, params);
    setCurrentParentAndRelations(flowCommand);
    setCurrentCommand(flowCommand);
    return instruction;
  }

  public I getInstruction() {
    return instruction;
  }

  public void setInstruction(I instruction) {
    this.instruction = instruction;
  }
}
