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

package org.floref.core.config.consumer;

/**
 * Used to inject a metrics injector. Can obtain information like: flow start, flow end, non flow method calls together
 * with contextual information (e.g. currentParent flow).
 *
 * @author Cristian Donoiu
 */
public interface MetricsConsumer {

  /**
   * Called before a flow is run.
   *
   * @param metrics
   */
  void beforeFlow(FlowMetrics metrics);

  /**
   * Called at the end of a flow execution.
   */
  void afterFlow(FlowMetrics metrics);

  /**
   * Called before any method reference is executed.
   */
  void beforeMethodReference(MethodRefMetrics metrics);

  /**
   * Called after any method reference is executed.
   */
  void afterMethodReference(MethodRefMetrics metrics);
}
