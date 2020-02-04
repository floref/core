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

package org.floref.core.reference;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.floref.core.dsl.TestFlows;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.dsl.flow.runtime.Aliases;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.reference.LambdaMetaBuilder;
import org.floref.core.flow.reference.MethodReference;
import org.floref.core.flow.reference.ParamBiConsumer;
import org.floref.core.flow.reference.ParamTriConsumer;
import org.junit.Before;
import org.junit.Test;

public class LambdaMetaBuilderTest {

  public void f(int i, int j) {

  }
  public <T, U> MethodReference from(ParamBiConsumer<T, U> consumer) {
    return consumer;
  }

  public <T, U, V> MethodReference from(ParamTriConsumer<T, U, V> consumer) {
    return consumer;
  }

  @Before
  public void clearAllFlowData() {
    Flows.deleteAll();
    Aliases.deleteAll();
  }

  @Test
  public void testMethodTargetIsObtained() {

    LambdaMetaBuilderTest test = new LambdaMetaBuilderTest();

    MethodReference methodReference = test.from(test::f);
    LambdaMeta lambdaMeta = LambdaMetaBuilder.getLambdaMeta(methodReference);

    assertEquals("f", lambdaMeta.getLambdaMethod().getName());
    assertEquals(test, lambdaMeta.getTarget());

    lambdaMeta = LambdaMetaBuilder.getLambdaMeta(test.from(TestFlows::mergeTwoStrings));
    assertEquals(TestFlows.class, lambdaMeta.getLambdaClass());
    assertEquals(null, lambdaMeta.getTarget());

  }

  static class Pojo {
    public int length(String s) {
      return s.length();
    }
  }

  public static class PublicPojoNonPublicMethod {
    int length(String s) {
      return s.length();
    }
  }


  @Test
  public void testTargetIsPublic() {
    try {
      TestFlows testFlows = Flows.from(TestFlows::length)
          .to(new Pojo()::length)
          .build();
    } catch (FlowDefinitionException e) {
      assertTrue(e.getMessage().contains("Class org.floref.core.reference.LambdaMetaBuilderTest.Pojo should be public"));
      return;
    }
    fail();
  }

  interface FlowTest {
    int length(String s);
  }
  @Test
  public void testTargetIsPublic2() {
    try {
      FlowTest testFlows = Flows.from(FlowTest::length)
          .to(new Pojo()::length)
          .build();
    } catch (FlowDefinitionException e) {
      assertTrue(e.getMessage().contains("Class org.floref.core.reference.LambdaMetaBuilderTest.FlowTest should be public."));
      return;
    }
    fail();
  }

  @Test
  public void testTargetMethodIsPublic() {
    try {
      TestFlows testFlows = Flows.from(TestFlows::length)
          .to(new PublicPojoNonPublicMethod()::length)
          .build();
    } catch (FlowDefinitionException e) {
      assertTrue(e.getMessage().contains("Method org.floref.core.reference.LambdaMetaBuilderTest.PublicPojoNonPublicMethod::length should be public."));
      return;
    }
    fail();
  }

}
