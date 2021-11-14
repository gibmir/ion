package com.github.gibmir.ion.lib.netty.client.tcp.environment.mock;

import io.netty.channel.Channel;
import io.netty.channel.pool.ChannelPool;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.Promise;

public class ChannelPoolStub implements ChannelPool {
  private final Future<Channel> channelFuture;

  private ChannelPoolStub(Future<Channel> channelFuture) {
    this.channelFuture = channelFuture;
  }

  public static ChannelPoolStub createWith(Future<Channel> channelFuture) {
    return new ChannelPoolStub(channelFuture);
  }

  @Override
  public Future<Channel> acquire() {
    return channelFuture;
  }

  @Override
  public Future<Channel> acquire(Promise<Channel> promise) {
    return channelFuture;
  }

  @Override
  public Future<Void> release(Channel channel) {
    return null;
  }

  @Override
  public Future<Void> release(Channel channel, Promise<Void> promise) {
    return null;
  }

  @Override
  public void close() {

  }
}
