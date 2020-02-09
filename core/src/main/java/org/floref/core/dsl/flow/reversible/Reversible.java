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

package org.floref.core.dsl.flow.reversible;


import org.floref.core.dsl.command.group.ParentCommand;
import org.floref.core.dsl.flow.IFork;
import org.floref.core.dsl.flow.To;
import org.floref.core.dsl.flow.data.FlowInstruction;
import org.floref.core.dsl.flow.data.FlowInstructionImpl;
import org.floref.core.dsl.flow.foreach.IForEach;
import org.floref.core.dsl.flow.parallel.IParallel;
import org.floref.core.dsl.flow.retry.IRetry;
import org.floref.core.dsl.flow.when.IWhen;
import org.floref.core.flow.reference.*;

import static org.floref.core.dsl.command.FlowCommandBuilders.REVERSIBLE;
import static org.floref.core.dsl.flow.data.FlowInstructionUtil.setRevertBy;

/**
 * Workflow base class allowing the call of any other flow commands.
 * Future improvement will be to restrict the returned object as a composition of the many different interfaces so as to
 * restrict what the user can call at any moment within the definition and prevent invalid definitions.
 *
 * @author Cristian Donoiu
 */
//public class Reversible<P, F> extends Base<P, F> {
public class Reversible<P, F> extends FlowInstructionImpl<F> implements
    To<Reversible<P, F>>,
    IFork<Reversible<P, F>>,
    IWhen<Reversible<P, F>, F>,
    IParallel<Reversible<P, F>, F>,
    IForEach<Reversible<P, F>, F>,
    IReversible<Reversible<P, F>, F>,
    IRetry<Reversible<P, F>, F> {

  protected FlowInstruction parent;
  
  public Reversible(FlowInstruction src) {
    copyData(src);
    parent = src;
  }

  public P end() {
    // End the last group.
    ParentCommand grandParent = getFlowData().getCurrentParent().getParent();
    getFlowData().setCurrentParent(grandParent);

    getFlowData().setInstruction(parent);
    return (P)parent;
  }

  public Reversible<P, F> revertBy(ParamVoidConsumerVoidSupplier consumer) {
    setRevertBy(this, consumer);
    return this;
  }

  public <T> Reversible<P, F> revertBy(ParamSupplier<T> consumer) {
    setRevertBy(this, consumer);
    return this;
  }

  public <T> Reversible<P, F> revertBy(ParamConsumer<T> consumer) {
    setRevertBy(this, consumer);
    return this;
  }

  public <T, U> Reversible<P, F> revertBy(ParamBiConsumer<T, U> consumer) {
    setRevertBy(this, consumer);
    return this;
  }

  public <T, U, V> Reversible<P, F> revertBy(ParamTriConsumer<T, U, V> consumer) {
    setRevertBy(this, consumer);
    return this;
  }

  public <T, U, V, X> Reversible<P, F> revertBy(ParamTetraConsumer<T, U, V, X> consumer) {
    setRevertBy(this, consumer);
    return this;
  }

  public <T, U, V, X, Y> Reversible<P, F> revertBy(ParamPentaConsumer<T, U, V, X, Y> consumer) {
    setRevertBy(this, consumer);
    return this;
  }

  public <T, U, V, X, Y, Z> Reversible<P, F> revertBy(ParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    getFlowData().addChild(REVERSIBLE, consumer);
    return this;
  }

  public <T, U, V, X, Y, Z, A> Reversible<P, F> revertBy(ParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer) {
    getFlowData().addChild(REVERSIBLE, consumer);
    return this;
  }
  
}
