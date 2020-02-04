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

import org.floref.core.dsl.command.group.GroupCommandUtil;
import org.floref.core.dsl.flow.FlowDsl;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.reference.MethodReference;

public class CommandUtil {

  public static <FLOW_DSL> FLOW_DSL setAggregator(FlowDsl<FLOW_DSL> flowCommand, MethodReference methodReference) {
    FlowCommand currentCommand = flowCommand.getCurrentCommand();
    if (! (currentCommand instanceof GroupCommandUtil)) {
      throw new FlowDefinitionException("'.aggregator' must be used with a corresponding group command");
    }
    ((GroupCommandUtil)currentCommand).setAggregator(methodReference);
    return (FLOW_DSL)flowCommand;
  }

  public static <FLOW_DSL> FLOW_DSL setTimeout(FlowDsl<FLOW_DSL> flowCommand, long millis) {
    FlowCommand currentCommand = flowCommand.getCurrentCommand();
    if (! (currentCommand instanceof GroupCommandUtil)) {
      throw new FlowDefinitionException("'.timeout' must be used with a corresponding group command");
    }
    GroupCommandUtil groupUtilCommand = ((GroupCommandUtil)currentCommand);
    groupUtilCommand.setTimeout(millis);
    return (FLOW_DSL)flowCommand;
  }

  public static <FLOW_DSL> FLOW_DSL stopOnException(FlowDsl<FLOW_DSL> flowCommand) {
    FlowCommand currentCommand = flowCommand.getCurrentCommand();
    if (! (currentCommand instanceof GroupCommandUtil)) {
      throw new FlowDefinitionException("'.stopOnException' must be used with a corresponding group command");
    }
    GroupCommandUtil groupUtilCommand = ((GroupCommandUtil)currentCommand);
    groupUtilCommand.stopOnException();
    return (FLOW_DSL)flowCommand;
  }

  public static <FLOW_DSL> FLOW_DSL setRevertBy(FlowDsl<FLOW_DSL> flowCommand, MethodReference methodReference) {
    FlowCommand currentCommand = flowCommand.getCurrentCommand();
    if (! (currentCommand instanceof ToCommand ) && !(currentCommand instanceof ForkCommand )) {
      throw new FlowDefinitionException("'.revertBy' currently supported only for '.to' and '.fork'");
    }
    ((MethodReferenceCommand)currentCommand).setRevertBy(methodReference);
    return (FLOW_DSL)flowCommand;
  }
}
