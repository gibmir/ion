package com.github.gibmir.ion.api.configuration;

import java.util.List;
import java.util.Optional;

public interface Configuration {

  <T> T getValue(String propertyName, Class<T> type);

  <T> Optional<T> getOptionalValue(String propertyName, Class<T> type);

  <T> List<T> getValues(String propertyName, Class<T> genericType);
}
