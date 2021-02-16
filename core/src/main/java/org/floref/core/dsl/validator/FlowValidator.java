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

package org.floref.core.dsl.validator;

import org.floref.core.dsl.command.FlowCommand;
import org.floref.core.dsl.command.MethodReferenceCommand;
import org.floref.core.dsl.flow.data.FlowDefinition;

/**
 * Flow validator class.
 *
 * @author Cristian Donoiu
 */
public class FlowValidator {

  /**
   * Can not validate the annotations since one FlowVar might be set within a higher level flow.
   * But can test if there are more than 1 params only one is without FlowVar annotation.
   * Can also test for type transmission when there are no annotations for the parameter of method 2 to be of same type
   * or subclass as the parameter of method 1.
   * Can also test if the last returned result is null or an instance of the flow reference return type.
   *
   * @param flowDefinition
   * @param flowCommand
   * @param previous
   */
  private static void validate(FlowDefinition flowDefinition, FlowCommand flowCommand, MethodReferenceCommand previous) {


    //    // Add it as validation at start time, when build or when
//    if (result != null && !method.getReturnType().equals(result.getClass())) {
//      throw new FlowDefinitionException("Incompatible return type: " + flowDefinition.getMethodReferenceAsString() + " returns "
//          + method.getReturnType() + " but the actual flow return value is " + result.getClass().getSimpleName() + " with value " + )
//    }


//    if (flowCommand instanceof MethodReferenceCommand) {
//      MethodReferenceCommand current = (MethodReferenceCommand) flowCommand;
//
//      if (previous != null) {
//        Class previousReturnType = previous.getReturnType();
////        Method method = current.getLambdaMeta().getLambdaActualMethod();
////        Class[] types = method.getParameterTypes();
//        List<MethodParameter> flowMethodParameters = current.getLambdaMeta().getParameters();
//
//        int notAFlowVariable = 0;
//        if (flowMethodParameters.size() > 1) {
//          for (MethodParameter flowMethodParameter : flowMethodParameters) {
//            if (!flowMethodParameter.isFlowVar()) {
//              notAFlowVariable ++;
//            }
//          }
//          if (notAFlowVariable > 1) {
//            throw new FlowValidationException("Method " + current.getLambdaMeta().getMethodReferenceAsString())
//          }
//        }
//
//        if (types.length != 1 || types[0] != previousReturnType) {
//          throw new FlowValidationException("Invalid flow '" + flowDefinition.getMethodReferenceAsString() + "' method "
//              + previous.getMethodReferenceAsString() + " returns '" + previousReturnType.getSimpleName()
//              + "' but method " + current.getMethodReferenceAsString() + " expects a '" + types[0].getSimpleName() + "'");
//        }
//      }
//    }
  }

  /**
   * Performs a continuity validation, including on branches.
   *
   * @param flowDefinition
   */
  public static void validate(FlowDefinition flowDefinition) {

//    List<FlowCommand> flowCommands = flowDefinition.getFlowCommands();
//
//    Class flowReturnType = flowDefinition.getFlowReference().getLambdaMethod().getReturnType();
//
//    MethodReferenceCommand previous = null;
//    Class currentReturnType;
//
//    // Continuity validation.
//    for (FlowCommand flowCommand : flowCommands) {
//      if (!flowCommand.isParent()) { // Then it should have a return type.
//        if (flowCommand instanceof MethodReferenceCommand) {
//          MethodReferenceCommand current = (MethodReferenceCommand)flowCommand;
//
//          if (previous != null) {
//            Class previousReturnType = previous.getReturnType();
//            Method method = current.getLambdaMeta().getLambdaActualMethod();
//            Class[] types = method.getParameterTypes();
//            if (types.length != 1 || types[0] != previousReturnType) {
//              throw new FlowValidationException("Invalid flow '" + flowDefinition.getMethodReferenceAsString() + "' method "
//                  + previous.getMethodReferenceAsString() + " returns '" + previousReturnType.getSimpleName()
//                  + "' but method " + current.getMethodReferenceAsString() + " expects a '" + types[0].getSimpleName() + "'");
//            }
//          }
//          if (flowCommand.considerResult()) {
//            previous = current;
//          }
//        }
//      }
//
//      if (flowReturnType != currentReturnType) {
//        throw new FlowValidationException("expected return type is " + flowReturnType + " but " + "x returns " + currentReturnType.getCanonicalName());
//      }
//    }


  }
}
