package com.github.gibmir.ion.lib.nio.client.sender.channel.resolver;

import com.github.gibmir.ion.api.client.context.RequestContext;
import com.github.gibmir.ion.lib.nio.client.sender.channel.cache.ChannelCache;

import java.nio.channels.SocketChannel;
import java.util.List;

public class ChannelResolver {
  private final ChannelCache channelCache;

  public ChannelResolver(ChannelCache channelCache) {
    this.channelCache = channelCache;
  }

  public SocketChannel resolveFor(RequestContext requestContext) {
    List<SocketChannel> socketChannels = channelCache.getChannelsFor(requestContext);
    return socketChannels.get(0);
  }
}
