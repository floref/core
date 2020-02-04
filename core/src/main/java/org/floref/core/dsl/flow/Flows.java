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

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import org.floref.core.dsl.flow.runtime.FlowImpex;
import org.floref.core.exception.FlowValidationException;
import org.floref.core.flow.build.FlowInstanceData;
import org.floref.core.flow.reference.*;
import org.floref.core.flow.registry.FlowRegistry;

/**
 * Used to define/build a flow and also to export/import flows.
 * Contains static methods that build FlowRef instances.
 * "from" calls are equivalent with calling <code>new From().from(...)</code>
 * @see From
 * @author Cristian Donoiu
 */
public class Flows {

  /**
   * Call it to startCommand the definition of a workflow.
   */
  public static <FLOW_CLASS> FlowBase<FLOW_CLASS> from(ParamSupplier<FLOW_CLASS> consumer) {
    return new From().from(consumer);
  }

  public static <FLOW_CLASS> FlowBase<FLOW_CLASS> from(ParamConsumer<FLOW_CLASS> consumer) {
    return new From().from(consumer);
  }

  public static <FLOW_CLASS, U> FlowBase<FLOW_CLASS> from(ParamBiConsumer<FLOW_CLASS, U> consumer) {
    return new From().from(consumer);
  }

  public static <FLOW_CLASS, U, V> FlowBase<FLOW_CLASS> from(ParamTriConsumer<FLOW_CLASS, U, V> consumer) {
    return new From().from(consumer);
  }

  public static <FLOW_CLASS, U, V, X> FlowBase<FLOW_CLASS> from(ParamTetraConsumer<FLOW_CLASS, U, V, X> consumer) {
    return new From().from(consumer);
  }

  public static <FLOW_CLASS, U, V, X, Y> FlowBase<FLOW_CLASS> from(
      ParamPentaConsumer<FLOW_CLASS, U, V, X, Y> consumer) {
    return new From().from(consumer);
  }

  public static <FLOW_CLASS, U, V, X, Y, Z> FlowBase<FLOW_CLASS> from(
      ParamHexaConsumer<FLOW_CLASS, U, V, X, Y, Z> consumer) {
    return new From().from(consumer);
  }

  public static <FLOW_CLASS, U, V, X, Y, Z, A> FlowBase<FLOW_CLASS> from(
      ParamHeptaConsumer<FLOW_CLASS, U, V, X, Y, Z, A> consumer) {
    return new From().from(consumer);
  }

  public static <FLOW_CLASS, U, V, X, Y, Z, A, B> FlowBase<FLOW_CLASS> from(
      ParamOctaConsumer<FLOW_CLASS, U, V, X, Y, Z, A, B> consumer) {
    return new From().from(consumer);
  }

  public static <FLOW_CLASS> FlowBase<FLOW_CLASS> from(LambdaMeta<FLOW_CLASS> lambdaMeta) {
    return new From().from(lambdaMeta);
  }

  /**
   * There are several options to obtain a flow in order to execute it:<br>
   * <ul>
   * <li>1. Directly from definition: <code>MyFlows myFlows = FlowRef.from(MyFlows::flow1).to(...).build();</code>
   * <li>2. If you use Spring just autowire/inject it from everywhere:
   * <code>@Autowired
   * MyFlows myFlows;
   * </code>
   * <li>3. Using this method.
   */
  public static <T> T getFlow(Class<T> flowInterfaceClass) {
    return FlowRegistry.getFlow(flowInterfaceClass);
  }

  /**
   * Deletes all flows and invalidates them in case they are still used in the code, executing them again
   * should throw an exception.
   */
  public static void deleteAll() {
    FlowRegistry.deleteAll();
  }

  /**
   * Exports a flow, considers any existing aliases.
   */
  public static <T> String export(Class<T> flowClass) {
    return FlowImpex.exportFlow(flowClass);
  }
  public static String export(Object flowInstance) {
    Objects.requireNonNull(flowInstance, "Flow instance is null");
    return export(FlowRegistry.getFlowInterface(flowInstance));
  }

  public static String exportAll() {
    return FlowImpex.exportAllFlows();
  }


  /**
   * Saves one or multiple flows. If a flow already exists then it will be updated.
   */
  public static void importFlows(String flowJson) {
    FlowImpex.importFlows(flowJson);
  }
  /**
   * Validation of a flow can be executed at different moments:
   * 1. By default flows are validated only on the first run. This is needed in order to not create dependencies
   * requirements like having the interface implementation available when using an interface method reference.
   * 2. There is also early validation at process startup but only after everything is started and configuration
   * property is:<code>flow.validateEarly=true</code>
   * 3. On demand by calling this for validating all. Validating will take one by one in isolation and validate and
   *   will mark as valid (also other parents could call the same child flow) also childs so that they are not validated in isolation (this performs as topologic sort)
   *   so that the flows with most of the dependencies are validated first.
   */
  public static void validateAll() {
    Iterator<FlowInstanceData> iterator = FlowRegistry.allFlowsIterator();
    StringBuilder sb = new StringBuilder();
    while (iterator.hasNext()) {
      List<FlowDefinition> flowDefinitions = iterator.next().getFlowDefinitions();
      for (FlowDefinition flowDefinition : flowDefinitions) {
        try {
          flowDefinition.validate();
        } catch (FlowValidationException e) {
          sb.append(e.getMessage() + "\n");
        }
      }
    }
    if (sb.length() > 0) {
      throw new FlowValidationException(sb.toString());
    }
  }

}
