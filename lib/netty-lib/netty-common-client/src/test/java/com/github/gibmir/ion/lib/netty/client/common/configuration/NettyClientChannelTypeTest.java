package com.github.gibmir.ion.lib.netty.client.common.configuration;

import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class NettyClientChannelTypeTest {

  @Test
  void testResolveChannelClass() {
    assertThat(NettyClientChannelType.NIO.resolveChannelClass(), is(NioSocketChannel.class));
    assertThat(NettyClientChannelType.EPOLL.resolveChannelClass(), is(EpollSocketChannel.class));

  }
}
