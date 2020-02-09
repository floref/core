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

import org.floref.core.dsl.command.group.ParentCommand;
import org.floref.core.dsl.flow.IFork;
import org.floref.core.dsl.flow.To;
import org.floref.core.dsl.flow.data.FlowInstruction;
import org.floref.core.dsl.flow.data.FlowInstructionImpl;
import org.floref.core.dsl.flow.foreach.IForEach;
import org.floref.core.dsl.flow.parallel.IParallel;
import org.floref.core.dsl.flow.retry.IRetry;
import org.floref.core.dsl.flow.reversible.IReversible;
import org.floref.core.dsl.flow.reversible.Reversible;
import org.floref.core.exception.FlowDefinitionException;

/**
 * When entry point.
 *
 * @author Cristian Donoiu
 */
public class When<P, F> extends FlowInstructionImpl<F> implements
    To<When<P, F>>,
    IFork<When<P, F>>,
    IWhen<When<P, F>, F>,
    IOtherwise<P, F>,   // The end of the otherwise will be the parent of when.
    IParallel<When<P, F>, F>,
    IForEach<When<P, F>, F>,
    IReversible<When<P, F>, F>,
    IRetry<Reversible<P, F>, F> {

  protected FlowInstruction parent;

  public When(FlowInstruction src) {
    copyData(src);
    parent = src;
  }

  public P end() {
    // End the last group.
    ParentCommand grandParent = getFlowData().getCurrentParent().getParent();
    if (grandParent == null) {
      throw new FlowDefinitionException("'.end()' is missing a block command to be ended. Too many '.end()'.");
    }
    getFlowData().setCurrentParent(grandParent);

    getFlowData().setInstruction(parent);
    return (P) parent;
  }
}
