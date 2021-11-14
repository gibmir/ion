package com.github.gibmir.ion.lib.netty.client.tcp.sender.initializer;

import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.common.channel.initializer.JsonRpcClientChannelInitializer;
import com.github.gibmir.ion.lib.netty.client.tcp.environment.TestEnvironment;
import com.github.gibmir.ion.lib.netty.client.tcp.environment.mock.JsonbMock;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class JsonRpcClientChannelInitializerTest {

  @Test
  void testInitChannelWithLoggingAndSsl() {
    EmbeddedChannel embeddedChannel = new EmbeddedChannel();
    ChannelPipeline pipeline = embeddedChannel.pipeline();
    Jsonb jsonb = JsonbMock.newMock(TestEnvironment.TEST_JSON);
    JsonRpcClientChannelInitializer jsonRpcClientChannelInitializer =
      JsonRpcClientChannelInitializer.builder(new JsonRpcRequestEncoder(),
        new JsonRpcResponseDecoder(jsonb, TestEnvironment.TEST_CHARSET),
        new JsonRpcResponseHandler(TestEnvironment.TEST_EMPTY_RESPONSE_LISTENER_REGISTRY))
        .withLogging(new LoggingHandler(LogLevel.ERROR))
        .withSsl(mock(SslHandler.class))
        .build();
    pipeline.addLast(jsonRpcClientChannelInitializer);
    assertNotNull(pipeline.get(SslHandler.class));
    assertNotNull(pipeline.get(LoggingHandler.class));
    assertNotNull(pipeline.get(JsonRpcRequestEncoder.class));
    assertNotNull(pipeline.get(JsonRpcResponseDecoder.class));
    assertNotNull(pipeline.get(JsonRpcResponseHandler.class));
  }

  @Test
  void testInitChannelWithoutLoggingAndSsl() {
    EmbeddedChannel embeddedChannel = new EmbeddedChannel();
    ChannelPipeline pipeline = embeddedChannel.pipeline();
    Jsonb jsonb = JsonbMock.newMock(TestEnvironment.TEST_JSON);
    JsonRpcClientChannelInitializer jsonRpcClientChannelInitializer = JsonRpcClientChannelInitializer.builder(new JsonRpcRequestEncoder(),
      new JsonRpcResponseDecoder(jsonb,
        TestEnvironment.TEST_CHARSET),
      new JsonRpcResponseHandler(
              TestEnvironment.TEST_EMPTY_RESPONSE_LISTENER_REGISTRY)).build();
    pipeline.addLast(jsonRpcClientChannelInitializer);
    assertNull(pipeline.get(LoggingHandler.class));
    assertNotNull(pipeline.get(JsonRpcRequestEncoder.class));
    assertNotNull(pipeline.get(JsonRpcResponseDecoder.class));
    assertNotNull(pipeline.get(JsonRpcResponseHandler.class));
  }
}
