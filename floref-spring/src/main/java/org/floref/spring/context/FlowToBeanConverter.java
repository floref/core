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

package org.floref.spring.context;

/**
 * See https://stackoverflow.com/questions/28374000/spring-programmatically-generate-a-set-of-beans
 *
 * Please see BeanDefinitionRegistryPostProcessor.postProcessBeanDefinitionRegistry() method javadocs. This is exactly
 * what you are in need of, because it lets you modify Spring's application context after normal bean definitions have
 * been loaded but before any single bean has been instantiated.
 *
 *
 *
 * BeanDefinitionRegistryPostProcessor should avoid the need described on the next line, still if the flows are created
 * within a method like postConstruct or preConstruct or context refreshed or from a method that is called in a bean.
 * Still with it you may fix some of them but not all.
 * If you use Spring the bean containing the flow definition must be constructed before the bean where you autowire.
 *
 * @author Cristian Donoiu
 */

//@Configuration
public class FlowToBeanConverter {  // implements BeanDefinitionRegistryPostProcessor {


}
