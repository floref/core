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
import org.floref.core.dsl.TestService;
import org.floref.core.dsl.flow.Flows;
import org.junit.Before;
import org.junit.Test;

import static org.floref.core.dsl.TestService.SLEEP;
import static org.floref.core.dsl.flow.Flows.from;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ForkTest {

  @Before
  public void before() {
    Flows.deleteAll();
  }

  @Test
  public void testWithoutAsync() {
    TestService testService = new TestService();

    long start = System.currentTimeMillis();
    TestFlows flows = from(TestFlows::mergeTwoStrings)
        .to(testService::longRunningTask)
        .build();

    String result = flows.mergeTwoStrings("a", "b");
    assertTrue(System.currentTimeMillis() - start > SLEEP);
    assertEquals("ab", result);

  }

  @Test
  public void testWithAsync() throws InterruptedException {
    TestService testService = new TestService();

    long start = System.currentTimeMillis();
    TestFlows flows = from(TestFlows::mergeTwoStrings)
        .fork(testService::longRunningTask)
        .build();

    String result = flows.mergeTwoStrings("a", "b");
    assertTrue(System.currentTimeMillis() - start < SLEEP);
    assertEquals(null, result);
    Thread.currentThread().sleep(SLEEP + 1000);
    assertEquals("ab", testService.forkResult);
  }

  @Test
  public void testWithAsync2() throws InterruptedException {
    TestService testService = new TestService();

    TestFlows flows = from(TestFlows::processOneStringReturnString)
        .fork(testService::longRunningTask)
        .to(testService::inverseString)
        .build();

    String result = flows.processOneStringReturnString("ab");
    assertEquals("ba", result);
  }
}
