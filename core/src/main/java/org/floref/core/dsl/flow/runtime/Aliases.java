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

package org.floref.core.dsl.flow.runtime;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.config.FlowConfiguration;
import org.floref.core.config.injector.BeanInjector;
import org.floref.core.dsl.command.MethodReferenceCommand;
import org.floref.core.dsl.flow.FlowDefinition;
import org.floref.core.dsl.flow.Flows;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.build.FlowInstanceData;
import org.floref.core.flow.reference.LambdaMeta;
import org.floref.core.flow.reference.LambdaMetaBuilder;
import org.floref.core.flow.reference.MethodReference;
import org.floref.core.flow.reference.Methods;
import org.floref.core.flow.reference.ParamBiConsumer;
import org.floref.core.flow.reference.ParamConsumer;
import org.floref.core.flow.reference.ParamHeptaConsumer;
import org.floref.core.flow.reference.ParamHexaConsumer;
import org.floref.core.flow.reference.ParamPentaConsumer;
import org.floref.core.flow.reference.ParamSupplier;
import org.floref.core.flow.reference.ParamTetraConsumer;
import org.floref.core.flow.reference.ParamTriConsumer;
import org.floref.core.flow.reference.ParamVoidConsumerVoidSupplier;
import org.floref.core.flow.registry.FlowRegistry;

/**
 * Represents a String alias for a method reference. The aliases are of 2 different types:
 * 1. Class targeting:  Class::method
 * 2. Instance targeting: <class instance>::method
 *
 * 1. The Class targeting aliases are the most common. They can accommodate:
 *  - flows instance: <code>FlowClass::method</code>
 *  - static methods: <code>Class::method</code>
 *  - instances that are injectable via BeanInjector.java (e.g. Spring beans) in the form of
 *  <code>BeanClass::method</code>. Thus a class targeting alias can target class instances eventually.
 *
 * 2. The instance only targeting is used when the instance is not a flow, static method or injectable bean.
 *    For example a flow that uses pojo targets for method references will use those pojo instances.
 *    <code>

 *
 *    </code>
 *    When export a flow that uses instances that were not injected via the BeanInjector they will appear under the
 *    aliases section in the JSON.
 *    The aliases will be generated automatically in the form of "
 */
public class Aliases {
  private static final Log LOG = LogFactory.getLog(Aliases.class);

  private static ConcurrentHashMap<String, LambdaMeta> ALIASES = new ConcurrentHashMap<>();  // alias -> lamdaMeta


  /**
   * Returns the target bean or target flow lambda meta. The provided lambda meta might be on a proxy.
   * @param lambdaMeta
   * @return
   */
  private static LambdaMeta getTargetLambdaMeta(LambdaMeta lambdaMeta) {
    // Check that the class is obtainable via BeanInjector.
    Object target = lambdaMeta.getTarget();

    // Non static method the target is an instance which may have been obtained as a flow or via bean injector.
    if (target!= null) {
      if (FlowRegistry.isFlow(target)) {
        // Change the lambdaMeta so that the target is the flow interface class and not the proxy.
        lambdaMeta = LambdaMetaBuilder.getLambdaMeta(FlowRegistry.getFlowInterface(target), lambdaMeta.getLambdaMethod());
      } else {
        // Needs to be a bean.
        BeanInjector beanInjector = FlowConfiguration.getBeanInjector();
        Class beanTargetClass = beanInjector.getTargetClass(target);
        Object bean = beanInjector.getBean(beanTargetClass);
        if (bean != null) {
          lambdaMeta = LambdaMetaBuilder.getLambdaMeta(beanTargetClass, lambdaMeta.getLambdaMethod());
        } else {
          return null;
        }
      }
    }
    return lambdaMeta;
  }
  /**
   * Common alias creator.
   * @param alias
   * @param lambdaMeta
   */
  private static void add(String alias, LambdaMeta lambdaMeta) {
    if (alias.contains("::")) {
      throw new FlowDefinitionException("Alias '" + alias + "' must not contain '::'");
    }
    if (ALIASES.contains(alias)) {
      LOG.info("Overwriting alias " + alias);
    }
    LambdaMeta targetLambdaMeta = getTargetLambdaMeta(lambdaMeta);
    if (targetLambdaMeta == null) {
      // Runtime aliases are a bit hard to generate statically: 1#class#method, 2#class#method if 2 different
      // instances are used in the same flow definition leading to hard to follow definitions.
      throw new FlowDefinitionException("Unsupported alias \"" + alias + "\" for "
              + lambdaMeta.getMethodReferenceAsString() + " because class instance could not be retrieved via " +
              "BeanInjector");
    }

    ALIASES.put(alias, targetLambdaMeta);
  }

  public static String getAlias(LambdaMeta lambdaMeta) {
    lambdaMeta = getTargetLambdaMeta(lambdaMeta);
    for (Map.Entry<String, LambdaMeta> entry : ALIASES.entrySet()) {
      LambdaMeta aliasLambdaMeta = entry.getValue();
      if (aliasLambdaMeta.equals(lambdaMeta)) {
        return entry.getKey();
      }
    }
    return null;
  }


  /**
   * Adds an alias for the given string which defines a method reference.
   * @param alias which can be used in the JSON for create/update/export of flows.
   * @param methodReferenceAsString "Class:method"
   */
  public static void add(String alias, String methodReferenceAsString) {
    add(alias, LambdaMetaBuilder.getLambdaMeta(methodReferenceAsString));
  }


  /**
   * Adds an alias for the given method reference.
   * @param alias which can be used in the JSON for create/update/export of flows.
   * @param methodReference
   */
  public static void add(String alias, MethodReference methodReference) {
    LambdaMeta lambdaMeta = LambdaMetaBuilder.getLambdaMeta(methodReference);
    add(alias, lambdaMeta);
  }

  public static void add(String alias, MethodReferenceCommand methodReferenceCommand) {
    add(alias, methodReferenceCommand.getLambdaMeta());
  }

  /**
   * Alias a method reference. It is recommended to use the Class as a target for the method reference:
   * <code>Class::method</code> in order to make the flow editable without having to preload aliases.
   * A 'Class::method' reference can refer to a flow instance, a static method or a bean instance via the BeanInjector.
   * If the target is an instance: <code>classInstance::method</code> and classInstance is not a flow then it will
   * be stored as a runtime alias that depends on an instance. This means that the runtime alias will have to be
   * preloaded by the system using Aliases.alias("aliasName", classInstance::method) or flow DSL before importing a flow
   * that uses it.
   *
   * @param alias
   * @param methodReferenceAsString
   */
  public static void alias(String alias, String methodReferenceAsString) {
    add(alias, methodReferenceAsString);
  }

  // For methods that do not have arguments and return void.
  public static void alias(String alias, ParamVoidConsumerVoidSupplier consumer) {
    add(alias, consumer);
  }

  public static <T> void alias(String alias, ParamSupplier<T> consumer) {
    add(alias, consumer);
  }

  public static <T> void alias(String alias, ParamConsumer<T> consumer) {
    add(alias, consumer);
  }

  public static <T, U> void alias(String alias, ParamBiConsumer<T, U> consumer) {
    add(alias, consumer);
  }

  public static <T, U, V> void alias(String alias, ParamTriConsumer<T, U, V> consumer) {
    add(alias, consumer);
  }

  public static <T, U, V, X> void alias(String alias, ParamTetraConsumer<T, U, V, X> consumer) {
    add(alias, consumer);
  }

  public static <T, U, V, X, Y> void alias(String alias, ParamPentaConsumer<T, U, V, X, Y> consumer) {
    add(alias, consumer);
  }

  public static <T, U, V, X, Y, Z> void alias(String alias, ParamHexaConsumer<T, U, V, X, Y, Z> consumer) {
    add(alias, consumer);
  }

  public static <T, U, V, X, Y, Z, A> void alias(String alias, ParamHeptaConsumer<T, U, V, X, Y, Z, A> consumer) {
    add(alias, consumer);
  }

  public static void delete(String alias) {
    ALIASES.remove(alias);
  }

  public static void deleteAll() {
    ALIASES.clear();
  }

  public static LambdaMeta getAliasLambdaMeta(String alias) {
    return ALIASES.get(alias);
  }



  public static Map<String, LambdaMeta> getAliases() {
    return ALIASES;
  }


  public static Map<String, String> getAliasesForExport() {
    //    Map aliases = new LinkedHashMap();
//    for (Map.Entry<String, LambdaMeta> entry : ALIASES.entrySet()) {
//      LambdaMeta lambdaMeta = entry.getValue();
//      Object target = lambdaMeta.getTarget();
//
//      if (target == null) {  // static method, flow reference or will be obtained via BeanInjector.
//        aliases.put(entry.getKey(), lambdaMeta.getMethodReferenceAsString());
//      } else { // The target is an instance which may have been obtained as a flow or via bean injector.
//        FlowInstanceData flowInstanceData = FlowRegistry.getFlowInstanceData(target);
//        boolean isFlow = flowInstanceData != null;
//        if (isFlow) {
//          // If a flow then replace with "FlowClass::method".
//          FlowDefinition flowDefinition = flowInstanceData.getFlowDefinitionFromMethod(lambdaMeta.getLambdaMethod());
//          if (flowDefinition == null) {
//            throw new FlowDefinitionException("Flow not yet defined for " + Methods
//                .getMethodReferenceAsString(flowInstanceData.getFlowClass(), lambdaMeta.getLambdaMethod()));
//          } else {
//            aliases.put(entry.getKey(), flowDefinition.getId());
//          }
//        } else {
//          // If a bean then use refer it by class.
//          Object bean = FlowConfiguration.getBeanInjector().getBean(lambdaMeta.getLambdaClass());
//          if (bean != null) {
//            aliases.put(entry.getKey(), lambdaMeta.getMethodReferenceAsString());
//          } else {
//            // Runtime aliases are a bit hard to generate statically: 1#class#method, 2#class#method if 2 different
//            // instances are used in the same flow definition leading to hard to follow definitions.
//            throw new FlowDefinitionException("Should not get here.");
//          }
//        }
//      }
//    }
//    return aliases;
    Map aliases = new LinkedHashMap();
    for (Map.Entry<String, LambdaMeta> entry : ALIASES.entrySet()) {
      LambdaMeta lambdaMeta = entry.getValue();
      aliases.put(entry.getKey(), lambdaMeta.getMethodReferenceAsString());
    }
    return aliases;
  }


}
