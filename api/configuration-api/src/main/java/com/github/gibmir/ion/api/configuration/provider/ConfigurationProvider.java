package com.github.gibmir.ion.api.configuration.provider;

import com.github.gibmir.ion.api.configuration.Configuration;

import java.util.ServiceLoader;

public interface ConfigurationProvider {
  static ConfigurationProvider load() {
    return ServiceLoader.load(ConfigurationProvider.class).findFirst().orElseThrow();
  }
  Configuration provide();
}
