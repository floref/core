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

import static org.floref.core.dsl.command.FlowCommandBuilders.FORK;
import static org.floref.core.dsl.command.FlowCommandBuilders.TO;

import org.floref.core.flow.reference.*;

/**
 * Contains all the fork commands.
 *
 * @author Cristian Donoiu
 */
public interface Fork<FLOW_DSL> extends FlowDsl<FLOW_DSL> {

  // For methods that do not have arguments and return void.
  default FLOW_DSL fork(ParamVoidConsumerVoidSupplier consumer) {
    return addChild(FORK, consumer);
  }

  default <T> FLOW_DSL fork(ParamSupplier<T> consumer) {
    return addChild(FORK, consumer);
  }

  default <T> FLOW_DSL fork(ParamConsumer<T> consumer) {
    return addChild(FORK, consumer);
  }

  default <T, U> FLOW_DSL fork(ParamBiConsumer<T, U> consumer) {
    return addChild(FORK, consumer);
  }

  default <T, U, V> FLOW_DSL fork(ParamTriConsumer<T, U, V> consumer) {
    return addChild(FORK, consumer);
  }

  default <T, U, V, X> FLOW_DSL fork(ParamTetraConsumer<T, U, V, X> consumer) {
    return addChild(FORK, consumer);
  }

  default <T, U, V, X, Y> FLOW_DSL fork(ParamPentaConsumer<T, U, V, X, Y> consumer) {
    return addChild(FORK, consumer);
  }

  default <T, U, V, X, Y, Z> FLOW_DSL fork(ParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    return addChild(FORK, consumer);
  }

  default <T, U, V, X, Y, Z, A> FLOW_DSL fork(ParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer){
    return addChild(FORK, consumer);
  }

  default <T> FLOW_DSL fork(LambdaMeta<T> lambdaMeta) {
    return addChild(TO, lambdaMeta);
  }
}
