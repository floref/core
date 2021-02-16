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

import org.floref.core.dsl.flow.data.FlowDefinition;
import org.floref.core.dsl.flow.from.From;
import org.floref.core.dsl.flow.from.FromBaseInstructionImpl;
import org.floref.core.dsl.flow.impex.FlowImpex;
import org.floref.core.exception.FlowValidationException;
import org.floref.core.flow.build.FlowInstanceData;
import org.floref.core.flow.reference.*;
import org.floref.core.flow.registry.FlowRegistry;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Used to define/build a flow and also to export/import flows.
 *
 * @author Cristian Donoiu
 */
public class Flows {

  /**
   * Call it to startCommand the definition of a workflow. FC designes the flow interface class.
   */
  public static <FC> FromBaseInstructionImpl<From<FC>, FC> from(ParamSupplier<FC> consumer) {
    return new From().from(consumer);
  }

  public static <FC> FromBaseInstructionImpl<From<FC>, FC> from(ParamConsumer<FC> consumer) {
    return new From().from(consumer);
  }

  public static <FC, U> FromBaseInstructionImpl<From<FC>, FC> from(ParamBiConsumer<FC, U> consumer) {
    return new From().from(consumer);
  }

  public static <FC, U, V> FromBaseInstructionImpl<From<FC>, FC> from(ParamTriConsumer<FC, U, V> consumer) {
    return new From().from(consumer);
  }

  public static <FC, U, V, X> FromBaseInstructionImpl<From<FC>, FC> from(ParamTetraConsumer<FC, U, V, X> consumer) {
    return new From().from(consumer);
  }

  public static <FC, U, V, X, Y> FromBaseInstructionImpl<From<FC>, FC> from(ParamPentaConsumer<FC, U, V, X, Y> consumer) {
    return new From().from(consumer);
  }

  public static <FC, U, V, X, Y, Z> FromBaseInstructionImpl<From<FC>, FC> from(ParamHexaConsumer<FC, U, V, X, Y, Z> consumer) {
    return new From().from(consumer);
  }

  public static <FC, U, V, X, Y, Z, A> FromBaseInstructionImpl<From<FC>, FC> from(ParamHeptaConsumer<FC, U, V, X, Y, Z, A> consumer) {
    return new From().from(consumer);
  }

  public static <FC, U, V, X, Y, Z, A, B> FromBaseInstructionImpl<From<FC>, FC> from(ParamOctaConsumer<FC, U, V, X, Y, Z, A, B> consumer) {
    return new From().from(consumer);
  }

  public static <FC> FromBaseInstructionImpl<From<FC>, FC> from(LambdaMeta<FC> lambdaMeta) {
    return new From().from(lambdaMeta);
  }

  /**
   * There are several options to obtain a flow in order to execute it:<br>
   * <ul>
   * <li>1. Directly from definition: <code>MyFlows myFlows = Flows.from(MyFlows::flow1).to(...).build();</code>
   * <li>2. Using this method.
   * <li>3. If you use Spring just autowire/inject it from everywhere:
   *   <code>@Autowired
   *   MyFlows myFlows;
   *   </code>
   */
  public static <T> T get(Class<T> flowInterfaceClass) {
    return FlowRegistry.getFlow(flowInterfaceClass);
  }

  public static <T> T get(Class<T> flowInterfaceClass, String id) {
    return FlowRegistry.getFlow(flowInterfaceClass, id);
  }

  /**
   * Deletes all flows and invalidates them in case they are still used in the code, executing them again should throw an exception.
   */
  public static void deleteById() {
    FlowRegistry.deleteAll();
  }

  /**
   * Deletes all flows and invalidates them in case they are still used in the code, executing them again should throw an exception.
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
   * Loads one or multiple flows. If a flow already exists then it will be replaced.
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
   * will mark as valid (also other parents could call the same child flow) also childs so that they are not validated in isolation (this performs as topologic sort)
   * so that the flows with most of the dependencies are validated first.
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
