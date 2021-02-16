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

package org.floref.core.flow.reference;

import java.io.Serializable;

/**
 * Catches 4 params method references with instance target or 3 params with class target.
 *
 * @author Cristian Donoiu
 */
public interface ParamTetraConsumer<T, U, V, X> extends MethodReference<T>, Serializable {
  void accept(T t, U u, V v, X x) throws Exception;
}
