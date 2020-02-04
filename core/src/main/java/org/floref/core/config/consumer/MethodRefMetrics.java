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

import org.floref.core.config.consumer.FlowMetrics.Builder;

public class MethodRefMetrics {
  protected String methodReference;
  protected boolean isFlow;
  protected boolean success = true;
  protected Object result;
  protected Object[] params;
  protected Exception exception;

  /**
   * @return the flow reference (e.g. "org.example.FlowClass::method")
   */
  public String getMethodReference() {
    return methodReference;
  }

  public void setMethodReference(String methodReference) {
    this.methodReference = methodReference;
  }

  /**
   * @return the status of the action. If success is false the exception will probably be non null.
   */
  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  public Object[] getParams() {
    return params;
  }

  public void setParams(Object[] params) {
    this.params = params;
  }

  public Exception getException() {
    return exception;
  }

  public void setException(Exception exception) {
    this.exception = exception;
  }

  public boolean isFlow() {
    return isFlow;
  }

  public void setFlow(boolean flow) {
    isFlow = flow;
  }

  public static class Builder {
    MethodRefMetrics metrics = new MethodRefMetrics();
    public Builder(String reference) {
      metrics.setMethodReference(reference);
    }

    public MethodRefMetrics build() {
      return metrics;
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
    public Builder isFlow(boolean isFlow) {
      metrics.isFlow = isFlow;
      return this;
    }
  }
}
