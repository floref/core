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
package org.floref.core.dsl.flow.retry;

import org.floref.core.dsl.flow.data.FlowInstruction;
import org.floref.core.flow.reference.*;

import static org.floref.core.dsl.command.FlowCommandBuilders.RETRY;

/**
 * .retry(classOrInstance::method) and utils.
 */
public interface IRetry<P, F> extends FlowInstruction {

  default Retry<P, F> retry(ParamVoidConsumerVoidSupplier consumer) {
    return new Retry(this, consumer);
  }

  default <T> Retry<P, F> retry(ParamSupplier<T> consumer) {
    return new Retry(this, consumer);
  }

  default <T> Retry<P, F> retry(ParamConsumer<T> consumer) {
    return new Retry(this, consumer);
  }

  default <T, U> Retry<P, F> retry(ParamBiConsumer<T, U> consumer) {
    return new Retry(this, consumer);
  }

  default <T, U, V> Retry<P, F> retry(ParamTriConsumer<T, U, V> consumer) {
    return new Retry(this, consumer);
  }

  default <T, U, V, X> Retry<P, F> retry(ParamTetraConsumer<T, U, V, X> consumer) {
    return new Retry(this, consumer);
  }

  default <T, U, V, X, Y> Retry<P, F> retry(ParamPentaConsumer<T, U, V, X, Y> consumer) {
    return new Retry(this, consumer);
  }

  default <T, U, V, X, Y, Z> Retry<P, F> retry(ParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    return new Retry(this, consumer);
  }

  default <T, U, V, X, Y, Z, A> Retry<P, F> retry(ParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer){
    return new Retry(this, consumer);
  }

  default <T> Retry<P, F> retry(LambdaMeta<T> lambdaMeta) {
    return (Retry<P, F>)getFlowData().addChild(RETRY, lambdaMeta);
  }
}
