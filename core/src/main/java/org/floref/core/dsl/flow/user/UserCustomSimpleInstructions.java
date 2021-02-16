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
package org.floref.core.dsl.flow.user;

import org.floref.core.dsl.flow.data.FlowInstruction;
import org.floref.core.flow.reference.ParamVoidConsumerVoidSupplier;

/**
 * This will be copied by the user in a package with the same name and with same class name.
 * Modify/add/remove instructions that will be available in the flow definition API.
 */
public interface UserCustomSimpleInstructions<P> extends FlowInstruction {

  String MY_INSTRUCTION = "instructionName";

  default P instructionName2(ParamVoidConsumerVoidSupplier consumer) {
    return (P) getFlowData().addChild(MY_INSTRUCTION, consumer);
  }
//
//  default <T> P instructionName(ParamSupplier<T> consumer) {
//    return (P)getFlowData().addChild(MY_INSTRUCTION, consumer);
//  }
//
//  default <T> P instructionName(ParamConsumer<T> consumer) {
//    return (P)getFlowData().addChild(MY_INSTRUCTION, consumer);
//  }
//
//  default <T, U> P instructionName(ParamBiConsumer<T, U> consumer) {
//    return (P)getFlowData().addChild(MY_INSTRUCTION, consumer);
//  }
//
//  default <T, U, V> P instructionName(ParamTriConsumer<T, U, V> consumer) {
//    return (P)getFlowData().addChild(MY_INSTRUCTION, consumer);
//  }
//
//  default <T, U, V, X> P instructionName(ParamTetraConsumer<T, U, V, X> consumer) {
//    return (P)getFlowData().addChild(MY_INSTRUCTION, consumer);
//  }
//
//  default <T, U, V, X, Y> P instructionName(ParamPentaConsumer<T, U, V, X, Y> consumer) {
//    return (P)getFlowData().addChild(MY_INSTRUCTION, consumer);
//  }
//
//  default <T, U, V, X, Y, Z> P instructionName(ParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
//    return (P)getFlowData().addChild(MY_INSTRUCTION, consumer);
//  }
//
//  default <T, U, V, X, Y, Z, A> P instructionName(ParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer){
//    return (P)getFlowData().addChild(MY_INSTRUCTION, consumer);
//  }
//
//  default <T> P instructionName(LambdaMeta<T> lambdaMeta) {
//    return (P)getFlowData().addChild(TO, lambdaMeta);
//  }
}
