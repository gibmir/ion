package com.github.gibmir.ion.lib.nio.client.sender.channel.cache;

import com.github.gibmir.ion.api.client.context.RequestContext;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Map;

public class ChannelCache {
  private final Map<String, List<SocketChannel>> cache;

  public ChannelCache(Map<String, List<SocketChannel>> cache) {
    this.cache = cache;
  }

  public List<SocketChannel> getChannelsFor(RequestContext requestContext) {
    return cache.get(requestContext.getMethodName());
  }
}
