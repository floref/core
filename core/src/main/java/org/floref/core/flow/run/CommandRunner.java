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

import org.floref.core.config.FlowConfiguration;
import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.dsl.flow.data.FlowDefinition;

import java.util.concurrent.*;

import static org.floref.core.config.FlowConfiguration.commonThreadPoolMaxSize;

/**
 * Used to run a flow definition by running and evaluating all the flow definition commands.
 *
 * @author Cristian Donoiu
 */
public class CommandRunner {

  // For the moment one pool for all except
//  public static ExecutorService executorService = new ThreadPoolExecutor(0,
//      FlowConfiguration.getIntConfig(commonThreadPoolMaxSize),60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
  public static ExecutorService executorService = new ThreadPoolExecutor(FlowConfiguration.getIntConfig(commonThreadPoolMaxSize),
      Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), (Runnable r) -> {
    Thread t = new Thread(r);
    t.setDaemon(true); // So that it does not block the application from exiting.
    return t;
  });
  public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1, (Runnable r) -> {
    Thread t = new Thread(r);
    t.setDaemon(true);
    return t;
  });

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
