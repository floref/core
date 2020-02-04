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

import static org.floref.core.dsl.command.FlowCommandBuilders.TO;

import org.floref.core.dsl.command.FlowCommandBuilders;
import org.floref.core.flow.reference.*;

/**
 * Contains all the .to commands.
 *
 * @author Cristian Donoiu
 */
public interface To<FLOW_DSL> extends FlowDsl<FLOW_DSL> {

  // For methods that do not have arguments and return void.
  default FLOW_DSL to(ParamVoidConsumerVoidSupplier consumer) {
    return addChild(TO, consumer);
  }

  default <T> FLOW_DSL to(ParamSupplier<T> consumer) {
    return addChild(TO, consumer);
  }

  default <T> FLOW_DSL to(ParamConsumer<T> consumer) {
    return addChild(TO, consumer);
  }

  default <T, U> FLOW_DSL to(ParamBiConsumer<T, U> consumer) {
    return addChild(TO, consumer);
  }

  default <T, U, V> FLOW_DSL to(ParamTriConsumer<T, U, V> consumer) {
    return addChild(TO, consumer);
  }

  default <T, U, V, X> FLOW_DSL to(ParamTetraConsumer<T, U, V, X> consumer) {
    return addChild(TO, consumer);
  }

  default <T, U, V, X, Y> FLOW_DSL to(ParamPentaConsumer<T, U, V, X, Y> consumer) {
    return addChild(TO, consumer);
  }

  default <T, U, V, X, Y, Z> FLOW_DSL to(ParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    return addChild(TO, consumer);
  }

  default <T, U, V, X, Y, Z, A> FLOW_DSL to(ParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer){
    return addChild(TO, consumer);
  }

  default <T> FLOW_DSL to(LambdaMeta<T> lambdaMeta) {
    return addChild(TO, lambdaMeta);
  }

  //  @FunctionalInterface
//  public interface TwoParam<T> {
//    Object accept(Object o1, Object o2);
//  }
//  public static void to(TwoParam<?> c) {  // Consider allowing inline lambda expressions .to((a,b) -> {...}) still this will miss type information.
//
//  }
}
