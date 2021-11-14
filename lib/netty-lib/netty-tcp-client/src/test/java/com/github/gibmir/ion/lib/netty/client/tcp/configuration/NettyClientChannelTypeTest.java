package com.github.gibmir.ion.lib.netty.client.tcp.configuration;

import com.github.gibmir.ion.lib.netty.client.common.configuration.NettyClientChannelType;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NettyClientChannelTypeTest {

  public static final String TEST_NIO_STRING = "NIO";
  public static final String TEST_EPOLL_STRING = "EPOLL";

  @Test
  void smokeEpoll() {
    NettyClientChannelType channelType = NettyClientChannelType.valueOf(TEST_EPOLL_STRING);
    assertEquals(NettyClientChannelType.EPOLL, channelType);
    assertTrue(channelType.resolveChannelClass().isAssignableFrom(EpollSocketChannel.class));

  }

  @Test
  void smokeNio() {
    NettyClientChannelType channelType = NettyClientChannelType.valueOf(TEST_NIO_STRING);
    assertEquals(NettyClientChannelType.NIO, channelType);
    assertTrue(channelType.resolveChannelClass().isAssignableFrom(NioSocketChannel.class));
  }

  @Test
  void testIncorrectValueOf() {
    assertThrows(IllegalArgumentException.class, () -> NettyClientChannelType.valueOf("incorrect"));
  }
}
