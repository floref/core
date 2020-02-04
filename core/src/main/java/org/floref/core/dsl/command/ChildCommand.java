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

import org.floref.core.dsl.command.group.ParentCommand;

/**
 * Base interface for all flow commands.
 *
 * @author Cristian Donoiu
 */
public abstract class ChildCommand implements FlowCommand { //}, ParentCommand {

  protected ParentCommand parent;

  @Override
  public ParentCommand getParent() {
    return parent;
  }

  @Override
  public void setParent(ParentCommand parentCommand) {
    // Only add the relation one way since for example the parent of an 'otherwise' must be the parent of then but
    // otherwise is not known as a child (only the 'then' is the entry point).
    this.parent = parentCommand;
  }

}
