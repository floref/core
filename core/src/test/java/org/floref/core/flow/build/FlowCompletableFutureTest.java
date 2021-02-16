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


import static org.floref.core.dsl.flow.Flows.from;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.floref.core.dsl.TestFlows;
import org.floref.core.dsl.TestService;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.flow.run.FlowSession;
import org.junit.Before;
import org.junit.Test;

public class FlowCompletableFutureTest {

  @Before
  public void before() throws InterruptedException {
    Flows.deleteAll();
  }

  @Test
  public void testFuture() throws ExecutionException, InterruptedException {
    TestService testService = new TestService();

    TestFlows testFlows = from(TestFlows::mergeTwoStringsInCompletableFuture)
        .to(testService::longRunningTask)
        .build();

    Future<String> future = testFlows.mergeTwoStringsInCompletableFuture("a", "b");
    assertEquals(false, future.isDone());
    assertEquals("ab", future.get());
    assertEquals(true, future.isDone());
    assertTrue(FlowSession.isEmpty());
  }

  @Test
  public void testFutureComplete() throws ExecutionException, InterruptedException {
    final StringBuilder stringBuilder = new StringBuilder();
    TestService testService = new TestService();

    TestFlows testFlows = from(TestFlows::mergeTwoStringsInCompletableFuture)
        .to(testService::longRunningTask)
        .build();

    CompletableFuture<String> future = testFlows.mergeTwoStringsInCompletableFuture("a", "b");
    future.whenComplete((string, action) -> {
      stringBuilder.append(string);
      synchronized (stringBuilder) {
        stringBuilder.notifyAll();
      }
    });
    synchronized (stringBuilder) {
      stringBuilder.wait();
    }
    assertEquals("ab", stringBuilder.toString());
  }
}
