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

import org.floref.core.config.FlowConfiguration;
import org.floref.core.config.injector.BeanInjector;
import org.floref.core.dsl.TestFlows;
import org.floref.core.dsl.TestService;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.flow.run.FlowUtil;
import org.junit.Before;
import org.junit.Test;

import static org.floref.core.dsl.flow.Flows.from;
import static org.junit.Assert.assertEquals;

public class FlowImportTest {

  @Before
  public void before() {
    FlowUtil.clearAllFlowData();
  }

  @Test
  public void importExport() {
    TestService testService = new TestService();
    FlowConfiguration.setBeanInjector(new BeanInjector() {
      @Override
      public Object getBean(Class beanClass) {
        return testService;
      }
    });

    TestFlows flows = from(TestFlows::mergeTwoStrings)
        .to(testService::mergeTwoStrings)
        .to(testService::toUpperCase).alias("makeItBig")
        .build();

    // 1. Export flow.
    String json = Flows.export(flows);
    Flows.deleteAll(); // Clear all.
    assertEquals(null, Flows.get(TestFlows.class));

    // 2. Import flow from JSON.
    Flows.importFlows(json);
    flows = Flows.get(TestFlows.class);

    // 3. Test the flow is runnable.
    assertEquals("AB", flows.mergeTwoStrings("a", "b"));
    assertEquals(json, Flows.export(flows));  // round trip test.
  }
}
