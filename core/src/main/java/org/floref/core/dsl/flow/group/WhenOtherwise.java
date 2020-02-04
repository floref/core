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

import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.dsl.command.WhenCommand;
import org.floref.core.dsl.command.group.ParentCommand;
import org.floref.core.dsl.flow.FlowDsl;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.reference.ParamSupplier;
import org.floref.core.flow.reference.predicate.PredicateParamBiConsumer;
import org.floref.core.flow.reference.predicate.PredicateParamConsumer;
import org.floref.core.flow.reference.predicate.PredicateParamHeptaConsumer;
import org.floref.core.flow.reference.predicate.PredicateParamHexaConsumer;
import org.floref.core.flow.reference.predicate.PredicateParamPentaConsumer;
import org.floref.core.flow.reference.predicate.PredicateParamTetraConsumer;
import org.floref.core.flow.reference.predicate.PredicateParamTriConsumer;

import static org.floref.core.dsl.command.FlowCommandBuilders.*;

/**
 * Contains when and otherwise commands.
 *
 * @author Cristian Donoiu
 */
public interface WhenOtherwise<FLOW_DSL> extends FlowDsl<FLOW_DSL> {

  default FLOW_DSL when(ParamSupplier<Boolean> consumer) {
    return addParent(WHEN, consumer);
  }

  default <T> FLOW_DSL when(PredicateParamConsumer<T> consumer) {
    return addParent(WHEN, consumer);
  }

  default <T, U> FLOW_DSL when(PredicateParamBiConsumer<T, U> consumer) {
    return addParent(WHEN, consumer);
  }

  default <T, U, V> FLOW_DSL when(PredicateParamTriConsumer<T, U, V> consumer) {
    return addParent(WHEN, consumer);
  }

  default <T, U, V, X> FLOW_DSL when(PredicateParamTetraConsumer<T, U, V, X> consumer) {
    return addParent(WHEN, consumer);
  }

  default <T, U, V, X, Y> FLOW_DSL when(PredicateParamPentaConsumer<T, U, V, X, Y> consumer) {
    return addParent(WHEN, consumer);
  }

  default <T, U, V, X, Y, Z> FLOW_DSL when(PredicateParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    return addParent(WHEN, consumer);
  }

  default <T, U, V, X, Y, Z, A> FLOW_DSL when(PredicateParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer){
    return addParent(WHEN, consumer);
  }

  default <T> FLOW_DSL when(LambdaMeta<T> lambdaMeta) {
    return addParent(TO, lambdaMeta);
  }

  default FLOW_DSL otherwise() {
    FlowCommand parent = getCurrentParent();
    if (!(parent instanceof WhenCommand)) {
      throw new FlowDefinitionException(".otherwise() has no corresponding 'when()' or the 'when()' was ended too early");
    }

    ParentCommand otherwiseCommand = (ParentCommand)buildCommand(OTHERWISE);
    WhenCommand whenCommand = (WhenCommand)parent;
    whenCommand.setOtherwise(otherwiseCommand);

    // The parent is the parent of 'then' but the 'otherwise' will not be a child so as to not get executed
    // independently of the then.
    otherwiseCommand.setParent(whenCommand.getParent());
    setCurrentParent(otherwiseCommand);

    return (FLOW_DSL) this;
  }
}
