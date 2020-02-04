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

/**
 * Flow run metrics aggregates different run data.
 * @author Cristian Donoiu
 */
package org.floref.core.config.consumer;

public class FlowMetrics extends MethodRefMetrics {
  protected boolean isOriginator;

  /**
   * @return true if the flow was called directly and not from within another flow.
   */
  public boolean isOriginator() {
    return isOriginator;
  }

  public void setOriginator(boolean originator) {
    isOriginator = originator;
  }

  public static class Builder {
    FlowMetrics metrics = new FlowMetrics();
    public Builder(String reference) {
      metrics.setMethodReference(reference);
    }

    public FlowMetrics build() {
      return metrics;
    }

    public Builder originator(boolean originator) {
      metrics.isOriginator = originator;
      return this;
    }
    public Builder success(boolean success) {
      metrics.success = success;
      return this;
    }
    public Builder result(Object result) {
      metrics.result = result;
      return this;
    }
    public Builder params(Object[] params) {
      metrics.params = params;
      return this;
    }
    public Builder exception(Exception exception) {
      metrics.exception = exception;
      if (exception != null) {
        metrics.success = false;
      }
      return this;
    }
  }
}
