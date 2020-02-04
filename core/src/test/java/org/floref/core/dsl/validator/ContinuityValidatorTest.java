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

package org.floref.core.dsl.validator;

import static junit.framework.Assert.assertEquals;

public class ContinuityValidatorTest {


  public static class Pojo {
    public int length(String s) {
      return s.length();
    }
  }

  public interface TestFlows {
    int length(String s);
    int length2(String s);
  }

//  @Test
//  public void test() {
//    Pojo pojo = new Pojo();
//
//    AliasesTest.TestFlows testFlows = Flows.from(AliasesTest.TestFlows::length).alias("flow1")
//        .to(pojo::length).alias("pojoLength")
//        .to(Integer::reverse)
//        .build();
//
//    Flows.from(AliasesTest.TestFlows::length2).alias("flow2")
//        .to(AliasesTest.TestFlows::length)
//        .build();
//
//    String json = Flows.export(AliasesTest.TestFlows.class);
//    assertEquals(3, testFlows.length("abc"));
//
//    assertEquals("{\"flows\":[{\"from\":\"flow1\",\"to\":\"pojoLength\"},{\"from\":\"flow2\",\"to\":\"flow1\"}],\"aliases\":{\"flow1\":\"org.floref.core.flow.alias.AliasesTest.TestFlows::length\",\"flow2\":\"org.floref.core.flow.alias.AliasesTest.TestFlows::length2\",\"pojoLength\":{\"type\":\"runtime\",\"reference\":\"org.floref.core.flow.alias.AliasesTest.Pojo::length\"}}}",
//        json);
//  }

}
