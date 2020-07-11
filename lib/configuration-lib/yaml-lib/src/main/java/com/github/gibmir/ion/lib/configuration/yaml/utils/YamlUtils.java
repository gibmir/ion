package com.github.gibmir.ion.lib.configuration.yaml.utils;

import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class YamlUtils {

  private YamlUtils() {
  }

  public static Map<String, Object> loadAsJavaProperty(Yaml yaml, InputStream inputStream) {
    Map<String, Object> load = yaml.load(inputStream);
    Map<String, Object> configMap = new TreeMap<>();
    fillConfiguration(ConfigurationUtils.ROOT_PREFIX, configMap, load.get(ConfigurationUtils.ROOT_PREFIX));
    return configMap;
  }

  public static Map<String, Object> loadAsJavaProperty(Yaml yaml, String inputStream) {
    Map<String, Object> load = yaml.load(inputStream);
    Map<String, Object> configMap = new TreeMap<>();
    fillConfiguration(ConfigurationUtils.ROOT_PREFIX, configMap, load.get(ConfigurationUtils.ROOT_PREFIX));
    return configMap;
  }

  public static void fillConfiguration(String parentKey, Map<String, Object> configuration, Object yaml) {
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
