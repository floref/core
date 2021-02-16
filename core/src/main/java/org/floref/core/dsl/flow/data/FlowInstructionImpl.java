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

import org.floref.core.flow.build.FlowInstanceBuilder;
import org.floref.core.flow.registry.FlowRegistry;

public class FlowInstructionImpl<FC> implements FlowInstruction {
  FlowData flowData = new FlowData();

  /**
   * Build the flow by creating the flow instance.
   * @return
   */
  public FC build() {
    FlowInstanceBuilder.build(flowData.getFlowDefinition(), false);
    FC flow = (FC) FlowRegistry.getFlow(flowData.getFlowDefinition());
    return flow;
  }

  @Override
  public FlowData getFlowData() {
    return flowData;
  }

  public void setFlowData(FlowData flowData) {
    this.flowData = flowData;
  }

  protected void copyData(FlowInstruction previous) {
    setFlowData(previous.getFlowData()); // Just copy it so that each instruction acts on the same.
    getFlowData().setInstruction(this);
  }
}