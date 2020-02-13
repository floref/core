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

import static org.floref.core.dsl.command.FlowCommandBuilders.COMPENSABLE;
import static org.floref.core.dsl.flow.data.FlowInstructionUtil.setReversion;

/**
 * Workflow base class allowing the call of any other flow commands.
 * Future improvement will be to restrict the returned object as a composition of the many different interfaces so as to
 * restrict what the user can call at any moment within the definition and prevent invalid definitions.
 *
 * @author Cristian Donoiu
 */
//public class Reversible<P, F> extends Base<P, F> {
public class Compensable<P, F> extends FlowInstructionImpl<F> implements
    To<Compensable<P, F>>,
    IFork<Compensable<P, F>>,
    IWhen<Compensable<P, F>, F>,
    IParallel<Compensable<P, F>, F>,
    IForEach<Compensable<P, F>, F>,
    ICompensable<Compensable<P, F>, F>,
    IRetry<Compensable<P, F>, F> {

  protected FlowInstruction parent;
  
  public Compensable(FlowInstruction src) {
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

  public Compensable<P, F> reversion(ParamVoidConsumerVoidSupplier consumer) {
    setReversion(this, consumer);
    return this;
  }

  public <T> Compensable<P, F> reversion(ParamSupplier<T> consumer) {
    setReversion(this, consumer);
    return this;
  }

  public <T> Compensable<P, F> reversion(ParamConsumer<T> consumer) {
    setReversion(this, consumer);
    return this;
  }

  public <T, U> Compensable<P, F> reversion(ParamBiConsumer<T, U> consumer) {
    setReversion(this, consumer);
    return this;
  }

  public <T, U, V> Compensable<P, F> reversion(ParamTriConsumer<T, U, V> consumer) {
    setReversion(this, consumer);
    return this;
  }

  public <T, U, V, X> Compensable<P, F> reversion(ParamTetraConsumer<T, U, V, X> consumer) {
    setReversion(this, consumer);
    return this;
  }

  public <T, U, V, X, Y> Compensable<P, F> reversion(ParamPentaConsumer<T, U, V, X, Y> consumer) {
    setReversion(this, consumer);
    return this;
  }

  public <T, U, V, X, Y, Z> Compensable<P, F> reversion(ParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    getFlowData().addChild(COMPENSABLE, consumer);
    return this;
  }

  public <T, U, V, X, Y, Z, A> Compensable<P, F> reversion(ParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer) {
    getFlowData().addChild(COMPENSABLE, consumer);
    return this;
  }
  
}
