package com.github.gibmir.ion.lib.netty.client.common.configuration;

import io.netty.channel.Channel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public enum NettyClientChannelType {

  NIO(NioSocketChannel.class), EPOLL(EpollSocketChannel.class);

  private final Class<? extends Channel> channelClass;

  NettyClientChannelType(Class<? extends Channel> channelClass) {
    this.channelClass = channelClass;
  }

  public Class<? extends Channel> resolveChannelClass() {
    return channelClass;
  }
}
