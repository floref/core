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
import org.floref.core.dsl.flow.impex.FlowStep;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.reference.MethodReference;
import org.floref.core.flow.run.CommandContext;
import org.floref.core.flow.run.CommandRunner;
import org.floref.core.flow.run.FlowSession;

import java.util.Map;
import java.util.concurrent.Callable;

import static org.floref.core.dsl.command.FlowCommandBuilders.FORK;

/**
 * .to
 *
 * @author Cristian Donoiu
 */
public class ForkCommand extends MethodReferenceCommand {
  private static final Log LOG = LogFactory.getLog(MethodReferenceCommand.class);

  public ForkCommand(MethodReference toMethodReference) {
    super(toMethodReference);
  }

  public ForkCommand(LambdaMeta lambdaMeta) {
    super(lambdaMeta);
  }

  @Override
  public void run(CommandContext commandContext) throws Exception {
    Map session = FlowSession.get();
    CommandContext contextCopy = commandContext.copy();

    Callable callable = () -> {
      try {
        FlowSession.set(session);
        super.run(contextCopy);
        return contextCopy.getResult();
      } catch (Throwable e) {
        LOG.error(e.getMessage(), e);
        throw e;
      } finally {
        FlowSession.clearSession();
      }
    };
    CommandRunner.submit(callable);

  }

  @Override
  public String getKeyword() {
    return FORK;
  }

  @Override
  public void definitionExport(FlowStep flowStep) {
    flowStep.setType(getKeyword());
    super.definitionExport(flowStep);
  }
}
