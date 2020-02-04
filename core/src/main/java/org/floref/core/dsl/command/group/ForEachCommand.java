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

import static org.floref.core.dsl.command.FlowCommandBuilders.FOR_EACH;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.flow.build.FlowObjectMethods.ToStringIndexes;
import org.floref.core.flow.run.CommandContext;

/**
 * .forEach
 *
 * @author Cristian Donoiu
 */
public class ForEachCommand extends GroupCommandUtil {
  private static final Log LOG = LogFactory.getLog(ForEachCommand.class);

  @Override
  public void run(CommandContext commandContext) throws Exception {

    Collection toBeSplit = (Collection)commandContext.getArguments()[0];

    List<CommandCallable> callables = new ArrayList();
    long iteration = 0;
    for (Object object : toBeSplit) {
      iteration++;
      callables.add(new CommandCallable("iteration " + iteration, () -> {
        CommandContext elemContext = new CommandContext();
        elemContext.setArguments(new Object[]{object});

        runChildren(elemContext);
        return elemContext;
      }));
    }
    run(commandContext, callables);

  }

  @Override
  public String getKeyword() {
    return FOR_EACH;
  }

  @Override
  public String toString() {
    return getKeyword() + "()";
  }

}
