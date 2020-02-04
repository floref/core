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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Default metrics consumer for internal purposes.
 *
 * @author Cristian Donoiu
 */
public class DefaultMetricsConsumer implements MetricsConsumer {
  static final Log LOG = LogFactory.getLog(DefaultMetricsConsumer.class);

  @Override
  public void beforeFlow(FlowMetrics metrics) {
    LOG.debug("Flow start " + metrics.getMethodReference());
  }

  @Override
  public void afterFlow(FlowMetrics metrics) {
    LOG.debug("Flow end " + metrics.getMethodReference());
  }

  @Override
  public void beforeMethodReference(MethodRefMetrics metrics) {
    LOG.debug("Method start " + metrics.getMethodReference());
  }

  @Override
  public void afterMethodReference(MethodRefMetrics metrics) {
    LOG.debug("Method end " + metrics.getMethodReference());
  }

}
