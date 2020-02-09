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

import org.floref.core.dsl.flow.data.FlowDefinition;

/**
 * Keeps execution information related to a command from the flow.
 *
 * @author Cristian Donoiu
 */
public class CommandContext {

  protected FlowDefinition flowDefinition;
  protected Object result;         // the current result
  protected Object[] arguments;    // the arguments to be used
  protected Exception exception;

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  public Object[] getArguments() {
    return arguments;
  }

  public void setArguments(Object[] arguments) {
    this.arguments = arguments;
  }

  public FlowDefinition getFlowDefinition() {
    return flowDefinition;
  }

  public void setFlowDefinition(FlowDefinition flowDefinition) {
    this.flowDefinition = flowDefinition;
  }

  public void moveResultToArguments() {
    arguments = new Object[]{result};
  }

  public Exception getException() {
    return exception;
  }

  public void setException(Exception exception) {
    this.exception = exception;
  }

  public CommandContext copy() {
    CommandContext commandContext = new CommandContext();
    commandContext.setArguments(arguments);
    commandContext.setResult(result);
    commandContext.setFlowDefinition(flowDefinition);
    return commandContext;
  }
}
