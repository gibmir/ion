package com.github.gibmir.ion.lib.netty.server.http.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public final class NettyHttpServerConfigurationUtils {
  private NettyHttpServerConfigurationUtils() {
  }

  //decoder
  //int
  public static final String NETTY_SERVER_HTTP_MAX_INITIAL_LENGTH =
    ROOT_PREFIX + "netty.server.http.max.initial.length";
  public static final int DEFAULT_MAX_INITIAL_LENGTH = 4096;

  public static final String NETTY_SERVER_HTTP_MAX_HEADER_SIZE =
    ROOT_PREFIX + "netty.server.http.max.header.size";
  public static final int DEFAULT_MAX_HEADER_SIZE = 8192;

  public static final String NETTY_SERVER_HTTP_MAX_CHUNK_SIZE =
    ROOT_PREFIX + "netty.server.http.max.chunk.size";
  public static final int DEFAULT_MAX_CHUNK_SIZE = 8192;

  /**
   * @param configuration application config
   * @return http request decoder config
   */
  public static HttpRequestDecoderConfiguration resolveDecoderConfig(final Configuration configuration) {
    Integer maxInitialLength = configuration.getOptionalValue(NETTY_SERVER_HTTP_MAX_INITIAL_LENGTH, Integer.class)
      .orElse(DEFAULT_MAX_INITIAL_LENGTH);
    Integer maxHeaderSize = configuration.getOptionalValue(NETTY_SERVER_HTTP_MAX_HEADER_SIZE, Integer.class)
      .orElse(DEFAULT_MAX_HEADER_SIZE);
    Integer maxChunkSize = configuration.getOptionalValue(NETTY_SERVER_HTTP_MAX_CHUNK_SIZE, Integer.class)
      .orElse(DEFAULT_MAX_CHUNK_SIZE);
    return new HttpRequestDecoderConfiguration(maxInitialLength, maxHeaderSize, maxChunkSize);
  }
}
