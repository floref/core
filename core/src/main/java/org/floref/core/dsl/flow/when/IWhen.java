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

package org.floref.core.dsl.flow.when;

import org.floref.core.dsl.flow.data.FlowInstruction;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.reference.ParamSupplier;
import org.floref.core.flow.reference.predicate.*;

import static org.floref.core.dsl.command.FlowCommandBuilders.WHEN;

/**
 * Contains when and otherwise commands.
 *
 * @author Cristian Donoiu
 */
public interface IWhen<P, F> extends FlowInstruction {

  default When<P, F> when(ParamSupplier<Boolean> consumer) {
    getFlowData().addParent(WHEN, consumer);
    return new When(this);
  }

  default <T> When<P, F> when(PredicateParamConsumer<T> consumer) {
    getFlowData().addParent(WHEN, consumer);
    return new When(this);
  }

  default <T, U> When<P, F> when(PredicateParamBiConsumer<T, U> consumer) {
    getFlowData().addParent(WHEN, consumer);
    return new When(this);
  }

  default <T, U, V> When<P, F> when(PredicateParamTriConsumer<T, U, V> consumer) {
    getFlowData().addParent(WHEN, consumer);
    return new When(this);
  }

  default <T, U, V, X> When<P, F> when(PredicateParamTetraConsumer<T, U, V, X> consumer) {
    getFlowData().addParent(WHEN, consumer);
    return new When(this);
  }

  default <T, U, V, X, Y> When<P, F> when(PredicateParamPentaConsumer<T, U, V, X, Y> consumer) {
    getFlowData().addParent(WHEN, consumer);
    return new When(this);
  }


  default <T, U, V, X, Y, Z> When<P, F> when(PredicateParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    getFlowData().addParent(WHEN, consumer);
    return new When(this);
  }

  default <T, U, V, X, Y, Z, A> When<P, F> when(PredicateParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer) {
    getFlowData().addParent(WHEN, consumer);
    return new When(this);
  }

  default <T> When<P, F> when(LambdaMeta<T> lambdaMeta) {
    getFlowData().addParent(WHEN, lambdaMeta);
    return new When(this);
  }

}
