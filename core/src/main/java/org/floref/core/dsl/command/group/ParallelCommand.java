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

package org.floref.core.dsl.command.group;

import static org.floref.core.dsl.command.FlowCommandBuilders.PARALLEL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.flow.run.CommandContext;

/**
 * .parallel
 *
 * @author Cristian Donoiu
 */
public class ParallelCommand extends GroupCommandUtil {
  private static final Log LOG = LogFactory.getLog(ParallelCommand.class);

  @Override
  public void run(CommandContext commandContext) throws Exception {
    List<CommandCallable> callables = new ArrayList();
    for (final FlowCommand command : children) {
      callables.add(new CommandCallable(command.getId(), () -> {
        CommandContext commandContextCopy = commandContext.copy();
        command.run(commandContextCopy);

        return commandContextCopy;
      }));
    }
    run(commandContext, callables);

  }

  @Override
  public String getKeyword() {
    return PARALLEL;
  }

  @Override
  public String toString() {
    return getKeyword() + "()";
  }

}
