package com.github.gibmir.ion.lib.netty.client.tcp.environment.mock;

import com.github.gibmir.ion.lib.netty.client.common.channel.pool.NettyChannelPoolFactory;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolMap;

import java.net.SocketAddress;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class ChannelPoolMapMock {

  private ChannelPoolMapMock() {
  }

  public static NettyChannelPoolFactory emptyMock() {
    return mock(NettyChannelPoolFactory.class);
  }

  @SuppressWarnings("unchecked")
  public static <T extends ChannelPool> ChannelPoolMap<SocketAddress, T> newMock(T channelPool) {
    ChannelPoolMap<SocketAddress, T> nettyChannelPool = mock(ChannelPoolMap.class);
    doAnswer(__ -> channelPool).when(nettyChannelPool).get(any(SocketAddress.class));
    return nettyChannelPool;
  }

  @SuppressWarnings("unchecked")
  public static <T extends ChannelPool> ChannelPoolMap<SocketAddress, T> newMock(Class<? extends RuntimeException> exceptionClass) {
    ChannelPoolMap<SocketAddress, T> nettyChannelPool = mock(ChannelPoolMap.class);
    doThrow(exceptionClass).when(nettyChannelPool).get(any(SocketAddress.class));
    return nettyChannelPool;
  }
}
