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

package org.floref.core.flow.annotation;

import org.floref.core.config.FlowConfiguration;
import org.floref.core.config.injector.BeanInjector;
import org.floref.core.dsl.TestFlows;
import org.floref.core.dsl.TestInterface;
import org.floref.core.dsl.TestInterfaceImpl;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.exception.FlowDefinitionException;
import org.junit.Before;
import org.junit.Test;

import static org.floref.core.dsl.flow.Flows.from;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FlowVarTest {

  TestInterface f() {
    return new TestInterfaceImpl();
  }

  @Before
  public void before() {
    Flows.deleteAll();
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
  }
  @Test
  public void test() {


    TestFlows testFlows = from(TestFlows::processOneStringWithAnnotation)
        .to(TestInterface::length)
        .to(TestInterface::toUpperCase)
        .build();

    try {
      testFlows.processOneStringWithAnnotation("abc");
    } catch (FlowDefinitionException e) {
      assertTrue(e.getMessage().contains("Method parameter mismatch for 'org.floref.core.dsl.TestInterface::toUpperCase' found 'class java.lang.Integer' but a 'java.lang.String' is expected"));
      return;
    }
    fail();
  }
}
