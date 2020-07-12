package com.github.gibmir.ion.api.client.request.factory.configuration.stub;

import com.github.gibmir.ion.api.configuration.Configuration;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ConfigurationTestStub implements Configuration {
  private final Map<String, Object> configurationMap;

  public ConfigurationTestStub(Map<String, Object> configurationMap) {
    this.configurationMap = configurationMap;
  }

  @Override
  public <T> T getValue(String propertyName, Class<T> type) {
    return type.cast(configurationMap.get(propertyName));
  }

  @Override
  public <T> Optional<T> getOptionalValue(String propertyName, Class<T> type) {
    return Optional.ofNullable(getValue(propertyName, type));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> List<T> getValues(String propertyName, Class<T> genericType) {
    return (List<T>) configurationMap.get(propertyName);
  }
}
