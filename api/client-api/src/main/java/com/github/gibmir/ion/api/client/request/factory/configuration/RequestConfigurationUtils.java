package com.github.gibmir.ion.api.client.request.factory.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;

import java.nio.charset.Charset;
import java.time.Duration;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public class RequestConfigurationUtils {
  private RequestConfigurationUtils() {
  }

  //charset
  public static final String REQUEST_CHARSET_PROPERTY = ROOT_PREFIX + ".client.request.charset";

  public static Charset readCharsetFrom(Configuration configuration) {
    return configuration.getOptionalValue(RequestConfigurationUtils.REQUEST_CHARSET_PROPERTY, String.class)
      .map(Charset::forName).orElse(ConfigurationUtils.DEFAULT_CHARSET);
  }
}
