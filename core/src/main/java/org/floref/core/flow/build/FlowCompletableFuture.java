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
import java.util.concurrent.CompletableFuture;

import static org.floref.core.flow.run.FlowUtil.CANCELLED;

/**
 * Future wrapper that can be used to interrupt a flow execution.
 *
 * @author Cristian Donoiu
 */
public class FlowCompletableFuture extends CompletableFuture {
  Map session;

  FlowCompletableFuture(Map session) {
    this.session = session;
  }

  @Override
  public boolean cancel(boolean mayInterruptIfRunning) {
    // Since the future.cancel may be called from another thread.
    if (session != null) {
      session.put(CANCELLED, true);
    }
    return cancel(mayInterruptIfRunning);
  }

  @Override
  public boolean isCancelled() {
    return (session != null && Boolean.TRUE.equals(session.get(CANCELLED)))
        || isCancelled();
  }

}
