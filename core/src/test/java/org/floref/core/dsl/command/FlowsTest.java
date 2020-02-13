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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.dsl.TestFlows;
import org.floref.core.dsl.TestService;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.run.FlowSession;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.floref.core.dsl.flow.Flows.from;
import static org.junit.Assert.*;

public class FlowsTest {
  private static final Log LOG = LogFactory.getLog(FlowsTest.class);

  @Before
  public void before() {
    Flows.deleteAll();
  }

  @After
  public void after() {
    assertTrue(FlowSession.isEmpty());
  }
  @Test
  public void testExceptionWhenFlowsIsNotAClass() {
    try {
      from("a"::length); // You can only define a flow with the target being a class.
    } catch (FlowDefinitionException e) {
      e.printStackTrace();
      assertTrue(e.getMessage().contains("Flow reference must refer flow class: 'from(FlowClass::method)'"));
      return;
    }
    fail("only interfaces are allowed");
  }

  @Test
  public void testExceptionWhenFlowsIsNotInterface() {
    try {
      from(String::length); // You can only define a flow from an interface class.
    } catch (FlowDefinitionException e) {
      e.printStackTrace();
      assertTrue(e.getMessage().contains("java.lang.String needs to be a Java interface in order to define flows from it."));
      return;
    }
    fail("only interfaces are allowed");
  }

  @Test
  public void happyTest() {
    TestService testService = new TestService();
    TestFlows flows = from(TestFlows::mergeTwoStrings)
        .to(testService::mergeTwoStrings)
        .to(testService::toUpperCase)
        .build();

    assertEquals("AB", flows.mergeTwoStrings("a", "b"));
    assertTrue(FlowSession.isEmpty());
  }

  @Test
  public void testSameMethodReferenceUsedTwiceInSameFlow() {
    TestService testService = new TestService();
    TestFlows flows = from(TestFlows::mergeTwoStrings)
        .to(testService::mergeTwoStrings)
        .to(testService::doubleString)
        .to(testService::doubleString)
        .build();

    assertEquals("abababab", flows.mergeTwoStrings("a", "b"));
    assertTrue(FlowSession.isEmpty());
  }

  @Test
  public void testCodeCompilation() {
    TestFlows flows = from(TestFlows::processZeroNoReturn)
        .build();
    flows = from(TestFlows::processZeroWithReturn)
        .build();
    TestService service = new TestService();

    flows = from(TestFlows::processSix)
        .to(TestFlows::processSeven)
        .build();
    flows = from(TestFlows::processOneStringReturnString)
        .to(service::one)
        .build();
    flows = from(TestFlows::mergeTwoStrings)
        .to(service::two)
        .build();
    flows = from(TestFlows::mergeThreeStrings)
        .to(service::three)
        .build();
    flows = from(TestFlows::mergeFourStrings)
        .to(service::four)
        .build();
    flows = from(TestFlows::mergeFiveStrings)
        .to(service::five)
        .build();
    flows = from(TestFlows::mergeSixStrings)
        .to(service::six)
        .build();
    flows = from(TestFlows::mergeSevenStrings)
        .to(service::seven)
        .build();
  }

  @Test
  public void testFlowDefinitionAndFlowComposing() {
    TestService merger = new TestService();

    TestFlows flows = from(TestFlows::mergeTwoStrings)
        .to(merger::mergeTwoStrings)
        .build();
    String s = flows.mergeTwoStrings("a", "b");
    assertEquals("ab", s);

    TestFlows flows2 = Flows.getFlow(TestFlows.class);
    assertEquals(flows, flows2);
    assertEquals("12", flows2.mergeTwoStrings("1", "2"));

    TestFlows flows3 = from(TestFlows::takeTwoStringsReturnInt)
        .to(TestFlows::mergeTwoStrings)
        .to(merger::length)
        .build();
    assertEquals(flows, flows3);
    assertEquals(3, flows3.takeTwoStringsReturnInt("12", "3"));
  }

  @Test(expected = java.lang.NullPointerException.class)
  public void testNullReference() {

    TestService merger = null;

    TestFlows flows = from(TestFlows::mergeTwoStrings)
        .to(merger::mergeTwoStrings) // will fail before going deeper.
        .build();

  }
//  @Test
//  public void testInvalidFlow() {
//    TestService merger = new TestService();
//
//    try {
//      TestFlows flows = from(TestFlows::mergeTwoStrings)
//          .to(merger::mergeTwoStrings)
//          .to(merger::mergeTwoStrings)
//          .build();
//    } catch (FlowDefinitionException e) {
//      assertEquals("Invalid flow 'org.floref.core.dsl.TestFlows::mergeTwoStrings'"
//          + " method org.floref.core.dsl.TestService::mergeTwoStrings returns 'String' but method"
//          + " org.floref.core.dsl.TestService::mergeTwoStrings expects a 'String'", e.getMessage());
//      return;
//    }
//    fail("should have thrown an exception");
//  }

  @Test
  public void testToString() {
    TestService merger = new TestService();
    TestFlows flows = from(TestFlows::mergeTwoStrings)
        .to(merger::mergeTwoStrings)
        .fork(merger::length)
        .build();

//    FlowRef
//        .start()
//        .from(TestFlows::processOneStringReturnString)
//        .to(merger::takeTwoStringsReturnInt)
//        .build();
    flows.toString();
  }

  @Test
  public void testToString2() {
    TestService merger = new TestService();
    TestFlows flows = from(TestFlows::mergeTwoStrings)
        .to(merger::mergeTwoStrings)
        .build();

//    FlowRef
//        .start()
//        .from(TestFlows::processOneStringReturnString)
//        .to(merger::takeTwoStringsReturnInt)
//        .build();
    flows.mergeTwoStrings("a", "b");
  }
//  @Test
//  public void testMethodRefOnClassCreatesNewInstanceForThatClassOrAutowiresBeanOfThatClass() {
//
//  }
  // Test method that returns void within the flow.



}
