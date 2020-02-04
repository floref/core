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

package org.floref.core.dsl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.dsl.command.WhenCommand;
import org.floref.core.flow.run.FlowUtil;

public class TestService {
  private static final Log LOG = LogFactory.getLog(TestService.class);
  public static final int SLEEP = 1_000;
  public String mergeTwoStrings(String a, String b) {
    return a + b;
  }
  public String toUpperCase(String s) {
    return s.toUpperCase();
  }
  public String toLowerCase(String s) {
    return s.toLowerCase();
  }
  public String doubleString(String s) {
    return s + s;
  }
  public String tripleString(String s) {
    return s + s + s;
  }

  public String inverseString(String s) {
    String inverse = "";
    for (int i = s.length()-1; i >= 0; i--) {
      inverse += s.charAt(i);
    }
    return inverse;
  }
  public String forkResult;

  public static int staticLength(String s) {
    return s.length();
  }
  public String longRunningTask(String a, String b) throws InterruptedException {
    LOG.info("--- running longRunningTask");
    Thread.currentThread().sleep(SLEEP);
    forkResult = a + b;
    return forkResult;
  }
  public String veryLongRunningTask(String a, String b) throws InterruptedException {
    LOG.info("--- running veryLongRunningTask");
    try {
      Thread.currentThread().sleep(SLEEP * 3);
    } catch (Exception e) {
      // Prevent exiting the flow.
      Thread.currentThread().interrupt();
    }
    return a + b;
  }

  public void cancelFlow() {
    FlowUtil.cancelFlow();
  }

  public int length(String a) {
    return a.length();
  }
  public String upperCase(String s) {
    return s.toUpperCase();
  }

  public void one(String s1) {
//    return null;
  }
//  public String one(String s1) {
//    return null;
//  }
  public void two(String s1, String s2) {
  }
  public void three(String s1, String s2, String s3) {
  }
  public void four(String s1, String s2, String s3, String s4) {
  }
  public void five(String s1, String s2, String s3, String s4, String s5) {
  }
  public void six(String s1, String s2, String s3, String s4, String s5, String s6) {
  }
  public void seven(String s1, String s2, String s3, String s4, String s5,  String s6, String s7) {
  }
  public void eight(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8) {
  }

}
