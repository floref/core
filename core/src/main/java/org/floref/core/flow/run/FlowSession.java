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

package org.floref.core.flow.run;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Keeps flow run session data.
 *
 * @author Cristian Donoiu
 */
public class FlowSession {
  // Important to be initialised with null so as to know which is the originator parent flow.
  private static ThreadLocal<Map<String, Object>> threadLocal = ThreadLocal.withInitial(() -> null);

  /**
   * This resets the session but <b>it will not clear the map</b> so that any users of it can continue to use it.
   */
  public static void clearSession() {
    threadLocal.set(null);   // do not touch the map, since forks or futures may be using it.
  }

  public static boolean isGate() {
    return get() == null;
  }

  public static void initIfNeeded() {
    if (get() == null) {
      threadLocal.set(new ConcurrentHashMap<>());
    }
  }

  /**
   * Used when copying a session to another thread.
   *
   * @param session
   */
  public static void set(Map session) {
    threadLocal.set(session);
  }

  public static Map get() {
    return threadLocal.get();
  }

  public static Object get(String key) {
    return get().get(key);
  }

  public static <T> T get(String key, Class<T> clazz) {
    return (T) get().get(key);
  }

  public static void set(String key, Object value) {
    initIfNeeded();
    get().put(key, value);
  }

  public static boolean isEmpty() {
    Map session = get();
    return session == null || session.isEmpty();
  }
}
