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

package org.floref.core.dsl.flow;

import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.dsl.command.group.ParentCommand;

/**
 * Keeps the flow definition and can add commands to it.
 *
 * @author Cristian Donoiu
 */
public class FlowDataImpl<FLOW_DSL extends FlowData, FLOW_CLASS>
    implements FlowData<FLOW_CLASS>, FlowDsl<FLOW_DSL> {

  protected FlowDefinition<FLOW_CLASS> flowDefinition;
  protected ParentCommand currentParent;
  protected FlowCommand currentCommand;

  @Override
  public FlowDefinition<FLOW_CLASS> getFlowDefinition() {
    return flowDefinition;
  }

  public void setFlowDefinition(FlowDefinition<FLOW_CLASS> flowDefinition) {
    this.flowDefinition = flowDefinition;
  }

  @Override
  public ParentCommand getCurrentParent() {
    return currentParent;
  }

  @Override
  public void setCurrentParent(ParentCommand flowCommand) {
    currentParent = flowCommand;
  }

  @Override
  public void setCurrentParentAndRelations(ParentCommand flowCommand) {
    ParentCommand currentParent = getCurrentParent();
    if (currentParent != null) {
      flowCommand.setParent(currentParent);  // set the relation both ways.
      currentParent.addChild(flowCommand);
    }
    setCurrentParent(flowCommand);
  }

  @Override
  public FlowCommand getCurrentCommand() {
    return currentCommand;
  }

  @Override
  public void setCurrentCommand(FlowCommand flowCommand) {
    currentCommand = flowCommand;
  }
}
