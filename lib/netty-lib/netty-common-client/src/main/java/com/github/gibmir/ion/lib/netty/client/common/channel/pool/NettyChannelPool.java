package com.github.gibmir.ion.lib.netty.client.common.channel.pool;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.pool.AbstractChannelPoolMap;
import io.netty.channel.pool.ChannelPoolHandler;
import io.netty.channel.pool.SimpleChannelPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public final class NettyChannelPool extends AbstractChannelPoolMap<SocketAddress, SimpleChannelPool> {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyChannelPool.class);
  private final EventLoopGroup group;
  private final Class<? extends Channel> channelClass;
  private final ChannelInitializer<Channel> channelInitializer;

  public NettyChannelPool(final EventLoopGroup group, final Class<? extends Channel> channelClass,
                          final ChannelInitializer<Channel> channelInitializer) {
    super();
    this.group = group;
    this.channelClass = channelClass;
    this.channelInitializer = channelInitializer;
  }

  @Override
  protected SimpleChannelPool newPool(final SocketAddress socketAddress) {
    return new SimpleChannelPool(new Bootstrap()
      .remoteAddress(socketAddress)
      .group(group)
      .channel(channelClass)
      //todo channel option customization
      .option(ChannelOption.SO_KEEPALIVE, true), new ChannelPoolHandler() {
      @Override
      public void channelCreated(final Channel ch) {
        LOGGER.debug("Channel for socket address [{}] was created", socketAddress);
        ch.pipeline().addLast(channelInitializer);
      }

      @Override
      public void channelAcquired(final Channel ch) {
        LOGGER.debug("Channel for socket address [{}] was acquired", socketAddress);
      }

      @Override
      public void channelReleased(final Channel ch) {
        LOGGER.debug("Channel for socket address [{}] was released", socketAddress);
      }
    });
  }
}
