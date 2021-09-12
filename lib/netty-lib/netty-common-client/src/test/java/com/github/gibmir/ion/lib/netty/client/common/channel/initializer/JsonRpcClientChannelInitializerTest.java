package com.github.gibmir.ion.lib.netty.client.common.channel.initializer;

import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

class JsonRpcClientChannelInitializerTest {
  @Test
  void smoke() {
    JsonRpcRequestEncoder encoder = mock(JsonRpcRequestEncoder.class);
    JsonRpcResponseDecoder decoder = mock(JsonRpcResponseDecoder.class);
    JsonRpcResponseHandler handler = mock(JsonRpcResponseHandler.class);
    JsonRpcClientChannelInitializer initializer = JsonRpcClientChannelInitializer.builder(encoder, decoder, handler)
      .build();

    EmbeddedChannel channel = new EmbeddedChannel();
    initializer.initChannel(channel);

    JsonRpcRequestEncoder jsonRpcRequestEncoder = channel.pipeline().get(JsonRpcRequestEncoder.class);
    JsonRpcResponseDecoder jsonRpcResponseDecoder = channel.pipeline().get(JsonRpcResponseDecoder.class);
    JsonRpcResponseHandler jsonRpcResponseHandler = channel.pipeline().get(JsonRpcResponseHandler.class);

    assertThat(jsonRpcRequestEncoder, notNullValue());
    assertThat(jsonRpcRequestEncoder, is(encoder));
    assertThat(jsonRpcResponseDecoder, notNullValue());
    assertThat(jsonRpcResponseDecoder, is(decoder));
    assertThat(jsonRpcResponseHandler, notNullValue());
    assertThat(jsonRpcResponseHandler, is(handler));
    assertThat(channel.pipeline().get(SslHandler.class), nullValue());
    assertThat(channel.pipeline().get(LoggingHandler.class), nullValue());
  }

  @Test
  void testWithLogging() {
    JsonRpcRequestEncoder encoder = mock(JsonRpcRequestEncoder.class);
    JsonRpcResponseDecoder decoder = mock(JsonRpcResponseDecoder.class);
    JsonRpcResponseHandler handler = mock(JsonRpcResponseHandler.class);
    LoggingHandler logging = mock(LoggingHandler.class);
    JsonRpcClientChannelInitializer initializer = JsonRpcClientChannelInitializer.builder(encoder, decoder, handler)
      .withLogging(logging).build();

    EmbeddedChannel channel = new EmbeddedChannel();
    initializer.initChannel(channel);

    JsonRpcRequestEncoder jsonRpcRequestEncoder = channel.pipeline().get(JsonRpcRequestEncoder.class);
    JsonRpcResponseDecoder jsonRpcResponseDecoder = channel.pipeline().get(JsonRpcResponseDecoder.class);
    JsonRpcResponseHandler jsonRpcResponseHandler = channel.pipeline().get(JsonRpcResponseHandler.class);
    LoggingHandler loggingHandler = channel.pipeline().get(LoggingHandler.class);

    assertThat(jsonRpcRequestEncoder, notNullValue());
    assertThat(jsonRpcRequestEncoder, is(encoder));
    assertThat(jsonRpcResponseDecoder, notNullValue());
    assertThat(jsonRpcResponseDecoder, is(decoder));
    assertThat(jsonRpcResponseHandler, notNullValue());
    assertThat(jsonRpcResponseHandler, is(handler));
    assertThat(loggingHandler, notNullValue());
    assertThat(loggingHandler, is(logging));
    assertThat(channel.pipeline().get(SslHandler.class), nullValue());
  }

  @Test
  void testWithSsl() {
    JsonRpcRequestEncoder encoder = mock(JsonRpcRequestEncoder.class);
    JsonRpcResponseDecoder decoder = mock(JsonRpcResponseDecoder.class);
    JsonRpcResponseHandler handler = mock(JsonRpcResponseHandler.class);
    SslHandler ssl = mock(SslHandler.class);
    JsonRpcClientChannelInitializer initializer = JsonRpcClientChannelInitializer.builder(encoder, decoder, handler)
      .withSsl(ssl).build();

    EmbeddedChannel channel = new EmbeddedChannel();
    initializer.initChannel(channel);

    JsonRpcRequestEncoder jsonRpcRequestEncoder = channel.pipeline().get(JsonRpcRequestEncoder.class);
    JsonRpcResponseDecoder jsonRpcResponseDecoder = channel.pipeline().get(JsonRpcResponseDecoder.class);
    JsonRpcResponseHandler jsonRpcResponseHandler = channel.pipeline().get(JsonRpcResponseHandler.class);
    SslHandler sslHandler = channel.pipeline().get(SslHandler.class);

    assertThat(jsonRpcRequestEncoder, notNullValue());
    assertThat(jsonRpcRequestEncoder, is(encoder));
    assertThat(jsonRpcResponseDecoder, notNullValue());
    assertThat(jsonRpcResponseDecoder, is(decoder));
    assertThat(jsonRpcResponseHandler, notNullValue());
    assertThat(jsonRpcResponseHandler, is(handler));
    assertThat(sslHandler, notNullValue());
    assertThat(sslHandler, is(ssl));
    assertThat(channel.pipeline().get(LoggingHandler.class), nullValue());
  }

  @Test
  void testWithSslAndLogging() {
    JsonRpcRequestEncoder encoder = mock(JsonRpcRequestEncoder.class);
    JsonRpcResponseDecoder decoder = mock(JsonRpcResponseDecoder.class);
    JsonRpcResponseHandler handler = mock(JsonRpcResponseHandler.class);
    SslHandler ssl = mock(SslHandler.class);
    LoggingHandler logging = mock(LoggingHandler.class);
    JsonRpcClientChannelInitializer initializer = JsonRpcClientChannelInitializer.builder(encoder, decoder, handler)
      .withSsl(ssl).withLogging(logging).build();

    EmbeddedChannel channel = new EmbeddedChannel();
    initializer.initChannel(channel);

    JsonRpcRequestEncoder jsonRpcRequestEncoder = channel.pipeline().get(JsonRpcRequestEncoder.class);
    JsonRpcResponseDecoder jsonRpcResponseDecoder = channel.pipeline().get(JsonRpcResponseDecoder.class);
    JsonRpcResponseHandler jsonRpcResponseHandler = channel.pipeline().get(JsonRpcResponseHandler.class);
    SslHandler sslHandler = channel.pipeline().get(SslHandler.class);
    LoggingHandler loggingHandler = channel.pipeline().get(LoggingHandler.class);

    assertThat(jsonRpcRequestEncoder, notNullValue());
    assertThat(jsonRpcRequestEncoder, is(encoder));
    assertThat(jsonRpcResponseDecoder, notNullValue());
    assertThat(jsonRpcResponseDecoder, is(decoder));
    assertThat(jsonRpcResponseHandler, notNullValue());
    assertThat(jsonRpcResponseHandler, is(handler));
    assertThat(sslHandler, notNullValue());
    assertThat(sslHandler, is(ssl));
    assertThat(loggingHandler, notNullValue());
    assertThat(loggingHandler, is(logging));
  }
}
