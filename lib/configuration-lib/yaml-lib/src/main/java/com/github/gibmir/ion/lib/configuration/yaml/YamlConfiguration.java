package com.github.gibmir.ion.lib.configuration.yaml;

import com.github.gibmir.ion.api.configuration.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class YamlConfiguration implements Configuration {
  private final Map<String, Object> properties;

  public YamlConfiguration(Map<String, Object> properties) {
    this.properties = properties;
  }

  @Override
  public <T> T getValue(String propertyName, Class<T> type) {
    return type.cast(properties.get(propertyName));
  }

  @Override
  public <T> Optional<T> getOptionalValue(String propertyName, Class<T> type) {
    return Optional.ofNullable(getValue(propertyName, type));
  }

  @Override
  public <T> List<T> getValues(String propertyName, Class<T> genericType) {
    Object value = properties.get(propertyName);
    if (value == null) {
      return Collections.emptyList();
    } else if (value instanceof List) {
      return uncheckedListCast(value);
    } else {
      return Collections.singletonList(genericType.cast(value));
    }
  }

  @SuppressWarnings("unchecked")
  private static <T> List<T> uncheckedListCast(Object list) {
    return (List<T>) list;
  }
}
