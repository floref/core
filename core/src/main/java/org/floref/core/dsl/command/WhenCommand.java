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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.dsl.command.group.ParentCommand;
import org.floref.core.dsl.flow.impex.FlowStep;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.reference.MethodReference;
import org.floref.core.flow.run.CommandContext;

import static org.floref.core.dsl.command.FlowCommandBuilders.WHEN;
import static org.floref.core.dsl.flow.impex.FlowImpex.addTypeAndRef;

/**
 * .to
 *
 * @author Cristian Donoiu
 */
public class WhenCommand extends ParentCommand {
  private static final Log LOG = LogFactory.getLog(WhenCommand.class);
  protected FlowCommand otherwise;
  protected MethodReferenceCommand conditionMethod;

  public WhenCommand(MethodReference methodReference) {
    this.conditionMethod = new MethodReferenceCommand(methodReference);
  }
  public WhenCommand(LambdaMeta lambdaMeta) {
    conditionMethod = new MethodReferenceCommand(lambdaMeta);
  }

  @Override
  public boolean isParent() {
    return true;
  }

  @Override
  public void run(CommandContext commandContext) throws Exception {
    // The condition will get the previous result which is OK, but also the first child will get it.
    CommandContext copyContext = commandContext.copy();
    conditionMethod.run(copyContext);
    Boolean result = (Boolean) copyContext.getResult();
    if (result == null) {
      throw new FlowDefinitionException(conditionMethod.getLambdaMeta().getActualMethodReferenceAsString()
          + " returned null instead of a boolean");
    }

    if (result) {
      runChildren(commandContext);  // Keep the previous arguments and provide it to the first child.
    } else {
      // Run otherwise if present.
      if (otherwise != null) {
        otherwise.run(commandContext);
      }
    }
  }

  @Override
  public String getKeyword() {
    return WHEN;
  }

  public String toString() {
    return getKeyword() + "()";
  }

  public FlowCommand getOtherwise() {
    return otherwise;
  }

  public void setOtherwise(FlowCommand otherwise) {
    this.otherwise = otherwise;
  }

  @Override
  public void definitionExport(FlowStep flowStep) {
    addTypeAndRef(flowStep, this, conditionMethod);
    super.definitionExport(flowStep);
  }
}
