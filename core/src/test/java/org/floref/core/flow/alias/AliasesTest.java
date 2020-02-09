/*
 Copyright 2020 the original author or authors.
 *
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 *
      https://www.apache.org/licenses/LICENSE-2.0
 *
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package org.floref.core.flow.alias;

import org.floref.core.config.FlowConfiguration;
import org.floref.core.config.injector.BeanInjector;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.dsl.flow.impex.Aliases;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.run.FlowUtil;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class AliasesTest {

  public static class TestBean {
    public int length(String s) {
      return s.length();
    }

    public static int staticLength(String s) {
      return s.length();
    }
  }

  public interface TestFlows {
    int length(String s);
    int size(String s);
  }

  @Test
  public void testAliasingBeanAndFlow() {
    TestBean testBean = new TestBean();
    FlowConfiguration.setBeanInjector(new BeanInjector() {
      @Override
      public Object getBean(Class beanClass) {
        return testBean;
      }
    });

    TestFlows testFlows = Flows.from(TestFlows::length).alias("flow1")
            .to(TestBean::length).alias("pojoLength")
            .build();

    Flows.from(TestFlows::size).alias("flow2")
            .to(TestFlows::length)
            .build();

    String json = Flows.export(TestFlows.class);
    assertEquals(3, testFlows.length("abc"));

    assertEquals("{\"flows\":[{\"type\":\"from\",\"ref\":\"flow1\",\"steps\":[{\"type\":\"to\",\"ref\":\"pojoLength\"}]},{\"type\":\"from\",\"ref\":\"flow2\",\"steps\":[{\"type\":\"to\",\"ref\":\"flow1\"}]}],\"aliases\":{\"flow1\":\"org.floref.core.flow.alias.AliasesTest.TestFlows::length\",\"flow2\":\"org.floref.core.flow.alias.AliasesTest.TestFlows::size\",\"pojoLength\":\"org.floref.core.flow.alias.AliasesTest.TestBean::length\"}}",
            json);
  }

  @Before
  public void before() {
    FlowUtil.clearAllFlowData();
  }

  @Test
  public void testWithoutAliases() {

    LinkedHashMap map = new LinkedHashMap();
    TestFlows testFlows = Flows.from(TestFlows::length)
        .to(new TestBean()::length)
        .build();

    assertEquals("{\"flows\":[{\"type\":\"from\",\"ref\":\"org.floref.core.flow.alias.AliasesTest.TestFlows::length\",\"steps\":[{\"type\":\"to\",\"ref\":\"org.floref.core.flow.alias.AliasesTest.TestBean::length\"}]}],\"aliases\":{}}",
        Flows.export(TestFlows.class));
  }

  @Test
  public void testWithRuntimeAlias() {
    TestBean testBean = new TestBean();
    FlowConfiguration.setBeanInjector(new BeanInjector() {
      @Override
      public Object getBean(Class beanClass) {
        return testBean;
      }
    });

    TestFlows testFlows = Flows.from(TestFlows::length)
            .to(testBean::length).alias("pojoLength")
            .build();

    //Aliases.alias("pojoLength", pojo::length);

    assertEquals("{\"flows\":[{\"type\":\"from\",\"ref\":\"org.floref.core.flow.alias.AliasesTest.TestFlows::length\",\"steps\":[{\"type\":\"to\",\"ref\":\"pojoLength\"}]}],\"aliases\":{\"pojoLength\":\"org.floref.core.flow.alias.AliasesTest.TestBean::length\"}}",
            Flows.export(TestFlows.class));
  }

  @Test
  public void testWithRuntimeAliasViaAliasesAPI() {
    TestBean testBean = new TestBean();
    FlowConfiguration.setBeanInjector(new BeanInjector() {
      @Override
      public Object getBean(Class beanClass) {
        return testBean;
      }
    });

    TestFlows testFlows = Flows.from(TestFlows::length)
        .to(testBean::length)
        .build();

    Aliases.alias("pojoLength", testBean::length);

    assertEquals("{\"flows\":[{\"type\":\"from\",\"ref\":\"org.floref.core.flow.alias.AliasesTest.TestFlows::length\",\"steps\":[{\"type\":\"to\",\"ref\":\"pojoLength\"}]}],\"aliases\":{\"pojoLength\":\"org.floref.core.flow.alias.AliasesTest.TestBean::length\"}}",
        Flows.export(TestFlows.class));
  }

  @Test
  public void testWithStaticMethodAlias() {
    TestBean testBean = new TestBean();

    TestFlows testFlows = Flows.from(TestFlows::length)
        .to(TestBean::staticLength).alias("pojoLength")
        .build();

//    Aliases.alias("pojoLength", pojo::length);

    assertEquals("{\"flows\":[{\"type\":\"from\",\"ref\":\"org.floref.core.flow.alias.AliasesTest.TestFlows::length\",\"steps\":[{\"type\":\"to\",\"ref\":\"pojoLength\"}]}],\"aliases\":{\"pojoLength\":\"org.floref.core.flow.alias.AliasesTest.TestBean::staticLength\"}}",
        Flows.export(TestFlows.class));
  }



  @Test
  public void testExceptionWhenNotABeanInjectorBean() {
    TestBean testBean = new TestBean();

    try {
      Flows.from(TestFlows::length).alias("flow1")
            .to(testBean::length).alias("pojoLength")
            .build();
    } catch (FlowDefinitionException e) {
      assertTrue(e.getMessage().contains("Unsupported alias \"pojoLength\" for org.floref.core.flow.alias.AliasesTest.TestBean::length because class instance could not be retrieved via BeanInjector"));
      return;
    }
    fail();
  }
}