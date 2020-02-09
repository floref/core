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

import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.dsl.command.FlowCommandBuilders;
import org.floref.core.dsl.command.WhenCommand;
import org.floref.core.dsl.command.group.ParentCommand;
import org.floref.core.dsl.flow.data.FlowInstruction;
import org.floref.core.exception.FlowDefinitionException;

import static org.floref.core.dsl.command.FlowCommandBuilders.OTHERWISE;

/**
 * Contains when and otherwise commands.
 *
 * @author Cristian Donoiu
 */
public interface IOtherwise<P, F> extends FlowInstruction {

  default Otherwise<P, F> otherwise() {

    FlowCommand parent = getFlowData().getCurrentParent();
    if (!(parent instanceof WhenCommand)) {
      throw new FlowDefinitionException(".otherwise() has no corresponding 'when()' or the 'when()' was ended too early");
    }

    ParentCommand otherwiseCommand = (ParentCommand) FlowCommandBuilders.build(OTHERWISE);
    WhenCommand whenCommand = (WhenCommand)parent;
    whenCommand.setOtherwise(otherwiseCommand);

    // The parent is the parent of 'then' but the 'otherwise' will not be a child so as to not get executed
    // independently of the then.
    otherwiseCommand.setParent(whenCommand.getParent());
    getFlowData().setCurrentParent(otherwiseCommand);

    return new Otherwise(this);
  }
}
