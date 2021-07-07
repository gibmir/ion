package com.github.gibmir.ion.lib.netty.common.http.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;

public final class NettyHttpConfigurationUtils {
  //aggregator
  //int
  public static final String NETTY_SERVER_HTTP_MAX_CONTENT_LENGTH = "ion.netty.server.http.max.content.length";
  public static final int DEFAULT_AGGREGATOR_MAX_CONTENT_LENGTH = 1048576;

  private NettyHttpConfigurationUtils() {
  }

  /**
   * Resolves max content length from application config.
   *
   * @param configuration application config
   * @return resolved max content length
   */
  public static int resolveMaxContentLength(final Configuration configuration) {
    return configuration.getOptionalValue(NETTY_SERVER_HTTP_MAX_CONTENT_LENGTH, Integer.class)
      .orElse(DEFAULT_AGGREGATOR_MAX_CONTENT_LENGTH);
  }
}
