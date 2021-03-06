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

package org.floref.core.dsl.flow.foreach;

import org.floref.core.dsl.flow.BaseInstructionImpl;
import org.floref.core.dsl.flow.data.FlowInstruction;
import org.floref.core.dsl.flow.group.GroupUtil;

/**
 * forEach DSL entry point.
 *
 * @author Cristian Donoiu
 */
public class ForEach<P, F> extends BaseInstructionImpl<P, F> implements GroupUtil<ForEach<P, F>> {

  public ForEach(FlowInstruction flowInstruction) {
    copyData(flowInstruction);
  }
}
