package com.github.gibmir.ion.lib.netty.client.channel.pool;

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

public class NettyChannelPool extends AbstractChannelPoolMap<SocketAddress, SimpleChannelPool> {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyChannelPool.class);
  private final EventLoopGroup group;
  private final Class<? extends Channel> channelClass;
  private final ChannelInitializer<Channel> channelInitializer;

  public NettyChannelPool(EventLoopGroup group, Class<? extends Channel> channelClass,
                          ChannelInitializer<Channel> channelInitializer) {
    super();
    this.group = group;
    this.channelClass = channelClass;
    this.channelInitializer = channelInitializer;
  }

  @Override
  protected SimpleChannelPool newPool(SocketAddress socketAddress) {
    return new SimpleChannelPool(new Bootstrap()
      .remoteAddress(socketAddress)
      .group(group)
      .channel(channelClass)
      //todo channel option customization
      .option(ChannelOption.SO_KEEPALIVE, true), new ChannelPoolHandler() {
      @Override
      public void channelCreated(Channel ch) {
        LOGGER.debug("Channel for socket address [{}] was created", socketAddress);
        ch.pipeline().addLast(channelInitializer);
      }

      @Override
      public void channelAcquired(Channel ch) {
        LOGGER.debug("Channel for socket address [{}] was acquired", socketAddress);
      }

      @Override
      public void channelReleased(Channel ch) {
        LOGGER.debug("Channel for socket address [{}] was released", socketAddress);
      }
    });
  }
}
