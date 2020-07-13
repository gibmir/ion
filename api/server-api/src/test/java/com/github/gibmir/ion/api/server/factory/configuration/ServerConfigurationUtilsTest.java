package com.github.gibmir.ion.api.server.factory.configuration;

import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.server.environment.stub.ConfigurationTestStub;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Collections;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ServerConfigurationUtilsTest {

  public static final String CORRECT_CHARSET = "UTF-8";

  @Test
  void testCreateCharsetWithCorrectConfiguration() {
    HashMap<String, Object> configMap = new HashMap<>();
    configMap.put(ServerConfigurationUtils.REQUEST_CHARSET_PROPERTY, CORRECT_CHARSET);
    ConfigurationTestStub configuration = new ConfigurationTestStub(configMap);

    Charset charset = assertDoesNotThrow(() -> ServerConfigurationUtils.createCharsetWith(configuration));

    assertThat(charset, equalTo(StandardCharsets.UTF_8));
  }

  public static final String INCORRECT_CHARSET = "INCORRECT";

  @Test
  void testCreateCharsetWithIncorrectConfiguration() {
    HashMap<String, Object> configMap = new HashMap<>();
    configMap.put(ServerConfigurationUtils.REQUEST_CHARSET_PROPERTY, INCORRECT_CHARSET);
    ConfigurationTestStub configuration = new ConfigurationTestStub(configMap);
    assertThrows(UnsupportedCharsetException.class, () -> ServerConfigurationUtils.createCharsetWith(configuration));
  }

  @Test
  void testCreateCharsetWithEmptyConfiguration() {
    ConfigurationTestStub configuration = new ConfigurationTestStub(Collections.emptyMap());

    Charset charset = assertDoesNotThrow(() -> ServerConfigurationUtils.createCharsetWith(configuration));

    assertThat(charset, equalTo(ConfigurationUtils.DEFAULT_CHARSET));
  }
}
