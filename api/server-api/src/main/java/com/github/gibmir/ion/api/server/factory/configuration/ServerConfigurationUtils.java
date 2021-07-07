package com.github.gibmir.ion.api.server.factory.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;

import java.nio.charset.Charset;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public final class ServerConfigurationUtils {
  //charset
  public static final String REQUEST_CHARSET_PROPERTY = ROOT_PREFIX + ".server.request.charset";

  private ServerConfigurationUtils() {
  }

  /**
   * Creates charset with specified config.
   *
   * @param configuration application config
   * @return charset
   */
  public static Charset createCharsetWith(final Configuration configuration) {
    return configuration.getOptionalValue(ServerConfigurationUtils.REQUEST_CHARSET_PROPERTY, String.class)
      .map(Charset::forName).orElse(ConfigurationUtils.DEFAULT_CHARSET);
  }
}
