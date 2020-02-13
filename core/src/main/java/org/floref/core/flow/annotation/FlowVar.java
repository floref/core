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

package org.floref.core.flow.annotation;

import java.lang.annotation.*;

/**
 * Used to store and retrieve flow session variables. A flow session is valid for the entire time the flow is run
 * including forked code/flows started from it.
 *
 * If annotation is on the method then the value returned will be saved in the session so that it can be used by
 * subsequent methods which can have parameters annotated.
 * <code>
 * interface Users {
 *   void setEmailByUserId(@FlowVar String userId, @FlowVar email)
 *   @FlowVar
 *   User getUser(@FlowVar String userId);
 *   void changeEmail(User user, @FlowVar String email); // Annotation not needed, previous step return was the User.
 *   void sendNotification(@FlowVar User user)           // Annotation needed, previous step return was void.
 * }
 * start().from(Users::setEmailByUserId)
 *   .to(Users::getUser)
 *   .to(Users::changeEmail) // Gets both the user and the info
 *   .to(Users::sendNotification)
 *
 * </code>
 * See also the documentation.
 *
 *
 *
 * @author Cristian Donoiu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Documented
@Inherited
public @interface FlowVar {

  /**
   * Optionally the name of the flow variable as it will be stored/retrieved in/from the flow session.
   * NOTE: If no name is provided then the parameter class canonical name is used to retrieve/store the value from/in
   * the session.
   * If there are more than two session variables of the same type then you will have to refer them by name.
   * <code>
   *       void processJobs(@FlowVar("job1") Job job1, @FlowVar("job2") Job job2) {...}
   * </code>
   */
  String value() default "";

  /**
   * By default the `FlowVar` annotation has a dual behaviour for <b>flow start method parameters</b>: it will first try
   * to read the parameter from the session variable. If the parameter is not in the session then it will save the given
   * parameter with the given session variable name.
   * <br/>
   * This is needed because a flow can run in isolation or be called from other flows. In order to disable this dual
   * behaviour set the "writeOnly" annotation parameter to false.
   */
  boolean writeOnly() default false;

}
