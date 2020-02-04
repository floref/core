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

package org.floref.core.flow.reference.predicate;

import java.io.Serializable;
import org.floref.core.flow.reference.MethodReference;

/**
 * Catches 7 params method references with instance target or 6 params with class target.
 * @author Cristian Donoiu
 */
public interface PredicateParamHeptaConsumer<T, U, V, X, Y, Z, A> extends MethodReference<T>, Serializable {
  boolean accept(T t, U u, V v, X x, Y y, Z z, A a) throws Exception;
}
