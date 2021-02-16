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
import org.junit.Test;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;

public class RetryTest {

  public void callRestClient(StringBuilder context) throws TimeoutException {
    context.append(".");
    if (context.length() < 3) {
      throw new TimeoutException("Server busy, try again later");
    }
    context.append("done");
  }

  public interface RetryFlow {
    void retryCall(StringBuilder context);
  }

  @Test
  public void test() {
    RetryTest test = new RetryTest();
    RetryFlow flow = Flows.from(RetryFlow::retryCall)
        .retry(test::callRestClient).delay(100).times(10).on(TimeoutException.class)
        .build();

    StringBuilder data = new StringBuilder("");
    flow.retryCall(data);
    assertEquals("...done", data.toString());
  }
}
