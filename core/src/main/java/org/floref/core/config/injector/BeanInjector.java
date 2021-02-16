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

package org.floref.core.config.injector;

/**
 * Gets a bean to be used(injected) when the flow is run. This is needed because:
 * 1. Method references can be on interface methods and thus an actual implementation is needed when the flow is run.
 * 2. Method references can be on class methods for which actual instances are managed by a dependency injection
 * framework like Spring and thus the BeanInjector needs to load the bean instance from the framework custom APIs.
 *
 * @author Cristian Donoiu
 * @See SpringBeanInjector
 */
public interface BeanInjector {

  /**
   * Gets a bean to be used(injected) when the flow is run.
   *
   * @param beanClass is an interface or class for which a bean in required to be injected.
   * @return the corresponding bean implementing the provided class/interface. Return null if there is no bean
   * available.
   */
  Object getBean(Class beanClass);

  /**
   * Determine the target class of the given bean instance which might be an JDK proxy, a cglib proxy, etc.
   * This is needed because when a flow using beans is exported the beans might be proxy instances but the exported
   * flow definition needs to refer the bean target class in method references.
   *
   * @param candidate
   * @return
   */
  default Class getTargetClass(Object candidate) {
    return candidate.getClass();
  }
}
