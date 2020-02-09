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

import org.floref.core.dsl.command.group.ForEachCommand;
import org.floref.core.dsl.command.group.ParallelCommand;
import org.floref.core.dsl.command.group.ReversibleCommand;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.reference.MethodReference;

import java.util.HashMap;
import java.util.Map;

/**
 * Keeps track of all commands and their keyword.
 *
 * @author Cristian Donoiu
 */
public class FlowCommandBuilders {
  public static final String FROM = "from";
  public static final String TO = "to";
  public static final String FORK = "fork";
  public static final String WHEN = "when";
  public static final String OTHERWISE = "otherwise";
  public static final String PARALLEL = "parallel";
  public static final String FOR_EACH = "forEach";
  public static final String REVERSIBLE = "reversible";
  public static final String RETRY = "retry";
  public static Map<String, FlowCommandBuilder> builders = new HashMap<>();

  static {
    init();
  }
  static void init() {
    addBuilder(TO, params -> {
      if (params[0] instanceof MethodReference) {
        return new ToCommand((MethodReference) params[0]);
      } else {
        return new ToCommand((LambdaMeta) params[0]);
      }
    });
    addBuilder(FORK, params -> {
      if (params[0] instanceof MethodReference) {
        return new ForkCommand((MethodReference) params[0]);
      } else {
        return new ForkCommand((LambdaMeta) params[0]);
      }
    });
    addBuilder(WHEN, params -> {
      if (params[0] instanceof MethodReference) {
        return new WhenCommand((MethodReference) params[0]);
      } else {
        return new WhenCommand((LambdaMeta) params[0]);
      }
    });
    addBuilder(OTHERWISE, params -> new OtherwiseCommand());
    addBuilder(PARALLEL, params -> new ParallelCommand());
    addBuilder(FOR_EACH, params -> new ForEachCommand());
    addBuilder(REVERSIBLE, params -> new ReversibleCommand());
    addBuilder(RETRY, params -> {
      if (params[0] instanceof MethodReference) {
        return new RetryCommand((MethodReference) params[0]);
      } else {
        return new RetryCommand((LambdaMeta) params[0]);
      }
    });
  }

  public static void addBuilder(String id, FlowCommandBuilder flowCommandBuilder) {
    builders.put(id, flowCommandBuilder);
  }

  public static FlowCommand build(String id, Object... params) {
    return builders.get(id).build(params);
  }
}
