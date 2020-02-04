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

package org.floref.core.dsl.flow.runtime;

import org.floref.core.dsl.flow.FlowDefinition;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.reference.LambdaMetaBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to model any flow step. Thus a flow definition is just a FlowStep that contains other FlowStep's.
 * Since lightway "Mjson" is used and it does not have types, let's do a wrapper.
 *
 * @author Cristian Donoiu
 */
public class FlowStep {
  private Map map;

  public FlowStep() {
    map = new LinkedHashMap();
  }
  public FlowStep(Object map) {
    if (map == null || !(map instanceof Map)) {
      throw new FlowDefinitionException("Elements inside 'steps' must be non null JSON objects");
    }
    this.map = (Map)map;
  }

  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }

  private <T> T get(String key, Class<T> clazz) {
    T value = (T)map.get(key);
    if (value != null && !(clazz.isAssignableFrom(value.getClass()))) {
      throw new FlowDefinitionException("For key " + key + " expecting a " + clazz.getSimpleName() + " but found a " + value.getClass().getSimpleName());
    }
    return value;
  }

  private <T> void set(String key, T value) {
    map.put(key, value);
  }

  public String getType() {
    return get("type", String.class);
  }

  public void setType(String type) {
    set("type", type);
  }

  public String getRef() {
    return get("ref", String.class);
  }
  public LambdaMeta getRefLambdaMeta() {
    String ref = getRef();
    LambdaMeta lambdaMeta = null;
    if (ref != null) {
      if (ref.contains("::")) {
        lambdaMeta = LambdaMetaBuilder.getLambdaMeta(ref);
      } else {
        // It is an alias.
        lambdaMeta = Aliases.getAliasLambdaMeta(ref);
      }
    }
    return lambdaMeta;
  }

  public void setRef(String ref) {
    set("ref", ref);
  }

  public String getAggregator() {
    return get("aggregator", String.class);
  }

  public void setAggregator(String ref) {
    set("aggregator", ref);
  }

  public Long getTimeout() {
    return get("timeout", Long.class);
  }

  public void setTimeout(long timeout) {
    set("timeout", timeout);
  }
  public Boolean getStopOnException() {
    return get("stopOnException", Boolean.class);
  }

  public void setStopOnException(boolean stopOnException) {
    set("stopOnException", stopOnException);
  }

  public List<FlowStep> getChildren() {
    List<Map> steps = get("steps", List.class);
    List instructionList = new ArrayList();
    if (steps == null) {
      return instructionList;
    } else {
      for (Object obj : steps) {
        instructionList.add(new FlowStep(obj));
      }
    }
    return instructionList;
  }

  public void setChildren(List<FlowStep> steps) {
    List stepList = get("steps", List.class);
    if (stepList == null) {
      stepList = new ArrayList();
      map.put("steps", stepList);
    }
    for (FlowStep step : steps) {
      stepList.add(step.map);
    }
  }
  public void addStep(FlowStep flowStep) {
    List stepList = get("steps", List.class);
    if (stepList == null) {
      stepList = new ArrayList();
      map.put("steps", stepList);
    }
    stepList.add(flowStep.map);
  }
}