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

import org.floref.core.dsl.command.group.ParentCommand;
import org.floref.core.dsl.flow.group.Compensate;
import org.floref.core.dsl.flow.group.Parallel;
import org.floref.core.dsl.flow.group.ForEach;
import org.floref.core.dsl.flow.group.WhenOtherwise;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.registry.FlowRegistry;

/**
 * Workflow base class allowing the call of any other flow commands.
 *
 * @author Cristian Donoiu
 */
public class FlowBase<FLOW_CLASS> extends FlowDataImpl<FlowBase<FLOW_CLASS>, FLOW_CLASS> implements
    To<FlowBase<FLOW_CLASS>>,
    Fork<FlowBase<FLOW_CLASS>>,
    WhenOtherwise<FlowBase<FLOW_CLASS>>,
    Parallel<FlowBase<FLOW_CLASS>>,
    ForEach<FlowBase<FLOW_CLASS>>,
    Compensate<FlowBase<FLOW_CLASS>> {

  public FLOW_CLASS build() {
    FLOW_CLASS flow = (FLOW_CLASS) FlowRegistry.getFlow(flowDefinition);
    return flow;
  }

  public FlowBase<FLOW_CLASS> end() {
    // End the last group.
    ParentCommand grandParent = currentParent.getParent();
    if (grandParent == null) {
      throw new FlowDefinitionException("'.end()' is missing a block command to be ended. Too many '.end()'.");
    }
    currentParent = grandParent;
    return this;
  }

  public FlowBase<FLOW_CLASS> alias(String alias) {
    getCurrentCommand().alias(alias);
    return this;
  }

}
