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

import org.floref.core.dsl.flow.IFork;
import org.floref.core.dsl.flow.To;
import org.floref.core.dsl.flow.data.FlowInstruction;
import org.floref.core.dsl.flow.data.FlowInstructionImpl;
import org.floref.core.dsl.flow.foreach.IForEach;
import org.floref.core.dsl.flow.parallel.IParallel;
import org.floref.core.dsl.flow.retry.IRetry;
import org.floref.core.dsl.flow.reversible.Compensable;
import org.floref.core.dsl.flow.reversible.ICompensable;

/**
 * Otherwise entry point.
 * @author Cristian Donoiu
 */
public class Otherwise<P, F> extends FlowInstructionImpl<F> implements
    To<Otherwise<P, F>>,
    IFork<Otherwise<P, F>>,
    IWhen<Otherwise<P, F>, F>,   // No .otherwise after an otherwise, there must be an .end first.
    IParallel<Otherwise<P, F>, F>,
    IForEach<Otherwise<P, F>, F>,
    ICompensable<Otherwise<P, F>, F>,
    IRetry<Compensable<P, F>, F> {

  protected FlowInstruction parent;

  public Otherwise(FlowInstruction then) {
    copyData(then);
    parent = then;
  }

  public P end() {
    return (P)((When)parent).end(); // Delegate to .when.
  }
}
