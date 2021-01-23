package com.github.gibmir.ion.lib.netty.server.common.configuration;

import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public enum NettyServerChannelType {
  NIO(NioServerSocketChannel.class), EPOLL(EpollServerSocketChannel.class);

  private final Class<? extends ServerChannel> channelClass;

  NettyServerChannelType(Class<? extends ServerChannel> channelClass) {
    this.channelClass = channelClass;
  }

  public Class<? extends ServerChannel> resolveChannelClass() {
    return channelClass;
  }
}
