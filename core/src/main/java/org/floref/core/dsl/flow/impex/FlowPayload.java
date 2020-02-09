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

package org.floref.core.dsl.flow.impex;

import org.floref.core.exception.FlowDefinitionException;

import java.util.*;

/**
 * Stores data from a flow export/import.
 */
public class FlowPayload {
  private static final String FLOWS = "flows";
  private static final String ALIASES = "aliases";

  List<FlowStep> flows = new ArrayList<>();
  Map<String, String> aliases = new LinkedHashMap<>();

  public FlowPayload() {
  }
  public FlowPayload(Map map) {
    init(map);
  }

  private void init(Map map) {
    // Flows
    Object flowsList = map.get(FLOWS);
    if (flowsList == null || !(flowsList instanceof List)) {
      throw new FlowDefinitionException("'" + FLOWS + "' must be a non empty list.");
    }
    for (Object flow : (List)flowsList) {
      if (flow == null || !(flow instanceof Map)) {
        throw new FlowDefinitionException("Flow must be a non empty map.");
      }
      flows.add(new FlowStep(flow));
    }
    // Aliases
    Object aliasesMap = map.get(ALIASES);
    if (aliasesMap != null) {
      if (!(aliasesMap instanceof Map)) {
        throw new FlowDefinitionException("'" + ALIASES + "' must be a non empty map.");
      }
      Set<Map.Entry> entries = ((Map) aliasesMap).entrySet();
      for (Map.Entry entry : entries) {
        Object alias = entry.getKey();
        if (alias == null || !(alias instanceof String)) {
          throw new FlowDefinitionException("Alias key must be a non empty string.");
        }
        Object aliasValue = entry.getValue();
        if (aliasValue == null || !(aliasValue instanceof String)) {
          throw new FlowDefinitionException("Alias value must be a non empty string.");
        }
        aliases.put((String)alias, (String)aliasValue);
      }
    }
  }

  public List<FlowStep> getFlows() {
    return flows;
  }

  public void setFlows(List<FlowStep> flows) {
    this.flows = flows;
  }

  public void addFlows(List<FlowStep> flowSteps) {
    flows.addAll(flowSteps);
  }

  public Map<String, String> getAliases() {
    return aliases;
  }

  public void setAliases(Map<String, String> aliases) {
    this.aliases = aliases;
  }

  public Map<String, ?> getMap() {
    Map map = new LinkedHashMap();
    List flowList = new ArrayList();
    for (FlowStep flowStep : flows) {
      flowList.add(flowStep.getMap());
    }
    map.put(FLOWS, flowList);
    map.put(ALIASES, aliases);
    return map;
  }
}
