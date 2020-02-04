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
import org.floref.core.dsl.command.FlowCommandBuilders;
import org.floref.core.dsl.command.group.ParentCommand;

/**
 * Interface implemented by all flow instructions. It contains methods common to most of the instructions.
 * @param <FLOW_DSL> represents the flow instruction itself.
 *
 * @author Cristian Donoiu
 */
public interface FlowDsl<FLOW_DSL> {

  // Consider moving all these to CommandUtil to not pollute the API.
  default FlowCommand buildCommand(String id, Object... args) {
    return FlowCommandBuilders.build(id, args);
  }

  default FLOW_DSL addChild(FlowCommand flowCommand) {
    getCurrentParent().addChild(flowCommand);  // command set currentParent from dsl and inverse
    setCurrentCommand(flowCommand);
    return (FLOW_DSL)this;
  }

  default FLOW_DSL addChild(String id, Object... params) {
    FlowCommand flowCommand = buildCommand(id, params);
    setCurrentCommand(flowCommand);
    return addChild(flowCommand);
  }

  default FLOW_DSL addParent(String id, Object... params) {
    ParentCommand flowCommand = (ParentCommand)buildCommand(id, params);
    setCurrentParentAndRelations(flowCommand);
    setCurrentCommand(flowCommand);
    return (FLOW_DSL)this;
  }

  ParentCommand getCurrentParent();
  void setCurrentParent(ParentCommand flowCommand);
  FlowCommand getCurrentCommand();
  void setCurrentCommand(FlowCommand flowCommand);
  void setCurrentParentAndRelations(ParentCommand flowCommand);

}
