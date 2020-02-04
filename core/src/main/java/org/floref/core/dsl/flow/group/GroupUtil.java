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

import static org.floref.core.dsl.command.CommandUtil.setAggregator;

import org.floref.core.dsl.command.CommandUtil;
import org.floref.core.dsl.flow.FlowDsl;
import org.floref.core.flow.reference.ParamBiConsumer;
import org.floref.core.flow.reference.ParamConsumer;
import org.floref.core.flow.reference.ParamHeptaConsumer;
import org.floref.core.flow.reference.ParamHexaConsumer;
import org.floref.core.flow.reference.ParamPentaConsumer;
import org.floref.core.flow.reference.ParamTetraConsumer;
import org.floref.core.flow.reference.ParamTriConsumer;

/**
 * Contains group util commands.
 *
 * @author Cristian Donoiu
 */
public interface GroupUtil<FLOW_DSL> extends FlowDsl<FLOW_DSL> {

  default FLOW_DSL stopOnException() {
    return CommandUtil.stopOnException(this);
  }
  default FLOW_DSL timeout(long millis) {
    return CommandUtil.setTimeout(this, millis);
  }

  default <T> FLOW_DSL aggregator(ParamConsumer<T> consumer) {
    return setAggregator(this, consumer);
  }

  default <T, U> FLOW_DSL aggregator(ParamBiConsumer<T, U> consumer) {
    return setAggregator(this, consumer);
  }

  default <T, U, V> FLOW_DSL aggregator(ParamTriConsumer<T, U, V> consumer) {
    return setAggregator(this, consumer);
  }

  default <T, U, V, X> FLOW_DSL aggregator(ParamTetraConsumer<T, U, V, X> consumer) {
    return setAggregator(this, consumer);
  }

  default <T, U, V, X, Y> FLOW_DSL aggregator(ParamPentaConsumer<T, U, V, X, Y> consumer) {
    return setAggregator(this, consumer);
  }

  default <T, U, V, X, Y, Z> FLOW_DSL aggregator(ParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    return setAggregator(this, consumer);
  }

  default <T, U, V, X, Y, Z, A> FLOW_DSL aggregator(ParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer){
    return setAggregator(this, consumer);
  }

}
