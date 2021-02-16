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

import static org.floref.core.dsl.command.FlowCommandBuilders.FORK;
import static org.floref.core.dsl.command.FlowCommandBuilders.TO;

/**
 * Contains all the fork commands.
 *
 * @author Cristian Donoiu
 */
public interface IFork<P> extends FlowInstruction {

  default P fork(ParamVoidConsumerVoidSupplier consumer) {
    return (P) getFlowData().addChild(FORK, consumer);
  }

  default <T> P fork(ParamSupplier<T> consumer) {
    return (P) getFlowData().addChild(FORK, consumer);
  }

  default <T> P fork(ParamConsumer<T> consumer) {
    return (P) getFlowData().addChild(FORK, consumer);
  }

  default <T, U> P fork(ParamBiConsumer<T, U> consumer) {
    return (P) getFlowData().addChild(FORK, consumer);
  }

  default <T, U, V> P fork(ParamTriConsumer<T, U, V> consumer) {
    return (P) getFlowData().addChild(FORK, consumer);
  }

  default <T, U, V, X> P fork(ParamTetraConsumer<T, U, V, X> consumer) {
    return (P) getFlowData().addChild(FORK, consumer);
  }

  default <T, U, V, X, Y> P fork(ParamPentaConsumer<T, U, V, X, Y> consumer) {
    return (P) getFlowData().addChild(FORK, consumer);
  }

  default <T, U, V, X, Y, Z> P fork(ParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    return (P) getFlowData().addChild(FORK, consumer);
  }

  default <T, U, V, X, Y, Z, A> P fork(ParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer) {
    return (P) getFlowData().addChild(FORK, consumer);
  }

  default <T> P fork(LambdaMeta<T> lambdaMeta) {
    return (P) getFlowData().addChild(TO, lambdaMeta);
  }
}
