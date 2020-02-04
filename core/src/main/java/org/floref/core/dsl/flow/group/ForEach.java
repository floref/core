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

package org.floref.core.dsl.flow.group;

import static org.floref.core.dsl.command.FlowCommandBuilders.FOR_EACH;

import org.floref.core.dsl.command.FlowCommandBuilders;
import org.floref.core.dsl.flow.FlowDsl;

/**
 * Contains .forEach command.
 *
 * @author Cristian Donoiu
 */
public interface ForEach<FLOW_DSL> extends FlowDsl<FLOW_DSL>, GroupUtil<FLOW_DSL> {

  default FLOW_DSL forEach() {
    return addParent(FOR_EACH);
  }

}
