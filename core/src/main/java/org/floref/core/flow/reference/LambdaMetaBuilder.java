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

import static org.floref.core.flow.reference.Methods.getMethod;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.floref.core.exception.FlowDefinitionException;

/**
 * Used to obtain method reference metadata.
 * Code inspired by:
 * https://cr.openjdk.java.net/~briangoetz/lambda/lambda-translation.html
 * http://benjiweber.co.uk/blog/2015/08/17/lambda-parameter-names-with-reflection/
 * https://dzone.com/articles/how-and-why-to-serialialize-lambdas
 * https://stackoverflow.com/questions/22807912/how-to-serialize-a-lambda
 * https://in.relation.to/2016/04/14/emulating-property-literals-with-java-8-method-references/
 * https://github.com/ninjaframework/ninja/blob/develop/ninja-core/src/main/java/ninja/utils/Lambdas.java
 *
 * @author Cristian Donoiu
 */
public class LambdaMetaBuilder {

  public static SerializedLambda getSerializedLambda(Object lambda) {

    for (Class<?> clazz = lambda.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
      try {
        Method replaceMethod = clazz.getDeclaredMethod("writeReplace"); // Test if bean has custom serializator.
        replaceMethod.setAccessible(true);
        SerializedLambda serializedLambda = (SerializedLambda) replaceMethod.invoke(lambda);

        return serializedLambda;
      } catch (NoSuchMethodException e) {
        throw new IllegalArgumentException("not a lambda");
      } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new RuntimeException("can not serialize lambda", e);
      } catch (NoSuchMethodError e) {
        // continue
      }
    }
    throw new RuntimeException("'writeReplace' not found");
  }



  public static LambdaMeta getLambdaMeta(MethodReference lambda) {
    LambdaMeta lambdaMeta = new LambdaMeta();
    SerializedLambda serializedLambda = getSerializedLambda(lambda);
    try {
      // "package/subpackage/.../Class" format.
      lambdaMeta.setLambdaClass(Class.forName(serializedLambda.getImplClass().replace('/', '.')));
    } catch (ClassNotFoundException e) {
      throw new FlowDefinitionException("Can not find class of method reference", e);
    }
    if (!Modifier.isPublic(lambdaMeta.getLambdaClass().getModifiers())) {
      throw new FlowDefinitionException("Class " + lambdaMeta.getLambdaClass().getCanonicalName() + " should be public.");
    }

    Method method = getMethod(lambdaMeta.getLambdaClass(), serializedLambda.getImplMethodName());
    if (!Modifier.isPublic(method.getModifiers())) {
      throw new FlowDefinitionException("Method " + Methods.getMethodReferenceAsString(method) + " should be public.");
    }
    lambdaMeta.setLambdaMethod(method);
    lambdaMeta.buildMethodMeta(method);

    if (serializedLambda.getCapturedArgCount() > 0) {
      lambdaMeta.setTarget(serializedLambda.getCapturedArg(0));
    }

    return lambdaMeta;
  }

  public static LambdaMeta getLambdaMeta(String methodReferenceAsString) {
    String parts[] = methodReferenceAsString.split("::");
    if (parts.length != 2) {
      throw new FlowDefinitionException(methodReferenceAsString + " is not a method reference (canonicalClassName::method)");
    }
    Class clazz;
    try {
      clazz = Class.forName(parts[0]);
    } catch (ClassNotFoundException e) {
      throw new FlowDefinitionException("Undefined class " + parts[0]);
    }
    if (!Modifier.isPublic(clazz.getModifiers())) {
      throw new FlowDefinitionException("Class " + clazz.getCanonicalName() + " should be public.");
    }

    Method method = getMethod(clazz, parts[1]);
    if (!Modifier.isPublic(method.getModifiers())) {
      throw new FlowDefinitionException("Method " + Methods.getMethodReferenceAsString(method) + " should be public.");
    }

    LambdaMeta lambdaMeta = new LambdaMeta();
    lambdaMeta.setLambdaClass(clazz);
    lambdaMeta.setLambdaMethod(method);
    lambdaMeta.buildMethodMeta(method);

    return lambdaMeta;
  }

  public static LambdaMeta getLambdaMeta(Class clazz, Method sample) {
    Method method = Methods.getMethod(clazz, sample.getName());
//    sample.getDeclaringClass()
    LambdaMeta lambdaMeta = new LambdaMeta();
    lambdaMeta.setLambdaClass(clazz);
    lambdaMeta.setLambdaMethod(method);
    return lambdaMeta;
  }

  public static <T, U> LambdaMeta getLambdaMetaFromConsumer(ParamBiConsumer<T, U> consumer) {
    return getLambdaMeta(consumer);
  }
  public static <T, U, V> LambdaMeta getLambdaMetaFromConsumer(ParamTriConsumer<T, U, V> consumer) {
    return getLambdaMeta(consumer);
  }
}
