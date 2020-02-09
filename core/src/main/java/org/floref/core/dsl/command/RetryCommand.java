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

import static org.floref.core.dsl.command.FlowCommandBuilders.RETRY;

/**
 * .retry
 *
 * @author Cristian Donoiu
 */
public class RetryCommand extends MethodReferenceCommand {
  private static final Log LOG = LogFactory.getLog(RetryCommand.class);
  protected long delay = 1000;
  protected float multiplier = 1;
  protected long times = 2;
  protected Class<? extends Exception> retryOnExceptionClass;

  public RetryCommand(MethodReference toMethodReference) {
    super(toMethodReference);
  }
  public RetryCommand(LambdaMeta lambdaMeta) {
    super(lambdaMeta);
  }

  public long getDelay() {
    return delay;
  }

  public void setDelay(long delay) {
    this.delay = delay;
  }

  public float getMultiplier() {
    return multiplier;
  }

  public void setMultiplier(float multiplier) {
    this.multiplier = multiplier;
  }

  public long getTimes() {
    return times;
  }

  public void setTimes(long times) {
    this.times = times;
  }

  public Class<? extends Exception> getRetryOnExceptionClass() {
    return retryOnExceptionClass;
  }

  public void setRetryOnExceptionClass(Class<? extends Exception> retryOnExceptionClass) {
    this.retryOnExceptionClass = retryOnExceptionClass;
  }

  @Override
  public void run(CommandContext commandContext) throws Exception {
    // Keep the caller thread waiting since we are optimistic.
    CommandContext commandContextCopy = commandContext.copy();

    long initialDelay = delay;
    int totalTimes = 0;
    // Repeat forever until an exception.
    while (true) {
      if (++totalTimes > times) {
        break;
      }
      try {
        commandContextCopy = commandContext.copy();
        super.run(commandContextCopy);
        break;
      } catch (Exception exception) {
        LOG.warn(exception.getMessage(), exception); // Should be logged as warning?.
        if (retryOnExceptionClass != null && !retryOnExceptionClass.isAssignableFrom(exception.getClass())) {
          break;
        }
      }
      Thread.sleep(initialDelay);
      initialDelay += multiplier * initialDelay;
    }

    commandContext.setResult(commandContextCopy.getResult());
    commandContext.setArguments(commandContextCopy.getArguments());
  }

  @Override
  public String getKeyword() {
    return RETRY;
  }

  @Override
  public void definitionExport(FlowStep flowStep) {
    flowStep.setType(getKeyword());
    super.definitionExport(flowStep);
  }

}
