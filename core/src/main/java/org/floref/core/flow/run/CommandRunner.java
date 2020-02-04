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

package org.floref.core.flow.run;

import static org.floref.core.config.FlowConfiguration.commonThreadPoolMaxSize;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.floref.core.config.FlowConfiguration;
import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.dsl.flow.FlowDefinition;

/**
 * Used to run a flow definition by running and evaluating all the flow definition commands.
 *
 * @author Cristian Donoiu
 */
public class CommandRunner {

  // For the moment one pool for all except
  public static ExecutorService executorService = new ThreadPoolExecutor(0,
      FlowConfiguration.getIntConfig(commonThreadPoolMaxSize),60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
  public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

  public static ExecutorService getExecutorService() {
    return executorService;
  }

  public static <T> Future<T> submit(Runnable runnable) {
    return (Future<T>) getExecutorService().submit(runnable);
  }
  public static <T> Future<T> submit(Callable callable) {
    return (Future<T>) getExecutorService().submit(callable);
  }

  public static ScheduledFuture scheduleFuture(Runnable runnable, long timeout) {
    ScheduledFuture scheduledFuture = scheduledExecutorService.schedule(runnable, timeout, TimeUnit.MILLISECONDS);
    return scheduledFuture;
  }

  public static Object run(FlowDefinition flowDefinition, Object[] args) throws Exception {
    FlowCommand flowCommand = flowDefinition.getStartCommand();
    CommandContext commandContext = new CommandContext();
    commandContext.setArguments(args);
    commandContext.setFlowDefinition(flowDefinition);
    flowCommand.run(commandContext);

    return commandContext.getResult();
  }
}
