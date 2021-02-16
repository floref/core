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

package org.floref.core.dsl.flow.retry;


import org.floref.core.dsl.command.RetryCommand;
import org.floref.core.dsl.flow.BaseInstructionImpl;
import org.floref.core.dsl.flow.data.FlowInstruction;
import org.floref.core.flow.reference.MethodReference;

import static org.floref.core.dsl.command.FlowCommandBuilders.RETRY;
import static org.floref.core.dsl.flow.data.FlowInstructionUtil.getInstructionCommand;

/**
 * .retry utils like delay, times.
 *
 * @author Cristian Donoiu
 */
public class Retry<P, F> extends BaseInstructionImpl<P, F> {

  //  protected FlowInstruction parent;
  public Retry(FlowInstruction src, MethodReference consumer) {
    copyData(src);
    getFlowData().addChild(RETRY, consumer);
  }

  public Retry<P, F> delay(long millis) {
    return this.delay(millis, 1);
  }

  /**
   * Exponential delay.
   */
  public Retry<P, F> delay(long initialValue, float multiplier) {
    RetryCommand retryCommand = getInstructionCommand(this, RetryCommand.class,
        "'.delay' currently supported only for '.retry'");
    retryCommand.setDelay(initialValue);
    retryCommand.setMultiplier(multiplier);
    return this;
  }

  /**
   * How many times to retry the referred method(flow or bean method).
   */
  public Retry<P, F> times(long times) {
    RetryCommand retryCommand = getInstructionCommand(this, RetryCommand.class,
        "'.times' currently supported only for '.retry'");
    retryCommand.setTimes(times);
    return this;
  }

  /**
   * Only when specified exceptions happen the retry is performed.
   * If not specified, then any exception will break a retry. This is better then retrying unrecoverable errors.
   */
  public Retry<P, F> on(Class<? extends Exception> exceptionClass) {
    RetryCommand retryCommand = getInstructionCommand(this, RetryCommand.class,
        "'.times' currently supported only for '.retry'");
    retryCommand.setRetryOnExceptionClass(exceptionClass);
    return this;
  }
}
