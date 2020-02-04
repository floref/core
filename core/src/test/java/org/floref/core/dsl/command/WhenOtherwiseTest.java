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

import static org.floref.core.dsl.flow.Flows.from;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.floref.core.dsl.TestFlows;
import org.floref.core.dsl.TestService;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.exception.FlowDefinitionException;
import org.junit.Before;
import org.junit.Test;

public class WhenOtherwiseTest {

  public boolean lengthMultipleOf2(String s) {
    return s.length() % 2 == 0;
  }
  public boolean lengthMultipleOf4(String s) {
    return s.length() % 4 == 0;
  }
  public boolean lengthMultipleOf8(String s) {
    return s.length() % 8 == 0;
  }
  public boolean lengthMultipleOf3(String s) {
    return s.length() % 3 == 0;
  }
  public static boolean evenLengthStatic(String s) {
    return s.length() % 2 == 0;
  }
  public boolean evenLengthTwoStrings(String s1, String s2) {
    return (s1 + s2).length() % 2 == 0;
  }
  public boolean getBoolean() {
    return true;
  }
  public String doubleString(String s) {
    return s + s;
  }

  @Before
  public void before() {
    Flows.deleteAll();
  }

  @Test
  public void testWhen() {
    TestService testService = new TestService();

    TestFlows flows = from(TestFlows::processOneStringReturnString)
        .when(this::lengthMultipleOf2)
          .to(testService::toUpperCase)
        .otherwise()
          .to(testService::toLowerCase)
        .end()
        .build();

    String s = flows.processOneStringReturnString("ab");
    assertEquals("AB", s);

  }

  @Test
  public void testOtherwise() {
    TestService testService = new TestService();

    TestFlows flows = from(TestFlows::processOneStringReturnString)
        .when(this::lengthMultipleOf2)
        .to(testService::toUpperCase)
        .otherwise()
        .to(testService::toLowerCase)
        .end()
        .build();

    String s = flows.processOneStringReturnString("abc");
    assertEquals("abc", s);
  }

  @Test
  public void testInvalidOtherwise() {
    TestService testService = new TestService();

    try {
      TestFlows flows = from(TestFlows::processOneStringReturnString)
          .to(testService::toUpperCase)
          .otherwise()
          .to(testService::toLowerCase)
          .build();
    } catch (FlowDefinitionException e) {
      assertTrue(e.getMessage().contains(".otherwise() has no corresponding 'when()'"));
      return;
    }
    fail();
  }

  @Test
  public void testTooManyEnds() {
    TestService testService = new TestService();

    try {
      TestFlows flows = from(TestFlows::processOneStringReturnString)
          .when(this::lengthMultipleOf2)
          .to(testService::toUpperCase)
          .end()
          .end()
          .build();
    } catch (FlowDefinitionException e) {
      assertTrue(e.getMessage().contains("'.end()' is missing a block command to be ended. Too many '.end()'."));
      return;
    }
    fail();
  }

  //Group commands are **eager**. In the absence of an `end()` they group everything that follows.
  @Test
  public void testEagerness() {
    TestService testService = new TestService();

    TestFlows flows = from(TestFlows::processOneStringReturnString)
        .when(this::lengthMultipleOf2)
          .when(this::lengthMultipleOf4)
            .to(testService::toUpperCase)
          .otherwise()
            .to(testService::toLowerCase)
        .build();

    String s = flows.processOneStringReturnString("ab");
    assertEquals("ab", s);
  }

  @Test
  public void testEagerness2() {
    TestService testService = new TestService();

    TestFlows flows = from(TestFlows::processOneStringReturnString)
        .when(this::lengthMultipleOf2)
          .when(this::lengthMultipleOf4)
            .to(testService::toUpperCase)
          .otherwise()
            .to(testService::toLowerCase)
          .end()
        .end()
        .build();

    String s = flows.processOneStringReturnString("abcd");
    assertEquals("ABCD", s);
  }

  @Test
  public void testEagerness3() {
    TestService testService = new TestService();

    TestFlows flows = from(TestFlows::processOneStringReturnString)
        .when(this::lengthMultipleOf2)
          .when(this::lengthMultipleOf4)
            .to(testService::toUpperCase)
          .end()
        .otherwise()
          .to(testService::toLowerCase)
        .build();

    String s = flows.processOneStringReturnString("ABC");
    assertEquals("abc", s);
  }

  @Test
  public void testEagerness4() {
    TestService testService = new TestService();

    TestFlows flows = from(TestFlows::processOneStringReturnString)
        .when(this::lengthMultipleOf2)
          .when(this::lengthMultipleOf4)
            .when(this::lengthMultipleOf8)
              .to(testService::toUpperCase)
            .end()
          .end()
        .end()
        .build();

    String s = flows.processOneStringReturnString("abcdefgh");
    assertEquals("ABCDEFGH", s);
  }

  @Test
  public void testSequence() {
    TestService testService = new TestService();

    TestFlows flows = from(TestFlows::processOneStringReturnString)
        .when(this::lengthMultipleOf2)
          .when(this::lengthMultipleOf4)
            .to(testService::toUpperCase)
          .end()
        .end()
        .when(this::lengthMultipleOf8)
          .to(testService::doubleString)
        .build();

    String s = flows.processOneStringReturnString("abcdefgh");
    assertEquals("ABCDEFGHABCDEFGH", s);
  }

  @Test
  public void testSequence2() {
    TestService testService = new TestService();

    TestFlows flows = from(TestFlows::processOneStringReturnString)
        .when(this::lengthMultipleOf2)
          .when(this::lengthMultipleOf4)
            .to(testService::toUpperCase)
          .end()
        .end()
        .when(this::lengthMultipleOf3)
          .to(testService::doubleString)
        .build();

    String s = flows.processOneStringReturnString("abc");
    assertEquals("abcabc", s);
  }

  @Test
  public void testSequence3() {
    TestService testService = new TestService();

    TestFlows flows = from(TestFlows::processOneStringReturnString)
        .when(this::lengthMultipleOf2)
          .to(testService::toUpperCase)
          .when(this::lengthMultipleOf3)
            .to(testService::doubleString)
          .otherwise()
            .to(testService::tripleString)
          .end()
          .to(testService::inverseString)
        .build();

    String s = flows.processOneStringReturnString("abcd");
    assertEquals("DCBADCBADCBA", s);
  }

  @Test
  public void testSequence4() {
    TestService testService = new TestService();

    TestFlows flows = from(TestFlows::processOneStringReturnString)
        .when(this::lengthMultipleOf2)
          .to(testService::toUpperCase)
          .when(this::lengthMultipleOf3)
            .to(testService::doubleString)
          .otherwise()
            .to(testService::tripleString)
          .end()
          .to(testService::inverseString)
        .build();

    String s = flows.processOneStringReturnString("abcd");
    assertEquals("DCBADCBADCBA", s);
  }

  @Test
  public void testSequence5() {
    TestService testService = new TestService();

    TestFlows flows = from(TestFlows::processOneStringReturnString)
        .when(this::lengthMultipleOf2)
          .to(testService::toUpperCase)
          .when(this::lengthMultipleOf3)
            .to(testService::doubleString)
          .otherwise()
            .to(testService::tripleString)
          .end()
        .end()
        .to(testService::inverseString)
        .build();

    String s = flows.processOneStringReturnString("abc");
    assertEquals("cba", s);
  }

}
