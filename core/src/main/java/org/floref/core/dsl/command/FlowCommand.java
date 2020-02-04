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

package org.floref.core.dsl.command;

import java.util.Map;
import org.floref.core.dsl.command.group.ParentCommand;
import org.floref.core.dsl.flow.runtime.FlowStep;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.build.FlowObjectMethods.ToStringIndexes;
import org.floref.core.flow.run.CommandContext;

/**
 * Base interface for all flow commands.
 * Multiple flow commands can be top level. Some commands can include others and thus some can have parents.
 *
 * @author Cristian Donoiu
 */
public interface FlowCommand {

  default String getId() {
    return getKeyword();
  };
  /**
   * @return true if the command is a grouping command.
   */
  default boolean isParent() {  ///////////////////////////// AICI DE SCOS SI TESTAT CU INSTANCE OF GROUPCOMMAND
    return false;
  }
  /**
   * @return the currentParent command that groups/contains this command.
   */
  default ParentCommand getParent() {
    return null;
  }

  void setParent(ParentCommand parentCommand);

  default void run(CommandContext commandContext) throws Exception {
    throw new FlowDefinitionException("command not implemented");
  }

  default String getKeyword() {
    return getClass().getSimpleName();
  };

  default void definitionExport(FlowStep flowStep) {
  }

  default void definitionImport(FlowStep flowStep) {

  }

  default void alias(String alias) {
  }
}
