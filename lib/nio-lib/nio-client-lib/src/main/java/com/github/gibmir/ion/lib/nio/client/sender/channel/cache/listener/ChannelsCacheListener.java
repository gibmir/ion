package com.github.gibmir.ion.lib.nio.client.sender.channel.cache.listener;

import com.github.gibmir.ion.lib.nio.client.sender.channel.cache.ChannelCache;

import java.nio.channels.SocketChannel;

public class ChannelsCacheListener {
  private final ChannelCache cache;

  public ChannelsCacheListener(ChannelCache cache) {
    this.cache = cache;
  }

  public void notifyWith(String methodName, SocketChannel channel) {
  }
}
