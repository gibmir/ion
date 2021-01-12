package com.github.gibmir.ion.api.configuration.properties;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.stub.ConfigurationTestStub;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;

import javax.json.bind.JsonbConfig;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.config.BinaryDataStrategy;
import javax.json.bind.config.PropertyNamingStrategy;
import javax.json.bind.config.PropertyOrderStrategy;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ConfigurationUtilsTest {

  public static final String TEST_BINARY_DATA_STRATEGY = BinaryDataStrategy.BYTE;
  public static final String TEST_DATE_FORMAT = JsonbDateFormat.DEFAULT_FORMAT;
  public static final String TEST_ENCODING = "UTF-8";
  public static final boolean TEST_FORMATTING = true;
  public static final Locale TEST_LOCALE = Locale.ENGLISH;
  public static final boolean TEST_NULL_VALUES = true;
  public static final String TEST_NAMING_STRATEGY = PropertyNamingStrategy.CASE_INSENSITIVE;
  public static final String TEST_ORDER_STRATEGY = PropertyOrderStrategy.ANY;
  public static final String TEST_CHARSET_VALUE = "UTF-8";

  @Test
  void createJsonbWithCorrectConfiguration() {
    HashMap<String, Object> configMap = new HashMap<>();
    configMap.put(ConfigurationUtils.JSONB_BINARY_DATA_STRATEGY, TEST_BINARY_DATA_STRATEGY);
    configMap.put(ConfigurationUtils.JSONB_DATE_FORMAT, TEST_DATE_FORMAT);
    configMap.put(ConfigurationUtils.JSONB_ENCODING, TEST_ENCODING);
    configMap.put(ConfigurationUtils.JSONB_FORMATTING, TEST_FORMATTING);
    configMap.put(ConfigurationUtils.JSONB_LOCALE, TEST_LOCALE.toString());
    configMap.put(ConfigurationUtils.JSONB_NULL_VALUES, TEST_NULL_VALUES);
    configMap.put(ConfigurationUtils.JSONB_PROPERTY_NAMING_STRATEGY, TEST_NAMING_STRATEGY);
    configMap.put(ConfigurationUtils.JSONB_PROPERTY_ORDER_STRATEGY, TEST_ORDER_STRATEGY);
    ConfigurationTestStub configuration = new ConfigurationTestStub(configMap);

    JsonbConfig jsonbConfig = assertDoesNotThrow(() -> ConfigurationUtils.readJsonbConfigFrom(configuration));
    Map<String, Object> jsonbConfigMap = jsonbConfig.getAsMap();
    assertThat(jsonbConfigMap.get(JsonbConfig.BINARY_DATA_STRATEGY), equalTo(TEST_BINARY_DATA_STRATEGY));
    assertThat(jsonbConfigMap.get(JsonbConfig.DATE_FORMAT), equalTo(TEST_DATE_FORMAT));
    assertThat(jsonbConfigMap.get(JsonbConfig.ENCODING), equalTo(TEST_ENCODING));
    assertThat(jsonbConfigMap.get(JsonbConfig.FORMATTING), equalTo(TEST_FORMATTING));
    assertThat(jsonbConfigMap.get(JsonbConfig.LOCALE), equalTo(TEST_LOCALE));
    assertThat(jsonbConfigMap.get(JsonbConfig.NULL_VALUES), equalTo(TEST_NULL_VALUES));
    assertThat(jsonbConfigMap.get(JsonbConfig.PROPERTY_NAMING_STRATEGY), equalTo(TEST_NAMING_STRATEGY));
    assertThat(jsonbConfigMap.get(JsonbConfig.PROPERTY_ORDER_STRATEGY), equalTo(TEST_ORDER_STRATEGY));
    assertDoesNotThrow(() -> ConfigurationUtils.createJsonbWith(configuration));
  }

  @Test
  void testReadCharsetFromCorrectConfiguration() {
    Configuration configurationTestStub = new ConfigurationTestStub(Map.of(ConfigurationUtils.REQUEST_CHARSET_PROPERTY,
      TEST_CHARSET_VALUE));
    Charset charset = ConfigurationUtils.readCharsetFrom(configurationTestStub);
    assertThat(charset, CoreMatchers.equalTo(Charset.forName(TEST_CHARSET_VALUE)));
  }

  @Test
  void testReadCharsetFromIncorrectConfiguration() {
    Configuration configurationTestStub = new ConfigurationTestStub(Map.of(ConfigurationUtils.REQUEST_CHARSET_PROPERTY,
      "INCORRECT"));
    assertThrows(IllegalArgumentException.class, () -> ConfigurationUtils.readCharsetFrom(configurationTestStub));
  }

  @Test
  void testReadCharsetFromEmptyConfig() {
    Configuration configurationTestStub = new ConfigurationTestStub(Collections.emptyMap());
    Charset charset = ConfigurationUtils.readCharsetFrom(configurationTestStub);
    assertThat(charset, CoreMatchers.equalTo(StandardCharsets.UTF_8));
  }
}
