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

package org.floref.core.flow.registry;

import static org.junit.Assert.assertEquals;

import org.floref.core.dsl.TestFlows;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.flow.build.FlowInstanceData;
import org.floref.core.flow.run.FlowUtil;
import org.junit.Before;
import org.junit.Test;

public class FlowRegistryTest {

  @Before
  public void before() {
    FlowUtil.clearAllFlowData();
  }

  @Test
  public void test() {
    TestFlows testFlows = Flows.from(TestFlows::length)
        .to(String::length)
        .build();

    testFlows.length("a");

    assertEquals(testFlows, Flows.get(TestFlows.class));
    assertEquals(FlowRegistry.getFlowInstanceData(testFlows), FlowRegistry.getFlowInstanceData(testFlows));
    FlowInstanceData flowInstanceData = FlowRegistry.getFlowInstanceData(testFlows);
    assertEquals(testFlows, FlowRegistry.getFlow(flowInstanceData.getFlowDefinitions().get(0)));
  }

  @Test
  public void testById() {
    TestFlows testFlows = Flows
        .from(TestFlows::length).id("group:f1")
        .to(String::length)
        .build();


    assertEquals(testFlows, Flows.get(TestFlows.class, "group"));
  }

}
