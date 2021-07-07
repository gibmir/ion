package com.github.gibmir.ion.api.configuration;

import java.util.List;
import java.util.Optional;

public interface Configuration {
  /**
   * @param propertyName property name
   * @param type         property type
   * @param <T>          generic property type
   * @return value of specified type for specified key
   */
  <T> T getValue(String propertyName, Class<T> type);

  /**
   * @param propertyName property name
   * @param type         property type
   * @param <T>          generic property type
   * @return optional value of specified type for specified key
   */
  <T> Optional<T> getOptionalValue(String propertyName, Class<T> type);

  /**
   * @param propertyName property name
   * @param genericType  list generic type
   * @param <T>          generic property type
   * @return values of specified type for specified key
   */
  <T> List<T> getValues(String propertyName, Class<T> genericType);
}
