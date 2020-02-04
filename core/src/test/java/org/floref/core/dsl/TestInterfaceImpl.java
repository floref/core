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

import org.floref.core.flow.annotation.FlowVar;

public class TestInterfaceImpl implements TestInterface {

  @FlowVar("length")
  public int length(String s) {
    return s.length();
  }

  @FlowVar("s2")
  public String toUpperCase(@FlowVar("s1") String s) {
    return s.toUpperCase();
  }

  public int add(@FlowVar("length") int i, String s1) {
    return i + s1.length();
  }

  public int add(@FlowVar("s1") String s1, @FlowVar("s2") String s2, int i) {
    return s1.length() + s2.length() + i;
  }

  public int totalLength(String s1, String s2) {  // no match for this.
    return s1.length() + s2.length();
  }
}
