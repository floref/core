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

import org.floref.core.dsl.flow.Flows;
import org.floref.core.exception.FlowTimeoutException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.floref.core.dsl.flow.Flows.from;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ParallelTest {

  public void aggregator(String s, List<String> results) {
    results.add(s + ".");
  }

  public String s1(String s) {
    return s + "1";
  }

  public String s2(String s) {
    return s + "2";
  }

  public String s2VeryLong(String s) {
    try {
      Thread.currentThread().sleep(Long.MAX_VALUE);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    return s + "2";
  }

  public String s3(String s) {
    throw new RuntimeException("s3");
  }

  public String processResults(List<String> results) {
    return results.stream().collect(Collectors.joining(","));
  }

  public interface TestFlows {
    String start(String s);
  }

  @Before
  public void before() {
    Flows.deleteAll();
  }

  @Test
  public void test() {
    ParallelTest test = new ParallelTest();

    TestFlows flows = from(TestFlows::start)
        .parallel()  // Optionally custom .aggregator(test::aggregator)
        .to(test::s1)
        .to(test::s2)
        .end()
        .to(test::processResults)
        .build();

    String result = flows.start("a");
    assertTrue("a1,a2".equals(result) || "a2,a1".equals(result));
  }

  @Test
  public void testWithAggregator() {
    ParallelTest test = new ParallelTest();

    TestFlows flows = from(TestFlows::start)
        .parallel().aggregator(test::aggregator)
        .to(test::s1)
        .to(test::s2)
        .end()
        .to(test::processResults)
        .build();

    String result = flows.start("a");
    assertTrue("a2.,a1.".equals(result) || "a1.,a2.".equals(result));
  }

  @Test
  public void testWithException() {
    ParallelTest test = new ParallelTest();

    TestFlows flows = from(TestFlows::start)
        .parallel()
        .to(test::s1)
        .to(test::s2)
        .to(test::s3)
        .end()
        .to(test::processResults)
        .build();

    String result = flows.start("a");
    assertTrue("a1,a2".equals(result) || "a2,a1".equals(result));
  }

  @Test
  public void testWithStopOnException() {
    ParallelTest test = new ParallelTest();

    TestFlows flows = from(TestFlows::start)
        .parallel().aggregator(test::aggregator).stopOnException()
        .to(test::s1)
        .to(test::s3)
        .end()
        .to(test::processResults)
        .build();

    try {
      flows.start("a");
    } catch (RuntimeException e) {
      assertTrue(e.getMessage().contains("s3"));
      return;
    }
    fail();
  }

  @Test
  public void testWithNoStopOnException() {
    ParallelTest test = new ParallelTest();

    TestFlows flows = from(TestFlows::start)
        .parallel() //.stopOnException()
        .to(test::s1)
        .to(test::s2)
        .to(test::s3)
        .end()
        .to(test::processResults)
        .build();

    String result = flows.start("a");
    assertTrue("a1,a2".equals(result) || "a2,a1".equals(result));
  }

  @Test
  public void testTimeout() {
    ParallelTest test = new ParallelTest();

    TestFlows flows = from(TestFlows::start)
        .parallel().aggregator(test::aggregator).timeout(500)
        .to(test::s1)
        .to(test::s2VeryLong)
        .end()
        .to(test::processResults)
        .build();

    try {
      flows.start("a");
    } catch (FlowTimeoutException e) {
      assertTrue(e.getMessage().contains("Timeout while running flow org.floref.core.dsl.command.ParallelTest.TestFlows::start parallel"));
      return;
    }
    fail();
  }

}
