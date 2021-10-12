package com.github.gibmir.ion.lib.netty.client.common.channel.pool;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.pool.ChannelPool;
import org.junit.jupiter.api.Test;

import java.net.SocketAddress;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;

class NettyChannelPoolFactoryTest {

  @Test
  @SuppressWarnings("unchecked")
  void smoke() {
    NettyChannelPoolFactory nettyChannelPool = assertDoesNotThrow(() -> new NettyChannelPoolFactory(mock(EventLoopGroup.class),
      EmbeddedChannel.class,
      // unchecked
      mock(ChannelInitializer.class)));
    ChannelPool channelPool = nettyChannelPool.newPool(mock(SocketAddress.class));
    assertThat(channelPool, not(nullValue()));
    assertDoesNotThrow(nettyChannelPool::close);
  }
}
