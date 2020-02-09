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

import mjson.Json;
import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.dsl.command.MethodReferenceCommand;
import org.floref.core.dsl.flow.Base;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.build.FlowInstanceData;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.registry.FlowRegistry;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import static org.floref.core.dsl.command.FlowCommandBuilders.FORK;
import static org.floref.core.dsl.command.FlowCommandBuilders.TO;

/**
 * JSON related utils.
 */
public class FlowImpex {


  public static <T> String exportFlow(Class<T> flowClass) {
    Objects.requireNonNull(flowClass, "Flow class is null");

    FlowPayload flowPayload = new FlowPayload();
    flowPayload.setFlows(FlowRegistry.getFlowInstanceData(flowClass).definitionExport());
    flowPayload.setAliases(Aliases.getAliasesForExport());
    return Json.make(flowPayload.getMap()).toString();
  }

  public static String exportAllFlows() {
    FlowPayload flowPayload = new FlowPayload();
    Iterator<FlowInstanceData> iterator = FlowRegistry.allFlowsIterator();
    while (iterator.hasNext()) {
      flowPayload.addFlows(iterator.next().definitionExport());
    }
    flowPayload.setAliases(Aliases.getAliasesForExport());
    return Json.make(flowPayload.getMap()).toString();
  }

  public static void addTypeAndRef(FlowStep flowStep, FlowCommand flowCommand,
                                   MethodReferenceCommand methodReferenceCommand) {
    flowStep.setType(flowCommand.getKeyword());
    if (methodReferenceCommand != null) {
      methodReferenceCommand.definitionExport(flowStep);
    }
  }


  private static void importFlow(FlowStep flowStep, Base base) {
    for (FlowStep step: flowStep.getChildren()) {
      LambdaMeta lambdaMeta = step.getRefLambdaMeta();
      switch (step.getType()) {
        case TO: {
          base.to(lambdaMeta);
          break;
        }
        case FORK: {
          base.fork(lambdaMeta);
          break;
        }
//        case WHEN: {
//          flowBase.when(lambdaMeta);
//          for (FlowStep child : step.getChildren()) {
//            importFlow(child, flowBase);
//          }
//          flowBase.end();
//          break;
//        }
//        case OTHERWISE: {
//          flowBase.otherwise();
//          for (FlowStep child : step.getChildren()) {
//            importFlow(child, flowBase);
//          }
//          flowBase.end();
//          break;
//        }
        default: {
          throw new FlowDefinitionException("Unsupported step type: " + step.getType());
        }
      }
    }
  }
  public static void importFlows(String json) {
    Map map = Json.read(json).asMap();
    FlowPayload payload = new FlowPayload(map);

    // Import/overwrite aliases first. Should make it transactional so that if part fails then no change in the system.
    for (Map.Entry<String, String> entry : payload.getAliases().entrySet()) {
      Aliases.add(entry.getKey(), entry.getValue());
    }
    // Import/overwrite flows.
    for (FlowStep flow : payload.getFlows()) {
      LambdaMeta from = flow.getRefLambdaMeta();
      Base base = Flows.from(from);
      importFlow(flow, base);
      Object flowInstance = base.build();
    }
  }
}
