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

import org.floref.core.exception.FlowDefinitionException;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Methods utils.
 */
public class Methods {

  private static final Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<Class<?>, Class<?>>();

  static {
    primitiveWrapperMap.put(Boolean.TYPE, Boolean.class);
    primitiveWrapperMap.put(Byte.TYPE, Byte.class);
    primitiveWrapperMap.put(Character.TYPE, Character.class);
    primitiveWrapperMap.put(Short.TYPE, Short.class);
    primitiveWrapperMap.put(Integer.TYPE, Integer.class);
    primitiveWrapperMap.put(Long.TYPE, Long.class);
    primitiveWrapperMap.put(Double.TYPE, Double.class);
    primitiveWrapperMap.put(Float.TYPE, Float.class);
    primitiveWrapperMap.put(Void.TYPE, Void.TYPE);
  }

  public static Class<?> primitiveToWrapper(final Class<?> cls) {
    Class<?> convertedClass = cls;
    if (cls != null && cls.isPrimitive()) {
      convertedClass = primitiveWrapperMap.get(cls);
    }
    return convertedClass;
  }

  public static String getMethodReferenceAsString(Method method) {
    return method.getDeclaringClass().getCanonicalName() + "::" + method.getName();
  }

  public static String getMethodReferenceAsString(Class baseClass, Method method) { // The method may be from subclass.
    return baseClass.getCanonicalName() + "::" + method.getName();
  }

  public static Method getMethod(Class<?> clazz, String methodName) {
    Method method = null;
    int sameNameMethod = 0;
    for (Method m : clazz.getDeclaredMethods()) {  // public/private and only of the current class.
      if (m.getName().equals(methodName)) {
        method = m;
        sameNameMethod++;
      }
    }
    // check inherited also but just public including those inherited.
    for (Method m : clazz.getMethods()) {
      if (m.getName().equals(methodName)) {
        if (method == null || !m.getDeclaringClass().equals(method.getDeclaringClass())) {
          method = m;
          sameNameMethod++;
        }
      }
    }
    if (sameNameMethod > 1) {
      throw new FlowDefinitionException("Can not identify method since there are multiple methods with the same name: "
          + getMethodReferenceAsString(method));
    }
    if (method == null) {
      throw new FlowDefinitionException(new NoSuchMethodException("Could not find method " + methodName + " in " + clazz));
    }
    return method;
  }

//  public static Method getMethod(Class<?> clazz, Method sample) {
//    Method method = null;
//    while (clazz != null) {
//      for (Method m : clazz.getDeclaredMethods()) {  // public/private and only of the current class.
//        if (m.getName().equals(sample.getName()) && m.getReturnType().isAssignableFrom(sample.getReturnType())
//            && Arrays.equals(m.getParameterTypes(), sample.getParameterTypes())) {
//          method = m;
//          break;
//        }
//      }
//      if (method == null) {
//        // check inherited also but just public including those inherited.
//        for (Method m : clazz.getMethods()) {    //
//          if (m.getName().equals(sample.getName()) && m.getReturnType().isAssignableFrom(sample.getReturnType())
//              && Arrays.equals(m.getParameterTypes(), sample.getParameterTypes())) {
//            method = m;
//            break;
//          }
//        }
//      }
//    }
//    if (method == null) {
//      throw new FlowDefinitionException(
//          new NoSuchMethodException("Could not find method " + sample.getName() + " in " + clazz));
//    }
//    if (!Modifier.isPublic(method.getModifiers())) {
//      throw new FlowDefinitionException("Method " + getMethodReferenceAsString(method) + " should be public.");
//    }
//    return method;
//  }
}
