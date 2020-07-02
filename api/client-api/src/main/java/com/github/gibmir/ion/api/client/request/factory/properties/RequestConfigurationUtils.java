package com.github.gibmir.ion.api.client.request.factory.properties;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;

import java.nio.charset.Charset;
import java.time.Duration;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public class RequestConfigurationUtils {
  private RequestConfigurationUtils() {
  }

  //timeout
  public static final String REQUEST_TIMEOUT_PROPERTY = ROOT_PREFIX + ".client.request.timeout";
  //charset
  public static final String REQUEST_CHARSET_PROPERTY = ROOT_PREFIX + ".client.request.charset";
  public static final Duration DEFAULT_TIMEOUT = Duration.ofMinutes(1);

  public static Charset createCharsetWith(Configuration configuration) {
    return configuration.getOptionalValue(RequestConfigurationUtils.REQUEST_CHARSET_PROPERTY, String.class)
      .map(Charset::forName).orElse(ConfigurationUtils.DEFAULT_CHARSET);
  }

  public static Duration createTimeoutWith(Configuration configuration) {
    return configuration.getOptionalValue(RequestConfigurationUtils.REQUEST_TIMEOUT_PROPERTY, String.class)
      .map(Long::valueOf)
      .map(Duration::ofSeconds)
      .orElse(RequestConfigurationUtils.DEFAULT_TIMEOUT);
  }
}
