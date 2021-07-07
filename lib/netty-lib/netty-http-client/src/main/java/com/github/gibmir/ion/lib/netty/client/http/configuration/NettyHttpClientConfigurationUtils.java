package com.github.gibmir.ion.lib.netty.client.http.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public final class NettyHttpClientConfigurationUtils {
  private NettyHttpClientConfigurationUtils() {
  }

  //decoder
  //int
  public static final String NETTY_CLIENT_HTTP_MAX_INITIAL_LENGTH =
    ROOT_PREFIX + "netty.client.http.max.initial.length";
  public static final int DEFAULT_MAX_INITIAL_LENGTH = 4096;

  public static final String NETTY_CLIENT_HTTP_MAX_HEADER_SIZE =
    ROOT_PREFIX + "netty.client.http.max.header.size";
  public static final int DEFAULT_MAX_HEADER_SIZE = 8192;

  public static final String NETTY_CLIENT_HTTP_MAX_CHUNK_SIZE =
    ROOT_PREFIX + "netty.client.http.max.chunk.size";
  public static final int DEFAULT_MAX_CHUNK_SIZE = 8192;

  /**
   * @param configuration application config
   * @return response decoder config
   */
  public static HttpResponseDecoderConfiguration resolveDecoratorConfig(final Configuration configuration) {
    Integer maxInitialLength = configuration.getOptionalValue(NETTY_CLIENT_HTTP_MAX_INITIAL_LENGTH, Integer.class)
      .orElse(DEFAULT_MAX_INITIAL_LENGTH);
    Integer maxHeaderSize = configuration.getOptionalValue(NETTY_CLIENT_HTTP_MAX_HEADER_SIZE, Integer.class)
      .orElse(DEFAULT_MAX_HEADER_SIZE);
    Integer maxChunkSize = configuration.getOptionalValue(NETTY_CLIENT_HTTP_MAX_CHUNK_SIZE, Integer.class)
      .orElse(DEFAULT_MAX_CHUNK_SIZE);
    return new HttpResponseDecoderConfiguration(maxInitialLength, maxHeaderSize, maxChunkSize);
  }
}
