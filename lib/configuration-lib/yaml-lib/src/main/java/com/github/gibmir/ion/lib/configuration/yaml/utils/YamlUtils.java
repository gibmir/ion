package com.github.gibmir.ion.lib.configuration.yaml.utils;

import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public final class YamlUtils {

  private YamlUtils() {
  }

  /**
   * Loads configuration as java properties.
   *
   * @param yaml        serializer
   * @param inputStream input
   * @return configuration map
   * @implNote From yaml:
   * <pre>{@code
   *  ion:
   *   config:
   *     example: 'demonstration'
   * }</pre>
   * should return:
   * <pre>{@code
   *  ion.config.example="demonstration"
   * }</pre>
   */
  public static Map<String, Object> loadAsJavaProperty(final Yaml yaml, final InputStream inputStream) {
    Map<String, Object> load = yaml.load(inputStream);
    Map<String, Object> configMap = new TreeMap<>();
    fillConfiguration(ConfigurationUtils.ROOT_PREFIX, configMap, load.get(ConfigurationUtils.ROOT_PREFIX));
    return configMap;
  }

  /**
   * Loads configuration as java properties.
   *
   * @param yaml        serializer
   * @param inputStream input
   * @return configuration map
   * @implNote From yaml:
   * <pre>{@code
   *  ion:
   *   config:
   *     example: 'demonstration'
   * }</pre>
   * should return:
   * <pre>{@code
   *  ion.config.example="demonstration"
   * }</pre>
   */
  public static Map<String, Object> loadAsJavaProperty(final Yaml yaml, final String inputStream) {
    Map<String, Object> load = yaml.load(inputStream);
    Map<String, Object> configMap = new TreeMap<>();
    fillConfiguration(ConfigurationUtils.ROOT_PREFIX, configMap, load.get(ConfigurationUtils.ROOT_PREFIX));
    return configMap;
  }

  /**
   * Recursively fill configuration map from yaml.
   *
   * @param parentKey     parent yaml key
   * @param configuration configuration map
   * @param yaml          yaml config
   */
  private static void fillConfiguration(final String parentKey, final Map<String, Object> configuration,
                                        final Object yaml) {
    if (yaml instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) yaml;
      Set<?> keys = map.keySet();
      for (Object key : keys) {
        Object value = map.get(key);
        fillConfiguration(parentKey + ConfigurationUtils.DOT_SEPARATOR + key.toString(), configuration, value);
      }
    } else {
      configuration.put(parentKey, yaml);
    }
  }
}
