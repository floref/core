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

import static org.floref.core.dsl.command.CommandUtil.setRevertBy;
import static org.floref.core.dsl.command.FlowCommandBuilders.COMPENSATE;

import org.floref.core.dsl.flow.FlowDsl;
import org.floref.core.flow.reference.ParamBiConsumer;
import org.floref.core.flow.reference.ParamConsumer;
import org.floref.core.flow.reference.ParamHeptaConsumer;
import org.floref.core.flow.reference.ParamHexaConsumer;
import org.floref.core.flow.reference.ParamPentaConsumer;
import org.floref.core.flow.reference.ParamSupplier;
import org.floref.core.flow.reference.ParamTetraConsumer;
import org.floref.core.flow.reference.ParamTriConsumer;
import org.floref.core.flow.reference.ParamVoidConsumerVoidSupplier;

/**
 * Contains .compensate command and configuration methods.
 *
 * @author Cristian Donoiu
 */
public interface Compensate<FLOW_DSL> extends FlowDsl<FLOW_DSL>, GroupUtil<FLOW_DSL> {

  default FLOW_DSL compensate() {
    return addParent(COMPENSATE);
  }

  default FLOW_DSL revertBy(ParamVoidConsumerVoidSupplier consumer) {
    return setRevertBy(this, consumer);
  }

  default <T> FLOW_DSL revertBy(ParamSupplier<T> consumer) {
    return setRevertBy(this, consumer);
  }

  default <T> FLOW_DSL revertBy(ParamConsumer<T> consumer) {
    return setRevertBy(this, consumer);
  }

  default <T, U> FLOW_DSL revertBy(ParamBiConsumer<T, U> consumer) {
    return setRevertBy(this, consumer);
  }

  default <T, U, V> FLOW_DSL revertBy(ParamTriConsumer<T, U, V> consumer) {
    return setRevertBy(this, consumer);
  }

  default <T, U, V, X> FLOW_DSL revertBy(ParamTetraConsumer<T, U, V, X> consumer) {
    return setRevertBy(this, consumer);
  }

  default <T, U, V, X, Y> FLOW_DSL revertBy(ParamPentaConsumer<T, U, V, X, Y> consumer) {
    return setRevertBy(this, consumer);
  }

  default <T, U, V, X, Y, Z> FLOW_DSL revertBy(ParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    return addChild(COMPENSATE, consumer);
  }

  default <T, U, V, X, Y, Z, A> FLOW_DSL revertBy(ParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer){
    return addChild(COMPENSATE, consumer);
  }
}
