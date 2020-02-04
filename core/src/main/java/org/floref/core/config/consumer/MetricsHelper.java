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
import org.floref.core.config.FlowConfiguration;

/**
 * Used internally in flowref to provide different metrics.
 *
 * @author Cristian Donoiu
 */
public class MetricsHelper {
  static final Log LOG = LogFactory.getLog(MetricsHelper.class);
  static DefaultMetricsConsumer defaultMetricsConsumer = new DefaultMetricsConsumer();
  static {
    FlowConfiguration.addMetricsConsumer(defaultMetricsConsumer);
  }

  interface MetricRunner {
    void run(MetricsConsumer metricsConsumer);
  }
  public static void runMetric(MetricRunner metricRunner) {
    for(MetricsConsumer metricsConsumer : FlowConfiguration.getMetricsConsumerList()) {
      try {
        metricRunner.run(metricsConsumer);
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
        throw e;
      }
    }
  }

  public static void beforeFlow(FlowMetrics flowMetrics) {
    runMetric((metricsConsumer) -> {
        metricsConsumer.beforeFlow(flowMetrics);
    });
  }

  public static void afterFlow(FlowMetrics flowMetrics) {
    runMetric((metricsConsumer) -> {
      metricsConsumer.afterFlow(flowMetrics);
    });
  }

  public static void beforeMethod(MethodRefMetrics methodRefMetrics) {
    runMetric((metricsConsumer) -> {
      metricsConsumer.beforeMethodReference(methodRefMetrics);
    });
  }

  public static void afterMethod(MethodRefMetrics methodRefMetrics) {
    runMetric((metricsConsumer) -> {
      metricsConsumer.afterMethodReference(methodRefMetrics);
    });
  }
}
