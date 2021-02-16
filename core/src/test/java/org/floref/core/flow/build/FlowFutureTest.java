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


import org.floref.core.dsl.TestFlows;
import org.floref.core.dsl.TestService;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.exception.FlowCancelledException;
import org.floref.core.flow.run.FlowSession;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.floref.core.dsl.flow.Flows.from;
import static org.junit.Assert.*;

public class FlowFutureTest {

  @Before
  public void before() throws InterruptedException {
    Flows.deleteAll();
  }

  @Test
  public void testFuture() throws ExecutionException, InterruptedException {
    TestService testService = new TestService();

    TestFlows testFlows = from(TestFlows::mergeTwoStringsInFuture)
        .to(testService::longRunningTask)
        .build();

    Future<String> future = testFlows.mergeTwoStringsInFuture("a", "b");
    assertEquals(false, future.isDone());
    assertEquals("ab", future.get());
    assertEquals(true, future.isDone());
    assertTrue(FlowSession.isEmpty());
  }

  @Test(expected = java.util.concurrent.CancellationException.class)
  public void testFutureCancellation() throws ExecutionException, InterruptedException {
    TestService testService = new TestService();

    TestFlows testFlows = from(TestFlows::mergeTwoStringsInFuture)
        .to(testService::longRunningTask)
        .to(testService::inverseString)
        .build();

    Future<String> future = testFlows.mergeTwoStringsInFuture("a", "b");
    assertEquals(false, future.isDone());
    future.cancel(false);

    assertTrue(future.isCancelled());
    future.get();

  }

  @Test
  public void testThreadInterruptWhileInProgress() throws ExecutionException, InterruptedException {
    TestService testService = new TestService();

    TestFlows testFlows = from(TestFlows::mergeTwoStrings)
        .to(testService::veryLongRunningTask)
        .to(testService::inverseString)
        .build();

    Thread thread = Thread.currentThread();
    new Thread(() -> {
      try {
        Thread.currentThread().sleep(500);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      thread.interrupt();
    }).start();


    try {
      testFlows.mergeTwoStrings("a", "b");
    } catch (FlowCancelledException e) {
      assertEquals("Flow org.floref.core.dsl.TestFlows::mergeTwoStrings has been cancelled before running"
          + " org.floref.core.dsl.TestService::inverseString", e.getMessage());
      return;
    } finally {
      try {
        thread.join();  // This is needed in order to reset the thread interrupt status, or else subsequent tests
        // might fail.
      } catch (Exception e) {
      }
    }
    fail();
  }

  @Test
  public void testFutureCancellationViaSession() throws ExecutionException, InterruptedException {
    TestService testService = new TestService();

    TestFlows testFlows = from(TestFlows::mergeTwoStringsInFuture)
        .to(testService::longRunningTask)
        .to(testService::cancelFlow)
        .to(testService::inverseString)
        .build();

    Future<String> future = testFlows.mergeTwoStringsInFuture("a", "b");
    try {
      future.get();
    } catch (Exception e) {
      assertEquals("Flow org.floref.core.dsl.TestFlows::mergeTwoStringsInFuture has been cancelled before"
          + " running org.floref.core.dsl.TestService::inverseString", e.getCause().getMessage());
      return;
    }
    fail();
  }
}
