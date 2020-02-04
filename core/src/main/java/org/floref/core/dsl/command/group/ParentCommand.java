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

package org.floref.core.dsl.command.group;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.dsl.command.ChildCommand;
import org.floref.core.dsl.flow.runtime.FlowStep;
import org.floref.core.flow.run.CommandContext;

/**
 * Group command that can group multiple child commands.
 */
public abstract class ParentCommand extends ChildCommand {
  protected List<FlowCommand> children = new ArrayList<>();

  public List<FlowCommand> getChildren() {
    return children;
  }

  public void addChild(FlowCommand flowCommand) {
    if (!children.contains(flowCommand)) {
      children.add(flowCommand);
      flowCommand.setParent(this);
    }
  }

  protected void runChildren(CommandContext commandContext) throws Exception {
    for (FlowCommand command : children) {
      command.run(commandContext);
      //commandContext.moveResultToArguments();
    }
  }

  @Override
  public void definitionExport(FlowStep flowStep) {
    List steps = new ArrayList();
    for (FlowCommand child : getChildren()) {
      FlowStep childFlowStep = new FlowStep();
      child.definitionExport(childFlowStep);
      flowStep.addStep(childFlowStep);
    }
  }
  @Override
  public void definitionImport(FlowStep flowStep) {

  }
}
