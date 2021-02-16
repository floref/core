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

package org.floref.core.flow.registry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.dsl.flow.data.FlowDefinition;
import org.floref.core.exception.FlowDefinitionException;
import org.floref.core.flow.build.FlowInstanceData;
import org.floref.core.flow.build.FlowInvocationHandler;

import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps track of all the flow instances.
 */
public class FlowRegistry {
  public static final String COLON = ":";
  private static final Log LOG = LogFactory.getLog(FlowRegistry.class);
  private static ConcurrentHashMap<String, FlowInstanceData> FLOWS = new ConcurrentHashMap<>();

  public static void registerNewFlow(FlowDefinition flowDefinition, Object flowInstance, FlowInvocationHandler flowInvocationHandler) {
    FlowInstanceData flowInstanceData = new FlowInstanceData();
    flowInstanceData.setFlow(flowInstance);
    flowInstanceData.setFlowClass(flowDefinition.getFlowClass());
    flowInstanceData.add(flowDefinition);
    flowInvocationHandler.setFlowInstanceData(flowInstanceData);
    flowInstanceData.setFlowInvocationHandler(flowInvocationHandler); // hmm


    if (flowDefinition.getId().contains(COLON)) {
      FLOWS.put(extractFlowGroupName(flowDefinition.getId()), flowInstanceData);
      FLOWS.put(flowDefinition.getId(), flowInstanceData);
    } else {
//      FLOWS.put(flowDefinition.getFlowClassCanonicalName(), flowInstanceData);
      FLOWS.put(extractFlowGroupName(flowDefinition.getId()), flowInstanceData);
    }
  }

//  public static Class getFlowInterface(Class flowClass) {
//    if (!flowClass.isInterface()) {  // It may be the proxy.
//      Class[] interfaces = flowClass.getInterfaces();
//      if (interfaces.length == 1) {
//        flowClass = interfaces[0];
//      }
//    }
//    return flowClass;
//  }

  /**
   * @param flowClass is the flow interface class.
   * @return a flow instance if a flow definition was done for at least a method from that interface.
   */
  public static <T> T getFlow(Class<T> flowClass) {
//    flowClass = getFlowInterface(flowClass);
    FlowInstanceData flowInstanceData = FLOWS.get(flowClass.getCanonicalName());
    if (flowInstanceData != null) {
      return (T) flowInstanceData.getFlow();
    }
    return null;
  }

  private static String extractFlowGroupName(String id) {
    String[] parts = id.split(COLON);
    return parts[0];
  }

  public static <T> T getFlow(Class<T> flowClass, String id) {
    id = extractFlowGroupName(id);
    FlowInstanceData flowInstanceData = FLOWS.get(id);
    if (flowInstanceData != null) {
      return (T) flowInstanceData.getFlow();
    }
    return null;
  }

  public static FlowInstanceData getFlowInstanceData(Class flowClass) {
    return FLOWS.get(flowClass.getCanonicalName());
  }

  public static FlowInstanceData getFlowInstanceData(Object flowInstance) {
    if (!isFlow(flowInstance)) {
      return null;
    }
    return getFlowInstanceData(getFlowInterface(flowInstance));
  }

  /**
   * @return an iterator that can be used to iterate on all flows instance data.
   */
  public static Iterator<FlowInstanceData> allFlowsIterator() {
    return FLOWS.values().iterator();
  }

  public static Object getFlow(FlowDefinition flowDefinition) {
    return getFlow(flowDefinition.getFlowClass(), flowDefinition.getId());
  }

  public static <T> boolean isFlowInstanceRegistered(FlowDefinition flowDefinition) {
    return FLOWS.containsKey(flowDefinition.getFlowClassCanonicalName());
  }

  public static FlowInstanceData getFlowInstanceData(FlowDefinition flowDefinition) {
    return FLOWS.get(flowDefinition.getFlowClassCanonicalName());
  }

  public static void deleteAll() {
    LOG.debug("Removing all flows.");
    FLOWS.clear();  // Should the existing flows get invalidated by changing their implementation to invalid?.
  }

  public static boolean isFlow(Object flow) {
    // Could iterate on the FLOWS map or could check proxy invocation handler.
    if (flow instanceof Proxy && Proxy.getInvocationHandler(flow) instanceof FlowInvocationHandler) {
      return true;
    }
    return false;
  }

  /**
   * Returns the flow interface class. A <code>flow.getClass()</code> will return the proxy class.
   */
  public static Class getFlowInterface(Object flow) {
    if (Proxy.isProxyClass(flow.getClass())) {
      return ((FlowInvocationHandler) Proxy.getInvocationHandler(flow)).getFlowInstanceData().getFlowClass();
    }
    return flow.getClass();
  }
}
