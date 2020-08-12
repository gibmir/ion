package com.github.gibmir.ion.lib.netty.client.sender.initializer;

import com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment;
import com.github.gibmir.ion.lib.netty.client.environment.mock.JsonbMock;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.sender.handler.JsonRpcResponseHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class JsonRpcNettyChannelInitializerTest {

  @Test
  void testInitChannelWithLogging() {
    EmbeddedChannel embeddedChannel = new EmbeddedChannel();
    ChannelPipeline pipeline = embeddedChannel.pipeline();
    JsonRpcNettyChannelInitializer jsonRpcNettyChannelInitializer = JsonRpcNettyChannelInitializer
      .withLogging(new LoggingHandler(LogLevel.ERROR), new JsonRpcRequestEncoder(),
        new JsonRpcResponseDecoder(JsonbMock.newMock(NettyClientTestEnvironment.TEST_JSON),
          NettyClientTestEnvironment.TEST_CHARSET),
        new JsonRpcResponseHandler(JsonbMock.newMock(NettyClientTestEnvironment.TEST_JSON),
          NettyClientTestEnvironment.TEST_EMPTY_RESPONSE_LISTENER_REGISTRY));
    pipeline.addLast(jsonRpcNettyChannelInitializer);
    assertNotNull(pipeline.get(LoggingHandler.class));
    assertNotNull(pipeline.get(JsonRpcRequestEncoder.class));
    assertNotNull(pipeline.get(JsonRpcResponseDecoder.class));
    assertNotNull(pipeline.get(JsonRpcResponseHandler.class));
  }

  @Test
  void testInitChannelWithoutLogging() {
    EmbeddedChannel embeddedChannel = new EmbeddedChannel();
    ChannelPipeline pipeline = embeddedChannel.pipeline();
    JsonRpcNettyChannelInitializer jsonRpcNettyChannelInitializer = JsonRpcNettyChannelInitializer
      .withoutLogging(new JsonRpcRequestEncoder(),
        new JsonRpcResponseDecoder(JsonbMock.newMock(NettyClientTestEnvironment.TEST_JSON),
          NettyClientTestEnvironment.TEST_CHARSET),
        new JsonRpcResponseHandler(JsonbMock.newMock(NettyClientTestEnvironment.TEST_JSON),
          NettyClientTestEnvironment.TEST_EMPTY_RESPONSE_LISTENER_REGISTRY));
    pipeline.addLast(jsonRpcNettyChannelInitializer);
    assertNull(pipeline.get(LoggingHandler.class));
    assertNotNull(pipeline.get(JsonRpcRequestEncoder.class));
    assertNotNull(pipeline.get(JsonRpcResponseDecoder.class));
    assertNotNull(pipeline.get(JsonRpcResponseHandler.class));
  }
}
