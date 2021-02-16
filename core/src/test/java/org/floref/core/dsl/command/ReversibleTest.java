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

import org.junit.Test;

import static org.floref.core.dsl.flow.Flows.from;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReversibleTest {
  public class Pojo { // some pojo
    int i;
  }

  public Pojo plus(Pojo pojo) {
    pojo.i += 2;
    return pojo;
  }

  public Pojo minus(Pojo pojo) {
    pojo.i -= 2;
    return pojo;
  }

  public Pojo multiply(Pojo pojo) {
    pojo.i = pojo.i * 2;
    return pojo;
  }

  public Pojo divide(Pojo pojo) {
    pojo.i = pojo.i / 2;
    return pojo;
  }

  public Pojo failHere(Pojo pojo) {
    throw new RuntimeException("failHere");
  }

  public interface Flows {
    Pojo testRevert(Pojo i);
  }

  @Test
  public void test() {
    ReversibleTest test = new ReversibleTest();
    Flows flows = from(Flows::testRevert)
        .reversible()
        .to(test::plus).revertBy(test::minus)
        .to(test::multiply).revertBy(test::divide)
        .to(test::failHere)
        .build();

    Pojo pojo = new Pojo();
    pojo.i = 2;
    try {
      flows.testRevert(pojo);
    } catch (Exception e) {
      assertTrue(e.getMessage().contains("failHere"));
      assertEquals(2, pojo.i);
    }
  }
}
