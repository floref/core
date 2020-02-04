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

import org.floref.core.config.injector.BeanInjector;
import org.floref.core.config.FlowConfiguration;
import org.floref.core.dsl.TestFlows;
import org.floref.core.dsl.TestInterface;
import org.floref.core.dsl.TestInterfaceImpl;
import org.floref.core.dsl.TestService;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.exception.MissingBeanFlowException;
import org.junit.Before;
import org.junit.Test;

public class MethodReferenceCommandTest {


  @Before
  public void clear() {
    FlowConfiguration.setBeanInjector(null);
    FlowConfiguration.setConfigInjector(null);
    Flows.deleteAll();
  }

  @Test
  public void testIfCanBeExecutedOnReturnFromPreviousStep() {
    TestFlows flows = from(TestFlows::length)
        .to(String::length)
        .build();

    assertEquals(3, flows.length("abc"));
  }

//  @Test
//  public void testContinuityExceptionAtBuildTime() {
//    TestFlows flows = Flows.start()
//        .from(TestFlows::processOneStringReturnString)
//        .to(String::takeTwoStringsReturnInt)
//        .build();
//
//    assertEquals(3, flows.length("abc"));
//  }

  @Test
  public void testFlowAggregation() {
    TestService testService = new TestService();
    from(TestFlows::processOneStringReturnString)
        .to(testService::upperCase)
        .build();

    TestFlows flows = from(TestFlows::mergeTwoStrings)
        .to(new TestService()::mergeTwoStrings)
        .to(TestFlows::processOneStringReturnString)
        .build();


    assertEquals("AB", flows.mergeTwoStrings("a", "b"));
  }

  @Test
  public void testBeanInjector() {
    TestInterfaceImpl bean = new TestInterfaceImpl();

    FlowConfiguration.setBeanInjector(new BeanInjector() {
      @Override
      public Object getBean(Class beanClass) {
        if (beanClass == TestInterface.class) {
          return bean;
        }
        return null;
      }
    });

    TestFlows testFlows = from(TestFlows::length)
        .to(TestInterface::length)
        .build();

    assertEquals(3, testFlows.length("abc"));
  }

  @Test
  public void testStaticMethod() {
    TestFlows testFlows = from(TestFlows::length)
        .to(TestService::staticLength)
        .build();

    assertEquals(3, testFlows.length("abc"));
  }

  @Test(expected = MissingBeanFlowException.class)
  public void testMissingBean() {
    TestFlows testFlows = from(TestFlows::length)
        .to(TestInterface::length)
        .build();
    testFlows.length("abc");
  }

}
