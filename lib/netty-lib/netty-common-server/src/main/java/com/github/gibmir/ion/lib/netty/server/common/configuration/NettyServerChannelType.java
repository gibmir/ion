package com.github.gibmir.ion.lib.netty.server.common.configuration;

import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public enum NettyServerChannelType {
  NIO(NioServerSocketChannel.class), EPOLL(EpollServerSocketChannel.class);

  private final Class<? extends ServerChannel> channelClass;

  NettyServerChannelType(final Class<? extends ServerChannel> channelClass) {
    this.channelClass = channelClass;
  }

  /**
   * @return resolved server channel class ({@link NioServerSocketChannel} or {@link EpollServerSocketChannel})
   */
  public Class<? extends ServerChannel> resolveChannelClass() {
    return channelClass;
  }
}
