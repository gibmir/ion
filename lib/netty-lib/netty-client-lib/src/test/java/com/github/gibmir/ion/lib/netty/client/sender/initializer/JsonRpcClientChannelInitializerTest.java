package com.github.gibmir.ion.lib.netty.client.sender.initializer;

import com.github.gibmir.ion.lib.netty.client.channel.initializer.JsonRpcClientChannelInitializer;
import com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment;
import com.github.gibmir.ion.lib.netty.client.environment.mock.JsonbMock;
import com.github.gibmir.ion.lib.netty.client.channel.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.channel.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.channel.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.JsonRpcChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class JsonRpcClientChannelInitializerTest {

  @Test
  void testInitChannelWithLogging() {
    EmbeddedChannel embeddedChannel = new EmbeddedChannel();
    ChannelPipeline pipeline = embeddedChannel.pipeline();
    Jsonb jsonb = JsonbMock.newMock(NettyClientTestEnvironment.TEST_JSON);
    JsonRpcClientChannelInitializer jsonRpcClientChannelInitializer =
      JsonRpcClientChannelInitializer.builder(new JsonRpcRequestEncoder(),
        new JsonRpcResponseDecoder(jsonb, NettyClientTestEnvironment.TEST_CHARSET),
        new JsonRpcResponseHandler(jsonb, NettyClientTestEnvironment.TEST_EMPTY_RESPONSE_LISTENER_REGISTRY))
        .withLogging(new LoggingHandler(LogLevel.ERROR)).build();
    pipeline.addLast(jsonRpcClientChannelInitializer);
    assertNotNull(pipeline.get(LoggingHandler.class));
    assertNotNull(pipeline.get(JsonRpcRequestEncoder.class));
    assertNotNull(pipeline.get(JsonRpcResponseDecoder.class));
    assertNotNull(pipeline.get(JsonRpcResponseHandler.class));
  }

  @Test
  void testInitChannelWithoutLogging() {
    EmbeddedChannel embeddedChannel = new EmbeddedChannel();
    ChannelPipeline pipeline = embeddedChannel.pipeline();
    Jsonb jsonb = JsonbMock.newMock(NettyClientTestEnvironment.TEST_JSON);
    JsonRpcClientChannelInitializer jsonRpcClientChannelInitializer = JsonRpcClientChannelInitializer.builder(new JsonRpcRequestEncoder(),
      new JsonRpcResponseDecoder(jsonb,
        NettyClientTestEnvironment.TEST_CHARSET),
      new JsonRpcResponseHandler(jsonb,
        NettyClientTestEnvironment.TEST_EMPTY_RESPONSE_LISTENER_REGISTRY)).build();
    pipeline.addLast(jsonRpcClientChannelInitializer);
    assertNull(pipeline.get(LoggingHandler.class));
    assertNotNull(pipeline.get(JsonRpcRequestEncoder.class));
    assertNotNull(pipeline.get(JsonRpcResponseDecoder.class));
    assertNotNull(pipeline.get(JsonRpcResponseHandler.class));
  }
}
