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

package org.floref.core.dsl.flow.group;

import org.floref.core.dsl.flow.data.FlowInstruction;
import org.floref.core.dsl.flow.data.FlowInstructionUtil;
import org.floref.core.flow.reference.*;

import static org.floref.core.dsl.flow.data.FlowInstructionUtil.setAggregator;

/**
 * Contains group util commands.
 *
 * @author Cristian Donoiu
 */
public interface GroupUtil<I> extends FlowInstruction {

  default I stopOnException() {
    return FlowInstructionUtil.stopOnException(this);
  }
  default I timeout(long millis) {
    return FlowInstructionUtil.setTimeout(this, millis);
  }

  default <T> I aggregator(ParamConsumer<T> consumer) {
    return setAggregator(this, consumer);
  }

  default <T, U> I aggregator(ParamBiConsumer<T, U> consumer) {
    return setAggregator(this, consumer);
  }

  default <T, U, V> I aggregator(ParamTriConsumer<T, U, V> consumer) {
    return setAggregator(this, consumer);
  }

  default <T, U, V, X> I aggregator(ParamTetraConsumer<T, U, V, X> consumer) {
    return setAggregator(this, consumer);
  }

  default <T, U, V, X, Y> I aggregator(ParamPentaConsumer<T, U, V, X, Y> consumer) {
    return setAggregator(this, consumer);
  }

  default <T, U, V, X, Y, Z> I aggregator(ParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    return setAggregator(this, consumer);
  }

  default <T, U, V, X, Y, Z, A> I aggregator(ParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer){
    return setAggregator(this, consumer);
  }

}
