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

import static org.floref.core.dsl.command.FlowCommandBuilders.FROM;
import static org.floref.core.dsl.flow.runtime.FlowImpex.addTypeAndRef;

import org.floref.core.dsl.command.group.ParentCommand;
import org.floref.core.dsl.flow.runtime.Aliases;
import org.floref.core.dsl.flow.runtime.FlowStep;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.reference.MethodReference;
import org.floref.core.flow.run.CommandContext;

/**
 * .from
 *
 * @author Cristian Donoiu
 */
public class FromCommand extends ParentCommand {

  protected MethodReferenceCommand methodReferenceCommand;

  public FromCommand(MethodReference methodReference) {
    this.methodReferenceCommand = new MethodReferenceCommand(FROM, methodReference);
  }
  public FromCommand(LambdaMeta lambdaMeta) {
    this.methodReferenceCommand = new MethodReferenceCommand(FROM, lambdaMeta);
  }

  @Override
  public boolean isParent() {
    return true;
  }

  @Override
  public String getKeyword() {
    return FROM;
  }

  @Override
  public String toString() {
    return methodReferenceCommand.toString();
  }

  @Override
  public void run(CommandContext commandContext) throws Exception {
     runChildren(commandContext);
  }

  @Override
  public void definitionExport(FlowStep flowStep) {
    addTypeAndRef(flowStep, this, methodReferenceCommand);
    super.definitionExport(flowStep);
  }

  @Override
  public void alias(String alias) {
    Aliases.add(alias, methodReferenceCommand);
  }
}
