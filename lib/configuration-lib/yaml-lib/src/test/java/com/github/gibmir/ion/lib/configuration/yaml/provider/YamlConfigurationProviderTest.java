package com.github.gibmir.ion.lib.configuration.yaml.provider;

import com.github.gibmir.ion.api.configuration.Configuration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class YamlConfigurationProviderTest {
  @Test
  void smoke() {
    YamlConfigurationProvider yamlConfigurationProvider = new YamlConfigurationProvider();
    Configuration yamlConfiguration = assertDoesNotThrow(yamlConfigurationProvider::provide);
    assertNotNull(yamlConfiguration);
  }
}
