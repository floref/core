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
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.floref.core.dsl.flow.Flows.from;
import static org.junit.Assert.assertTrue;

public class ForEachTest {

  public void aggregator(String s, List<String> results, Exception e) {
    if (e == null) {
      results.add(s + ".");
    } else {
      results.add("error");
    }
  }

  public String s1(String s) {
    return s + "1";
  }
  public String s2(String s) {
    return s + "2";
  }
  public String s3(String s) {
    if (s.equalsIgnoreCase("b12")) {
      throw new RuntimeException("s3");
    } else {
      return s;
    }
  }
  public String joinByDot(String s) {
    return s + "." + s.toUpperCase();
  }
  public List getListSplitByDot(String s) {
    return Arrays.asList(s.split("\\."));
  }

  public String processResults(List<String> results) {
    return results.stream().collect(Collectors.joining( "," ));
  }

  public String processResults3(List<List<String>> results) {
//    return results.stream().collect(Collectors.joining( "," ));
    StringBuilder merge = new StringBuilder();
    results.forEach(list -> {
      for (String s : list) {
        merge.append(s);
      }
    });
    return merge.toString();
  }

  public List<String> processResults2(List<String> results) {
    return results;
  }

  public interface TestFlows {
    String start(Collection<String> s);
    List<String> start2(Collection<String> s);
  }

  @Before
  public void before() {
    Flows.deleteAll();
  }

  @Test
  public void test() {
    ForEachTest test = new ForEachTest();

    TestFlows flows = from(TestFlows::start)
        .forEach()  // Optionally custom .aggregator(test::aggregator)
        .to(test::s1)
        .to(test::s2)
        .end()
        .to(test::processResults)
        .build();

    String result = flows.start(Arrays.asList("a", "b"));
    assertTrue("b12,a12".equals(result) || "a12,b12".equals(result));
  }

  @Test
  public void test2() {
    ForEachTest test = new ForEachTest();

    TestFlows flows = from(TestFlows::start2)
        .forEach()
          .to(test::s1)
          .to(test::s2)
        .end()
        .to(test::processResults2)
        .forEach()
          .to(test::s1)
          .to(test::s2)
        .end()
        .to(test::processResults2)
        .build();

    List<String> result = flows.start2(Arrays.asList("a", "b"));
    assertTrue(result.equals(Arrays.asList(new String[] {"a1212", "b1212"}))
        || result.equals(Arrays.asList(new String[] {"b1212", "a1212"})));
  }

  @Test
  public void imbricatedForEach() {
    ForEachTest test = new ForEachTest();

    TestFlows flows = from(TestFlows::start)
        .forEach()
          .to(test::joinByDot)
          .to(test::getListSplitByDot)
          .forEach()
            .to(test::s1)
            .to(test::s2)
          .end()
        .end()
        .to(test::processResults3)
        .build();

    String result = flows.start(Arrays.asList("a", "b"));
    assertTrue(result.contains("a12") && result.contains("A12") && result.contains("b12") && result.contains("B12"));
  }

  @Test
  public void testWithAggregator() {
    ForEachTest test = new ForEachTest();

    TestFlows flows = from(TestFlows::start)
        .forEach().aggregator(test::aggregator)
          .to(test::s1)
          .to(test::s2)
        .end()
        .to(test::processResults)
        .build();

    String result = flows.start(Arrays.asList("a", "b"));
    assertTrue("b12.,a12.".equals(result) || "a12.,b12.".equals(result));
  }

  @Test
  public void testWithException() {
    ForEachTest test = new ForEachTest();

    TestFlows flows = from(TestFlows::start)
        .forEach()
          .to(test::s1)
          .to(test::s2)
          .to(test::s3)
        .end()
        .to(test::processResults)
        .build();

    String result = flows.start(Arrays.asList("a", "b"));
    assertTrue("a12".equals(result));
  }

}
