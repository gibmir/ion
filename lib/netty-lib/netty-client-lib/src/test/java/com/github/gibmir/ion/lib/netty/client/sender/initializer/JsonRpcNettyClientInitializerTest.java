package com.github.gibmir.ion.lib.netty.client.sender.initializer;

import com.github.gibmir.ion.lib.netty.client.sender.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.sender.handler.JsonRpcResponseHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.Test;

import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_LOGGING_HANDLER;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_NETTY_CLIENT_INITIALIZER;
import static org.junit.jupiter.api.Assertions.*;

class JsonRpcNettyClientInitializerTest {

    @Test
    void testInitChannel() {
      EmbeddedChannel embeddedChannel = new EmbeddedChannel();
      ChannelPipeline pipeline = embeddedChannel.pipeline();
      pipeline.addFirst(TEST_NETTY_CLIENT_INITIALIZER);
      assertNotNull(pipeline.get(LoggingHandler.class));
      assertNotNull(pipeline.get(JsonRpcRequestEncoder.class));
      assertNotNull(pipeline.get(JsonRpcResponseDecoder.class));
      assertNotNull(pipeline.get(JsonRpcResponseHandler.class));
    }
}
