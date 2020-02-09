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

import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.dsl.flow.data.FlowDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowObjectMethods {
  public static final String EQUALS = "equals";
  public static final String HASHCODE = "hashCode";
  public static final String TOSTRING = "toString";

  public static boolean equals(Object flow1, Object flow2) {
    return flow1 == flow2;
  }

  public static int hashCode(Object flow) {
    return flow.hashCode();
  }

  private static String indentation(int indent) {
    return new String(new char[indent]).replace("\0", "  ");
  }

  private static void navigate(FlowCommand flowCommand, StringBuilder definition, int indent) {
    String string = flowCommand.toString();
    definition.append("\n" + indentation(indent) + "." + string);
  }


  public static class ToStringNumbering {
    List<String> names = new ArrayList();
    List<Object> objects = new ArrayList();
    String prefix;
    int current = 1;
    public ToStringNumbering(Class clazz) {
      String className = clazz.getSimpleName();
      prefix = Character.toLowerCase(className.charAt(0)) + className.substring(1);
    }
    public String getName(Object target) {
      String name = null;
      int index = objects.indexOf(target);
      if (index == -1) {
        name = prefix + (current == 1 ? "" : current);
        names.add(name);
        current++;
        objects.add(target);
      } else {
        name = names.get(index);
      }
      return name;
    }
  }
  public static class ToStringIndexes {
    Map<String, ToStringNumbering> map = new HashMap<>();

    public String getName(Object target) {
      String className = target.getClass().getCanonicalName();
      ToStringNumbering toStringNumbering = map.get(className);
      if (toStringNumbering == null) {
        toStringNumbering = new ToStringNumbering(target.getClass());
        map.put(className, toStringNumbering);
      }
      return toStringNumbering.getName(target);
    }
  }
  public static String toString(FlowInstanceData flowInstanceData) {
    ToStringIndexes toStringIndexes = new ToStringIndexes();

    List<FlowDefinition> flowDefinitions = flowInstanceData.getFlowDefinitions();
    StringBuilder definitions = new StringBuilder();
    for (FlowDefinition flowDefinition : flowDefinitions) {
      List<FlowCommand> flowCommands = flowDefinition.getFlowCommands();
      definitions.append(flowCommands.get(0).toString());

      for (int i = 1; i < flowCommands.size(); i++) {
        navigate(flowCommands.get(i), definitions, 1);
      }

      definitions.append(";\n\n");
    }

    return definitions.toString();
  }
}
