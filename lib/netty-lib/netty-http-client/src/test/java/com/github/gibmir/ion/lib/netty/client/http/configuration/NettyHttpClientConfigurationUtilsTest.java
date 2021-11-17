package com.github.gibmir.ion.lib.netty.client.http.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.gibmir.ion.lib.netty.client.http.configuration.NettyHttpClientConfigurationUtils.DEFAULT_MAX_CHUNK_SIZE;
import static com.github.gibmir.ion.lib.netty.client.http.configuration.NettyHttpClientConfigurationUtils.DEFAULT_MAX_HEADER_SIZE;
import static com.github.gibmir.ion.lib.netty.client.http.configuration.NettyHttpClientConfigurationUtils.DEFAULT_MAX_INITIAL_LENGTH;
import static com.github.gibmir.ion.lib.netty.client.http.configuration.NettyHttpClientConfigurationUtils.NETTY_CLIENT_HTTP_MAX_CHUNK_SIZE;
import static com.github.gibmir.ion.lib.netty.client.http.configuration.NettyHttpClientConfigurationUtils.NETTY_CLIENT_HTTP_MAX_HEADER_SIZE;
import static com.github.gibmir.ion.lib.netty.client.http.configuration.NettyHttpClientConfigurationUtils.NETTY_CLIENT_HTTP_MAX_INITIAL_LENGTH;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class NettyHttpClientConfigurationUtilsTest {

  @Test
  void testResolveDecoratorConfigDefaults() {
    HttpResponseDecoderConfiguration config = NettyHttpClientConfigurationUtils
      .resolveDecoratorConfig(mock(Configuration.class));

    assertThat(config.getMaxChunkSize(), equalTo(DEFAULT_MAX_CHUNK_SIZE));
    assertThat(config.getMaxHeaderSize(), equalTo(DEFAULT_MAX_HEADER_SIZE));
    assertThat(config.getMaxInitialLineLength(), equalTo(DEFAULT_MAX_INITIAL_LENGTH));
  }

  @Test
  void testResolveDecoratorConfigWithMaxChunkSize() {
    Configuration configuration = mock(Configuration.class);
    int testMaxChunkSize = 123;
    doAnswer(__ -> Optional.of(testMaxChunkSize)).when(configuration)
      .getOptionalValue(NETTY_CLIENT_HTTP_MAX_CHUNK_SIZE, Integer.class);
    HttpResponseDecoderConfiguration config = NettyHttpClientConfigurationUtils
      .resolveDecoratorConfig(configuration);

    assertThat(config.getMaxChunkSize(), equalTo(testMaxChunkSize));
  }

  @Test
  void testResolveDecoratorConfigWithMaxHeaderSize() {
    Configuration configuration = mock(Configuration.class);
    int testMaxHeaderSize = 123;
    doAnswer(__ -> Optional.of(testMaxHeaderSize)).when(configuration)
      .getOptionalValue(NETTY_CLIENT_HTTP_MAX_HEADER_SIZE, Integer.class);
    HttpResponseDecoderConfiguration config = NettyHttpClientConfigurationUtils
      .resolveDecoratorConfig(configuration);

    assertThat(config.getMaxHeaderSize(), equalTo(testMaxHeaderSize));
  }

  @Test
  void testResolveDecoratorConfigWithMaxInitialLength() {
    Configuration configuration = mock(Configuration.class);
    int testMaxInitialLength = 123;
    doAnswer(__ -> Optional.of(testMaxInitialLength)).when(configuration)
      .getOptionalValue(NETTY_CLIENT_HTTP_MAX_INITIAL_LENGTH, Integer.class);
    HttpResponseDecoderConfiguration config = NettyHttpClientConfigurationUtils
      .resolveDecoratorConfig(configuration);

    assertThat(config.getMaxInitialLineLength(), equalTo(testMaxInitialLength));
  }
}
