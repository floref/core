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

package org.floref.core.dsl.flow.data;

import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.dsl.command.MethodReferenceCommand;
import org.floref.core.dsl.command.ToCommand;
import org.floref.core.dsl.command.group.GroupCommandUtil;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.reference.MethodReference;

/**
 * DSL utils.
 *
 * @author Cristian Donoiu
 */
public class FlowInstructionUtil {

  public static <I extends FlowCommand> I getInstructionCommand(FlowInstruction flowInstruction,
                                                                Class<I> flowCommandClass, String errorMessage) {
    FlowData flowData = flowInstruction.getFlowData();
    FlowCommand currentCommand = flowData.getCurrentCommand();
    if (!flowCommandClass.isAssignableFrom(currentCommand.getClass())) {
      throw new FlowDefinitionException(errorMessage);
    }
    return (I)currentCommand;
  }

  public static <I> I setAggregator(FlowInstruction flowCommand, MethodReference methodReference) {
    GroupCommandUtil groupCommandUtil = getInstructionCommand(flowCommand, GroupCommandUtil.class,
        "'.aggregator' must be used with a corresponding group command");
    groupCommandUtil.setAggregator(methodReference);
    return (I)flowCommand;
  }

  public static <I> I setTimeout(FlowInstruction flowCommand, long millis) {
    GroupCommandUtil groupCommandUtil = getInstructionCommand(flowCommand, GroupCommandUtil.class,
        "'.timeout' must be used with a corresponding group command");
    groupCommandUtil.setTimeout(millis);
    return (I)flowCommand;
  }

  public static <I> I stopOnException(FlowInstruction flowCommand) {
    GroupCommandUtil groupCommandUtil = getInstructionCommand(flowCommand, GroupCommandUtil.class,
        "'.stopOnException' must be used with a corresponding group command");
    groupCommandUtil.stopOnException();
    return (I)flowCommand;
  }

  public static <I> I setRevertBy(FlowInstruction flowCommand, MethodReference methodReference) {
    MethodReferenceCommand methodReferenceCommand = getInstructionCommand(flowCommand, ToCommand.class,
        "'.revertBy' currently supported only for '.to'");
    methodReferenceCommand.setRevertBy(methodReference);
    return (I)flowCommand;
  }

  public void copy(FlowInstruction src, FlowInstruction dest) {
    dest.setFlowData(src.getFlowData());
  }
}
