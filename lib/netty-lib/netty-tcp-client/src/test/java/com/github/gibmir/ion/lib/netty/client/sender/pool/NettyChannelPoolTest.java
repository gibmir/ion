package com.github.gibmir.ion.lib.netty.client.sender.pool;

import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.common.channel.initializer.JsonRpcClientChannelInitializer;
import com.github.gibmir.ion.lib.netty.client.common.channel.pool.NettyChannelPool;
import com.github.gibmir.ion.lib.netty.client.environment.mock.EventLoopGroupMock;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.pool.SimpleChannelPool;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;

import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_CHARSET;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_EMPTY_RESPONSE_LISTENER_REGISTRY;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_LOGGING_HANDLER;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_NETTY_CLIENT_INITIALIZER;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_REAL_JSONB;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_REQUEST_ENCODER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class NettyChannelPoolTest {

  @Test
  void smoke() {
    NettyChannelPool nettyChannelPool = assertDoesNotThrow(() -> new NettyChannelPool(EventLoopGroupMock.emptyMock(),
      EmbeddedChannel.class, TEST_NETTY_CLIENT_INITIALIZER));
    assertDoesNotThrow(nettyChannelPool::close);
  }

  @Test
  void testEmbedded() {
    EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
    NettyChannelPool nettyChannelPool = assertDoesNotThrow(() -> new NettyChannelPool(eventLoopGroup, EmbeddedChannel.class,
      JsonRpcClientChannelInitializer.builder(TEST_REQUEST_ENCODER,
        new JsonRpcResponseDecoder(TEST_REAL_JSONB, TEST_CHARSET),
        new JsonRpcResponseHandler(TEST_REAL_JSONB, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY))
        .withLogging(TEST_LOGGING_HANDLER).build()));
    SimpleChannelPool localhost = assertDoesNotThrow(() -> nettyChannelPool.get(InetSocketAddress.createUnresolved("localhost", 55_555)));
    assertNotNull(localhost);
    assertDoesNotThrow(nettyChannelPool::close);
  }
}
