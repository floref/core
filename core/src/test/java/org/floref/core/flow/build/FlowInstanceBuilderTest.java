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

package org.floref.core.flow.build;

import org.floref.core.dsl.TestFlows;
import org.floref.core.dsl.TestService;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.exception.FlowDefinitionException;
import org.junit.Before;
import org.junit.Test;

import static org.floref.core.dsl.flow.Flows.from;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class FlowInstanceBuilderTest {

  @Before
  public void before() {
    Flows.deleteAll();
  }

  @Test
  public void testFlowAlreadyDefined() {
    TestService merger = new TestService();
    TestFlows flows = from(TestFlows::mergeTwoStrings)
        .to(merger::mergeTwoStrings)
        .build();

    try {
      from(TestFlows::mergeTwoStrings)
          .to(merger::mergeTwoStrings)
          .build();
    } catch (FlowDefinitionException e) {
      assertEquals("Flow already defined for org.floref.core.dsl.TestFlows::mergeTwoStrings", e.getMessage());
      return;
    }
    fail();
  }

  @Test
  public void testSession() {

    TestService testService = new TestService();
    TestService testService2 = new TestService();
    from(TestFlows::processOneStringReturnString)
        .to(testService::upperCase)
        .build();

    TestFlows flows = from(TestFlows::mergeTwoStrings)
        .to(testService2::mergeTwoStrings)
        .to(TestFlows::processOneStringReturnString)
        .build();

    assertEquals("from(TestFlows::processOneStringReturnString)\n"
        + "  .to(testService::upperCase);\n"
        + "\n"
        + "from(TestFlows::mergeTwoStrings)\n"
        + "  .to(testService::mergeTwoStrings)\n"
        + "  .to(TestFlows::processOneStringReturnString);\n"
        + "\n", flows.toString());

    flows.mergeTwoStrings("a", "b");

  }
}
