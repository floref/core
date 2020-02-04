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
import org.floref.core.flow.build.FlowInstanceBuilder;
import org.floref.core.flow.reference.*;

/**
 * Base class containing FlowRef DSL. Fluent API with minimal set of commands.
 *
 * @author Cristian Donoiu
 */
public class From extends FlowDataImpl {

  private static final Log LOG = LogFactory.getLog(From.class);


  protected <FLOW_CLASS> FlowBase<FLOW_CLASS> init(FromCommand fromCommand, boolean update) {

    flowDefinition.setStartCommand(fromCommand);

    FlowBase<FLOW_CLASS> flowFlowBaseFlow = new FlowBase();
    flowFlowBaseFlow.setFlowDefinition(flowDefinition);
    flowFlowBaseFlow.setCurrentCommand(fromCommand);
    flowFlowBaseFlow.setCurrentParent(fromCommand);
    FlowInstanceBuilder.build(flowDefinition, update); // build the flow upright, definition is always interpreted.
    return flowFlowBaseFlow;
  }

  protected <FLOW_CLASS> FlowBase<FLOW_CLASS> preBuild(MethodReference<FLOW_CLASS> flowRef) {
    flowDefinition = new FlowDefinition(flowRef);
    LOG.debug("new flow: " + flowDefinition.getId());
    FromCommand fromCommand = new FromCommand(flowRef);
    return init(fromCommand, false);
  }

  protected <FLOW_CLASS> FlowBase<FLOW_CLASS> preBuild(LambdaMeta lambdaMeta) {
    flowDefinition = new FlowDefinition(lambdaMeta);
    LOG.debug("new flow: " + flowDefinition.getId());
    FromCommand fromCommand = new FromCommand(lambdaMeta);
    return init(fromCommand, true);
  }

  /**
   * Call it to startCommand the definition of a workflow.
   */
  public <FLOW_CLASS> FlowBase<FLOW_CLASS> from(ParamSupplier<FLOW_CLASS> consumer) {
    return preBuild(consumer);
  }

  public <FLOW_CLASS> FlowBase<FLOW_CLASS> from(ParamConsumer<FLOW_CLASS> consumer) {
    return preBuild(consumer);
  }

  public <FLOW_CLASS, U> FlowBase<FLOW_CLASS> from(ParamBiConsumer<FLOW_CLASS, U> consumer) {
    return preBuild(consumer);
  }

  public <FLOW_CLASS, U, V> FlowBase<FLOW_CLASS> from(ParamTriConsumer<FLOW_CLASS, U, V> consumer) {
    return preBuild(consumer);
  }

  public <FLOW_CLASS, U, V, X> FlowBase<FLOW_CLASS> from(ParamTetraConsumer<FLOW_CLASS, U, V, X> consumer) {
    return preBuild(consumer);
  }

  public <FLOW_CLASS, U, V, X, Y> FlowBase<FLOW_CLASS> from(
      ParamPentaConsumer<FLOW_CLASS, U, V, X, Y> consumer) {
    return preBuild(consumer);
  }

  public <FLOW_CLASS, U, V, X, Y, Z> FlowBase<FLOW_CLASS> from(
      ParamHexaConsumer<FLOW_CLASS, U, V, X, Y, Z> consumer) {
    return preBuild(consumer);
  }

  public <FLOW_CLASS, U, V, X, Y, Z, A> FlowBase<FLOW_CLASS> from(
      ParamHeptaConsumer<FLOW_CLASS, U, V, X, Y, Z, A> consumer) {
    return preBuild(consumer);
  }

  public <FLOW_CLASS, U, V, X, Y, Z, A, B> FlowBase<FLOW_CLASS> from(
      ParamOctaConsumer<FLOW_CLASS, U, V, X, Y, Z, A, B> consumer) {
    return preBuild(consumer);
  }

  public <FLOW_CLASS> FlowBase<FLOW_CLASS> from(LambdaMeta<FLOW_CLASS> lambdaMeta) {
    return preBuild(lambdaMeta);
  }
}
