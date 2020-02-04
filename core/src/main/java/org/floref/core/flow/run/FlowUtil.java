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

package org.floref.core.flow.run;

import java.util.Map;

import org.floref.core.config.FlowConfiguration;
import org.floref.core.config.injector.BeanInjector;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.dsl.flow.runtime.Aliases;
import org.floref.core.flow.registry.FlowRegistry;

public class FlowUtil {
  public static final String CANCELLED = "__FLOREF_cancelled";

  /**
   * Cancels the current flow which is executing the current thread. It will also try to
   * stop any children substeps (e.g. forked, parallel, etc) if they did not run yet.
   * The threads are not interrupted.
   */
  public static void cancelFlow() {
    FlowSession.set(CANCELLED, true);
  }

  /**
   * @return true if the current thread is interrupted or the flow currently running was cancelled using
   * <code>FlowUtil.cancelFlow()</code>
   */
  public static boolean isCancelledFlow() {
    return Boolean.TRUE.equals(FlowSession.get(CANCELLED)) || Thread.currentThread().isInterrupted();
  }

  public static void clearAllFlowData() {
    Flows.deleteAll();
    Aliases.deleteAll();
    FlowConfiguration.setBeanInjector(new BeanInjector() {
      @Override
      public Object getBean(Class beanClass) {
        return null;
      }
    });
  }
}

