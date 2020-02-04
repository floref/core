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

package org.floref.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.floref.core.config.injector.BeanInjector;
import org.floref.core.config.injector.ConfigInjector;
import org.floref.core.config.consumer.MetricsConsumer;
import org.floref.core.dsl.flow.From;

/**
 * Floref configuration class.
 *
 * @author Cristian Donoiu
 */
public class FlowConfiguration {
  private static final Log LOG = LogFactory.getLog(From.class);
  public static final String CONFIG_FILE = "/floref.properties";
  public static final String commonThreadPoolMaxSize = "flow.run.thread.pool.max.size";

  private static class FlowConfigurationHolder {
    static final FlowConfiguration CONFIG = new FlowConfiguration();
  }

  /**
   * Get the entire configuration.
   * @return the configuration that you can use to set get/set particular properties.
   */
  protected static FlowConfiguration get() {
    return FlowConfigurationHolder.CONFIG;
  }

  protected boolean loaded;
  protected BeanInjector beanInjector = new BeanInjector() {
    @Override
    public Object getBean(Class beanClass) {
      return null;
    }
  };
  protected ConfigInjector configInjector;
  protected List<MetricsConsumer> metricsConsumerList = new CopyOnWriteArrayList();
  private Properties config = new Properties();

  /**
   * Returns the configuration value for the given key. First looks in the configinjector, then it looks in the
   * floref.properties classpath file.
   * @param key
   * @return the config value.
   */
  protected String getConfig(String key) {
    loadIfNeeded();
    String value = null;
    if (configInjector != null) { // Try first the config injector.
      value = configInjector.getProperty(key);
    }
    if (value == null) {
      value = config.getProperty(key);
    }
    return value;
  }

  public static String get(String key) {
    return get().getConfig(key);
  }

  public static int getIntConfig(String key) {
    return Integer.parseInt(get().getConfig(key));
  }

  protected void loadIfNeeded() {
    if (!loaded) {
      try (InputStream inputStream = FlowConfiguration.class.getResourceAsStream(CONFIG_FILE)) {
        if (inputStream == null) {
          return;
        }
        // Some defaults first.
        config.put(commonThreadPoolMaxSize, 32);

        // The merge over properties from file.
        config.load(inputStream);
      } catch (IOException e) {
        LOG.error(e.getMessage(), e);
      }
      loaded = true;
    }
  }


  /**
   * Set bean injector. Called by spring integration or the user in order to set a bean provider when method reference
   * with the target a Class are used in a flow. For example given a call like <b>.to(UserService::createUser)</b>
   *
   * If UserService is not a flow class and the createUser is not a static method then the target instance on which the
   * method will be run will be obtained from the BeanInjector that is set here.
   */
  public static void setBeanInjector(BeanInjector beanInjector) {
    get().beanInjector = beanInjector;
  }

  public static BeanInjector getBeanInjector() {
    return get().beanInjector;
  }

  public static ConfigInjector getConfigInjector() {
    return get().configInjector;
  }

  public static void setConfigInjector(ConfigInjector configInjector) {
    get().configInjector = configInjector;
  }

  public static void addMetricsConsumer(MetricsConsumer metricsConsumer) {
    if (!get().metricsConsumerList.contains(metricsConsumer)) {
      get().metricsConsumerList.add(metricsConsumer);
    }
  }
  public static void removeMetricsConsumer(MetricsConsumer metricsConsumer) {
    get().metricsConsumerList.remove(metricsConsumer);
  }

  public static List<MetricsConsumer> getMetricsConsumerList() {
    return get().metricsConsumerList;
  }
}
