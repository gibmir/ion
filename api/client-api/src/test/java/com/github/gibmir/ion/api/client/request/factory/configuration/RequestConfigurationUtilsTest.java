package com.github.gibmir.ion.api.client.request.factory.configuration;

import com.github.gibmir.ion.api.client.factory.configuration.RequestConfigurationUtils;
import com.github.gibmir.ion.api.client.request.factory.configuration.stub.ConfigurationTestStub;
import com.github.gibmir.ion.api.configuration.Configuration;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestConfigurationUtilsTest {

  public static final String TEST_CHARSET_VALUE = "UTF-8";

  @Test
  void testReadCharsetFromCorrectConfiguration() {
    Configuration configurationTestStub = new ConfigurationTestStub(Map.of(RequestConfigurationUtils.REQUEST_CHARSET_PROPERTY,
      TEST_CHARSET_VALUE));
    Charset charset = RequestConfigurationUtils.readCharsetFrom(configurationTestStub);
    assertThat(charset, equalTo(Charset.forName(TEST_CHARSET_VALUE)));
  }

  @Test
  void testReadCharsetFromIncorrectConfiguration() {
    Configuration configurationTestStub = new ConfigurationTestStub(Map.of(RequestConfigurationUtils.REQUEST_CHARSET_PROPERTY,
      "INCORRECT"));
    assertThrows(IllegalArgumentException.class, () -> RequestConfigurationUtils.readCharsetFrom(configurationTestStub));
  }

  @Test
  void testReadCharsetFromEmptyConfig() {
    Configuration configurationTestStub = new ConfigurationTestStub(Collections.emptyMap());
    Charset charset = RequestConfigurationUtils.readCharsetFrom(configurationTestStub);
    assertThat(charset, equalTo(StandardCharsets.UTF_8));
  }
}
