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

package org.floref.core.dsl;

import org.floref.core.flow.annotation.FlowVar;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface TestFlows {

  void processZeroNoReturn();

  int processZeroWithReturn();

  String processOneStringReturnString(String s);

  String processOneStringWithAnnotation(@FlowVar("s1") String s);

  String mergeTwoStrings(String s1, String s2);

  Future<String> mergeTwoStringsInFuture(String s1, String s2);

  CompletableFuture<String> mergeTwoStringsInCompletableFuture(String s1, String s2);

  String mergeThreeStrings(String s1, String s2, String s3);

  String mergeFourStrings(String s1, String s2, String s3, String s4);

  String mergeFiveStrings(String s1, String s2, String s3, String s4, String s5);

  String mergeSixStrings(String s1, String s2, String s3, String s4, String s5, String s6);

  String mergeSevenStrings(String s1, String s2, String s3, String s4, String s5, String s6, String s7);

  String mergeEightStrings(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8);

  int takeTwoStringsReturnInt(String s1, String s2);

  int length(String s1);

  void processSix(int one, String two, int three, int four, String six);

  int processSeven(int one, String two, int three, int four, int six, int seven);

  int reverse(int i);


}