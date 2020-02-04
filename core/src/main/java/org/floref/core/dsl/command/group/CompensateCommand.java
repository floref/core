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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.dsl.command.MethodReferenceCommand;
import org.floref.core.flow.run.CommandContext;

/**
 * .compensate command
 *
 * @author Cristian Donoiu
 */
public class CompensateCommand extends GroupCommandUtil {
  private static final Log LOG = LogFactory.getLog(CompensateCommand.class);

  @Override
  public void run(CommandContext commandContext) throws Exception {
    List<MethodReferenceCommand> revertCommandList = new ArrayList<>();
    List<CommandContext> revertContextList = new ArrayList<>();

    try {
      for (final FlowCommand command : children) {
        MethodReferenceCommand methodReferenceCommand = (MethodReferenceCommand) command;
        command.run(commandContext);

        // Record reverter only if run was OK.
        if (methodReferenceCommand.getRevertBy() != null) {
          revertCommandList.add(0, methodReferenceCommand.getRevertBy());
          revertContextList.add(0, commandContext.copy());
        }
      }
    } catch (Exception e) {
      for (int i = 0; i < revertCommandList.size(); i++) {
        revertCommandList.get(i).run(revertContextList.get(i));
      }
      throw e;
    }


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
