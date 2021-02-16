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

package org.floref.core.dsl.command;

import org.floref.core.dsl.TestFlows;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.exception.FlowDefinitionException;
import org.junit.Before;
import org.junit.Test;

import static org.floref.core.dsl.flow.Flows.from;
import static org.junit.Assert.*;

public class ToTest {

  public static class StringMerger {
    public String two(String a, String b) {
      return a + b;
    }
  }

  public static class StringLength {
    public int length(String a) {
      return a.length();
    }

    public String fromInt(int value) {
      return Integer.toString(value);
    }
  }

  @Before
  public void clearFlows() {
    Flows.deleteAll();
  }

  @Test
  public void testNonDefinedFlow() {
    TestFlows flows = from(TestFlows::takeTwoStringsReturnInt)
        .to(String::length)
        .build();
    try {
      flows.mergeTwoStrings("a", "bc");
    } catch (FlowDefinitionException e) {
      assertTrue(e.getMessage().contains("Flow not yet defined for "));
      return;
    }
    fail("exception not thrown");
  }

  @Test
  public void testTo() {
    StringMerger stringMerger = new StringMerger();
    StringLength stringLength = new StringLength();

    // Should not pass validation as the last method returns a type different than mergeTwoStrings
    TestFlows flows = from(TestFlows::takeTwoStringsReturnInt)
        .to(stringMerger::two)
        .to(stringLength::length)
        .build();
    assertEquals(3, flows.takeTwoStringsReturnInt("a", "bc"));
  }
}
