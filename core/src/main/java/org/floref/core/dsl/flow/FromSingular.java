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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.dsl.command.FromCommand;
import org.floref.core.dsl.flow.data.FlowData;
import org.floref.core.dsl.flow.data.FlowDefinition;
import org.floref.core.flow.build.FlowInstanceBuilder;
import org.floref.core.flow.reference.*;
import org.floref.core.flow.registry.FlowRegistry;

/**
 * Base class containing FlowRef DSL. Fluent API with minimal set of commands.
 *
 * @author Cristian Donoiu
 */
public class FromSingular<FLOW_CLASS> extends FlowData {

  private static final Log LOG = LogFactory.getLog(FromSingular.class);

  protected <FLOW_CLASS> BaseSingular<FromSingular, FLOW_CLASS, ?, ?, ?, ?, ?, ?, ?> init(FromCommand fromCommand, boolean update) {

    flowDefinition.setStartCommand(fromCommand);

    BaseSingular<FromSingular, FLOW_CLASS, ?, ?, ?, ?, ?, ?, ?> baseInstruction = new BaseSingular();
    FlowData flowData = baseInstruction.getFlowData();
    flowData.setFlowDefinition(flowDefinition);
    flowData.setCurrentCommand(fromCommand);
    flowData.setCurrentParent(fromCommand);
    flowData.setInstruction(baseInstruction);
    FlowInstanceBuilder.build(flowDefinition, update); // build the flow upright, definition is always interpreted.
    return baseInstruction;
  }

  protected <FLOW_CLASS> BaseSingular<FromSingular, FLOW_CLASS, ?, ?, ?, ?, ?, ?, ?> preBuild(MethodReference<FLOW_CLASS> flowRef) {
    flowDefinition = new FlowDefinition(flowRef);
    LOG.debug("new flow: " + flowDefinition.getId());
    FromCommand fromCommand = new FromCommand(flowRef);
    return init(fromCommand, false);
  }

  protected <FLOW_CLASS> BaseSingular<FromSingular, FLOW_CLASS, ?, ?, ?, ?, ?, ?, ?> preBuild(LambdaMeta lambdaMeta) {
    flowDefinition = new FlowDefinition(lambdaMeta);
    LOG.debug("new flow: " + flowDefinition.getId());
    FromCommand fromCommand = new FromCommand(lambdaMeta);
    return init(fromCommand, true);
  }

//  /**
//   * Call it to startCommand the definition of a workflow.
//   */
//  public <FLOW_CLASS> BaseSingular<FromSingular, FLOW_CLASS> from(ParamSupplier<FLOW_CLASS> consumer) {
//    return preBuild(consumer);
//  }

//  public <FLOW_CLASS> BaseSingular<FromSingular, FLOW_CLASS> from(ParamConsumer<FLOW_CLASS> consumer) {
//    return preBuild(consumer);
//  }

  public <FLOW_CLASS, T> BaseSingular<FromSingular, FLOW_CLASS, T, ?, ?, ?, ?, ?, ?> from(ParamBiConsumer<FLOW_CLASS, T> consumer) {
    return (BaseSingular<FromSingular, FLOW_CLASS, T, ?, ?, ?, ?, ?, ?>) preBuild(consumer);
  }

//  public <FLOW_CLASS, U> Base<From, FLOW_CLASS> from(ParamBiConsumer<FLOW_CLASS, U> consumer) {
//    return null;
//  }

  public <FLOW_CLASS, T, U> BaseSingular<FromSingular, FLOW_CLASS, T, U, ?, ?, ?, ?, ?> from(ParamTriConsumer<FLOW_CLASS, T, U> consumer) {
    return (BaseSingular<FromSingular, FLOW_CLASS, T, U, ?, ?, ?, ?, ?>) preBuild(consumer);
  }
//
//  public <FLOW_CLASS, U, V, X> BaseSingular<FromSingular, FLOW_CLASS> from(ParamTetraConsumer<FLOW_CLASS, U, V, X> consumer) {
//    return preBuild(consumer);
//  }
//
//  public <FLOW_CLASS, U, V, X, Y> BaseSingular<FromSingular, FLOW_CLASS> from(
//      ParamPentaConsumer<FLOW_CLASS, U, V, X, Y> consumer) {
//    return preBuild(consumer);
//  }
//
//  public <FLOW_CLASS, U, V, X, Y, Z> BaseSingular<FromSingular, FLOW_CLASS> from(
//      ParamHexaConsumer<FLOW_CLASS, U, V, X, Y, Z> consumer) {
//    return preBuild(consumer);
//  }
//
//  public <FLOW_CLASS, U, V, X, Y, Z, A> BaseSingular<FromSingular, FLOW_CLASS> from(
//      ParamHeptaConsumer<FLOW_CLASS, U, V, X, Y, Z, A> consumer) {
//    return preBuild(consumer);
//  }
//
//  public <FLOW_CLASS, U, V, X, Y, Z, A, B> BaseSingular<FromSingular, FLOW_CLASS> from(
//      ParamOctaConsumer<FLOW_CLASS, U, V, X, Y, Z, A, B> consumer) {
//    return preBuild(consumer);
//  }
//
//  public <FLOW_CLASS> BaseSingular<FromSingular, FLOW_CLASS> from(LambdaMeta<FLOW_CLASS> lambdaMeta) {
//    return preBuild(lambdaMeta);
//  }

  public FLOW_CLASS build() {
    FLOW_CLASS flow = (FLOW_CLASS) FlowRegistry.getFlow(getFlowDefinition());
    return flow;
  }



}
