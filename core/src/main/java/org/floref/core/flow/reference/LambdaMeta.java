/*
 * Copyright 2020 the original author  or authors.
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.flow.annotation.FlowVar;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

/**
 * Lambda meta util.
 * @param <T>
 * @author Cristian Donoiu
 */
public class LambdaMeta<T> {
  private static final Log LOG = LogFactory.getLog(LambdaMeta.class);

  protected T target;
  protected Class<T> lambdaClass;
  protected Method lambdaMethod;
  // In case of beans the method reference might be on an interface but we want the implementation bean that may have
  // the actual annotations.
  protected Class lambdaActualClass;
  protected Method lambdaActualMethod;

  // Method meta.
  protected Class returnType;
  protected FlowVar returnFlowVar;
  protected List<MethodParameter> parameters;

  public static class MethodParameter {
    Class clazz;
    FlowVar flowVar; // null is the parameters is not a FlowVar.
    int position;

    public boolean isFlowVar() {
      return flowVar != null;
    }

    public Class getClazz() {
      return clazz;
    }

    public void setClazz(Class clazz) {
      this.clazz = clazz;
    }

    public FlowVar getFlowVar() {
      return flowVar;
    }

    public void setFlowVar(FlowVar flowVar) {
      this.flowVar = flowVar;
    }

    public int getPosition() {
      return position;
    }

    public void setPosition(int position) {
      this.position = position;
    }
  }
  public Class<T> getLambdaClass() {
    return lambdaClass;
  }

  public void setLambdaClass(Class<T> lambdaClass) {
    this.lambdaClass = lambdaClass;
    lambdaActualClass = lambdaClass;  // At startup/build time both are the same.
  }

  public Method getLambdaMethod() {
    return lambdaMethod;
  }

  public void setLambdaMethod(Method lambdaMethod) {
    this.lambdaMethod = lambdaMethod;
    lambdaActualMethod = lambdaMethod;
  }

  public Class getLambdaActualClass() {
    return lambdaActualClass;
  }

  public void setLambdaActualClass(Class lambdaActualClass) {
    this.lambdaActualClass = lambdaActualClass;
  }

  public Method getLambdaActualMethod() {
    return lambdaActualMethod;
  }

  public void setLambdaActualMethod(Method lambdaActualMethod) {
    this.lambdaActualMethod = lambdaActualMethod;
  }

  public Class getReturnType() {
    return returnType;
  }

  public void setReturnType(Class returnType) {
    this.returnType = returnType;
  }

  public FlowVar getReturnFlowVar() {
    return returnFlowVar;
  }

  public void setReturnFlowVar(FlowVar returnFlowVar) {
    this.returnFlowVar = returnFlowVar;
  }

  public List<MethodParameter> getParameters() {  // The actual parameters.
    return parameters;
  }

  public void setParameters(
      List<MethodParameter> parameters) {
    this.parameters = parameters;
  }

  public T getTarget() {
    return target;
  }

  public void setTarget(T target) {
    this.target = target;
  }

  public String getMethodReferenceAsString() {
    return lambdaClass.getCanonicalName() + "::" + lambdaMethod.getName();
  }
  public String getActualMethodReferenceAsString() {
    return lambdaActualClass.getCanonicalName() + "::" + lambdaActualMethod.getName();
  }

  /**
   * Retrieves method return type, parameter types and FlowVar annotation info.
   * @param method
   */
  public void buildMethodMeta(Method method) {
    returnType = method.getReturnType();

    Annotation[] methodAnnotations = method.getAnnotations();
    for (int i = 0; i<methodAnnotations.length; i++) {
      if (methodAnnotations[i] instanceof FlowVar) {
        returnFlowVar = (FlowVar)methodAnnotations[i];
      }
    }

    Parameter[] methodParameters = method.getParameters();
    parameters = new ArrayList<>();
    for (int i = 0; i<methodParameters.length; i++) {
      Parameter parameter = methodParameters[i];
      MethodParameter methodParameter = new MethodParameter();
      methodParameter.clazz = Methods.primitiveToWrapper(parameter.getType());
      methodParameter.position = i;
      Annotation[] paramAnnotations = parameter.getAnnotations();
      for (int j = 0; j<paramAnnotations.length; j++) {
        if (paramAnnotations[j] instanceof FlowVar) {
          methodParameter.flowVar = (FlowVar)paramAnnotations[j];
        }
      }
      parameters.add(methodParameter);
    }
  }

  @Deprecated   // Is thid needed?
  public void refreshActualMethod(Method method) {
    if (lambdaActualMethod != method) {
      LOG.debug("Loading new method " + method.getDeclaringClass().getSimpleName() + "#" + method.getName());
      lambdaActualMethod = method;
      if (!Modifier.isPublic(method.getModifiers())) {
        method.setAccessible(true);
      }
      buildMethodMeta(method);
    }
  }

  public boolean equals(LambdaMeta lambdaMeta) {
    if (getLambdaClass().equals(lambdaMeta.getLambdaClass()) && getLambdaMethod().getName().equals(lambdaMeta.getLambdaMethod().getName())) {
      return true;
    }
    return false;
  }

}
