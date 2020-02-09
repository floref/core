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

import org.floref.core.dsl.flow.data.FlowInstruction;
import org.floref.core.flow.reference.*;

import static org.floref.core.dsl.command.FlowCommandBuilders.TO;

/**
 * Contains all the .to commands.
 *
 * @author Cristian Donoiu
 */
public interface To<P> extends FlowInstruction {

  default P to(ParamVoidConsumerVoidSupplier consumer) {
    return (P)getFlowData().addChild(TO, consumer);
  }

  default <T> P to(ParamSupplier<T> consumer) {
    return (P)getFlowData().addChild(TO, consumer);
  }

  default <T> P to(ParamConsumer<T> consumer) {
    return (P)getFlowData().addChild(TO, consumer);
  }

  default <T, U> P to(ParamBiConsumer<T, U> consumer) {
    return (P)getFlowData().addChild(TO, consumer);
  }

  default <T, U, V> P to(ParamTriConsumer<T, U, V> consumer) {
    return (P)getFlowData().addChild(TO, consumer);
  }

  default <T, U, V, X> P to(ParamTetraConsumer<T, U, V, X> consumer) {
    return (P)getFlowData().addChild(TO, consumer);
  }

  default <T, U, V, X, Y> P to(ParamPentaConsumer<T, U, V, X, Y> consumer) {
    return (P)getFlowData().addChild(TO, consumer);
  }

  default <T, U, V, X, Y, Z> P to(ParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    return (P)getFlowData().addChild(TO, consumer);
  }

  default <T, U, V, X, Y, Z, A> P to(ParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer){
    return (P)getFlowData().addChild(TO, consumer);
  }

  default <T> P to(LambdaMeta<T> lambdaMeta) {
    return (P)getFlowData().addChild(TO, lambdaMeta);
  }

  //  @FunctionalInterface
//  public interface TwoParam<T> {
//    Object accept(Object o1, Object o2);
//  }
//  public static void to(TwoParam<?> c) {  // Consider allowing inline lambda expressions .to((a,b) -> {...}) still this will miss type information.
//
//  }

  default P alias(String alias) {
    getFlowData().getCurrentCommand().alias(alias);
    return (P)this;
  }
}
