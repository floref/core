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

package org.floref.core.dsl.flow.from;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.dsl.command.FromCommand;
import org.floref.core.dsl.flow.data.FlowData;
import org.floref.core.dsl.flow.data.FlowDefinition;
import org.floref.core.flow.build.FlowInstanceBuilder;
import org.floref.core.flow.reference.*;

/**
 * Used to start a flow from a method reference. Calling that method will start the flow.
 *
 * @author Cristian Donoiu
 */
public class From<FC> extends FlowData {
  private static final Log LOG = LogFactory.getLog(From.class);

  protected <FC> FromBaseInstructionImpl<From, FC> getBase(FromCommand fromCommand, boolean update) {

    flowDefinition.setStartCommand(fromCommand);

    FromBaseInstructionImpl<From, FC> fromBase = new FromBaseInstructionImpl();
    FlowData flowData = fromBase.getFlowData();
    flowData.setFlowDefinition(flowDefinition);
    flowData.setCurrentCommand(fromCommand);
    flowData.setCurrentParent(fromCommand);
    flowData.setInstruction(fromBase);
//    FlowInstanceBuilder.build(flowDefinition, update); // build the flow upright, definition is always interpreted.
    return fromBase;
  }

  protected <FC> FromBaseInstructionImpl<From, FC> preBuild(MethodReference<FC> flowRef) {
    flowDefinition = new FlowDefinition(flowRef);
    LOG.debug("new flow: " + flowDefinition.getId());
    FromCommand fromCommand = new FromCommand(flowRef);
    return getBase(fromCommand, false);
  }

  protected <FC> FromBaseInstructionImpl<From, FC> preBuild(LambdaMeta lambdaMeta) {
    flowDefinition = new FlowDefinition(lambdaMeta);
    LOG.debug("new flow: " + flowDefinition.getId());
    FromCommand fromCommand = new FromCommand(lambdaMeta);
    return getBase(fromCommand, true);
  }

  /**
   * Call it to startCommand the definition of a workflow.
   */
  public <FC> FromBaseInstructionImpl<From, FC> from(ParamSupplier<FC> consumer) {
    return preBuild(consumer);
  }

  public <FC> FromBaseInstructionImpl<From, FC> from(ParamConsumer<FC> consumer) {
    return preBuild(consumer);
  }

  public <FC, U> FromBaseInstructionImpl<From, FC> from(ParamBiConsumer<FC, U> consumer) {
    return preBuild(consumer);
  }

  public <FC, U, V> FromBaseInstructionImpl<From, FC> from(ParamTriConsumer<FC, U, V> consumer) {
    return preBuild(consumer);
  }

  public <FC, U, V, X> FromBaseInstructionImpl<From, FC> from(ParamTetraConsumer<FC, U, V, X> consumer) {
    return preBuild(consumer);
  }

  public <FC, U, V, X, Y> FromBaseInstructionImpl<From, FC> from(ParamPentaConsumer<FC, U, V, X, Y> consumer) {
    return preBuild(consumer);
  }

  public <FC, U, V, X, Y, Z> FromBaseInstructionImpl<From, FC> from(ParamHexaConsumer<FC, U, V, X, Y, Z> consumer) {
    return preBuild(consumer);
  }

  public <FC, U, V, X, Y, Z, A> FromBaseInstructionImpl<From, FC> from(ParamHeptaConsumer<FC, U, V, X, Y, Z, A> consumer) {
    return preBuild(consumer);
  }

  public <FC, U, V, X, Y, Z, A, B> FromBaseInstructionImpl<From, FC> from(ParamOctaConsumer<FC, U, V, X, Y, Z, A, B> consumer) {
    return preBuild(consumer);
  }

  public <FC> FromBaseInstructionImpl<From, FC> from(LambdaMeta<FC> lambdaMeta) {
    return preBuild(lambdaMeta);
  }

}
