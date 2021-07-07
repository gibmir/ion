package com.github.gibmir.ion.lib.configuration.yaml;

import com.github.gibmir.ion.api.configuration.Configuration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class YamlConfiguration implements Configuration {
  private final Map<String, Object> properties;

  public YamlConfiguration(final Map<String, Object> properties) {
    this.properties = properties;
  }

  @Override
  public final <T> T getValue(final String propertyName, final Class<T> type) {
    return type.cast(properties.get(propertyName));
  }

  @Override
  public final <T> Optional<T> getOptionalValue(final String propertyName, final Class<T> type) {
    return Optional.ofNullable(getValue(propertyName, type));
  }

  @Override
  public final <T> List<T> getValues(final String propertyName, final Class<T> genericType) {
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
  private static <T> List<T> uncheckedListCast(final Object list) {
    return (List<T>) list;
  }
}
