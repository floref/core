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

package org.floref.core.dsl.flow;

import org.floref.core.dsl.command.group.ParentCommand;
import org.floref.core.dsl.flow.data.FlowInstruction;
import org.floref.core.dsl.flow.data.FlowInstructionImpl;
import org.floref.core.dsl.flow.foreach.IForEach;
import org.floref.core.dsl.flow.parallel.IParallel;
import org.floref.core.dsl.flow.retry.IRetry;
import org.floref.core.dsl.flow.reversible.IReversible;
import org.floref.core.dsl.flow.user.UserCustomSimpleInstructions;
import org.floref.core.dsl.flow.when.IWhen;
import org.floref.core.exception.FlowDefinitionException;

/**
 * Workflow base class allowing the call of any other flow commands.
 *
 * <i>Floref's context constrained API</i>
 * The flow definition is context constrained depending on the current instruction. This is a unique feature of Floref
 * that makes sure the flows are valid early on at compile time.
 * Generics is used extensively in order to assure flow validity from compile time. For example you will not be able to
 * put two '.end()' in a simple flow because the first end() will refer the From while the second is not possible since
 * there is no more .end() within From. Another example is the fact that group command attributes like aggregator,
 * revertBy, etc ae only visible only after the parent/group command is used. This also help in keeping the API small
 * at the high level and diverge/grow as more commands are used and shrink when end-ing those.
 *
 * @author Cristian Donoiu
 */
public class BaseInstructionImpl<P, F> extends FlowInstructionImpl<F> implements
    To<BaseInstructionImpl<P, F>>,
    IFork<BaseInstructionImpl<P, F>>,
    IWhen<BaseInstructionImpl<P, F>, F>,
    IParallel<BaseInstructionImpl<P, F>, F>,
    IForEach<BaseInstructionImpl<P, F>, F>,
    IReversible<BaseInstructionImpl<P, F>, F>,
    IRetry<BaseInstructionImpl<P, F>, F>,
    UserCustomSimpleInstructions<BaseInstructionImpl<P, F>> {

  public BaseInstructionImpl() {
  }

  public BaseInstructionImpl(FlowInstruction instruction) {
    copyData(instruction);
  }

  public P end() {
    // End the last group.
    ParentCommand grandParent = getFlowData().getCurrentParent().getParent();
    if (grandParent == null) {
      throw new FlowDefinitionException("'.end()' is missing a block command to be ended. Too many '.end()'.");
    }
    getFlowData().setCurrentParent(grandParent);

    getFlowData().setInstruction(this);
    return (P) this;
  }

}
