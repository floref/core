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

package org.floref.core.flow.build;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.floref.core.flow.run.FlowUtil.CANCELLED;

/**
 * Future wrapper that can be used to interrupt a flow execution.
 *
 * @author Cristian Donoiu
 */
public class FlowFuture implements Future {
  Future future;
  Map session;

  FlowFuture(Future future, Map session) {
    this.future = future;
    this.session = session;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    // Since the future.cancel may be called from another thread.
    if (session != null) {
      session.put(CANCELLED, true);
    }
    return future.cancel(mayInterruptIfRunning);
  }

  @Override
  public boolean isCancelled() {
    return (session != null && Boolean.TRUE.equals(session.get(CANCELLED)))
        || future.isCancelled();
  }

  @Override
  public boolean isDone() {
    return future.isDone();
  }

  @Override
  public Object get() throws InterruptedException, ExecutionException {
    return future.get();
  }

  @Override
  public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
    return future.get(timeout, unit);
  }
}
