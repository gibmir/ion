package com.github.gibmir.ion.api.configuration.provider;

import com.github.gibmir.ion.api.configuration.Configuration;

import java.util.ServiceLoader;

public interface ConfigurationProvider {
  /**
   * Loads through SPI configuration provider.
   *
   * @return configuration provider
   */
  static ConfigurationProvider load() {
    return ServiceLoader.load(ConfigurationProvider.class).findFirst().orElseThrow();
  }

  /**
   * Provides application configuration.
   *
   * @return application configuration
   * @throws com.github.gibmir.ion.api.configuration.exception.ConfigurationProvidingException if exception occurred
   *                                                                                           while trying to
   *                                                                                           provide config
   */
  Configuration provide();
}
