package com.github.gibmir.ion.lib.netty.client.sender.pool;

import com.github.gibmir.ion.lib.netty.client.sender.initializer.JsonRpcNettyChannelInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.Map;

//todo redo on netty channel pool
public class ChannelPool implements Closeable {
  private final Map<SocketAddress, Channel> channelsPool;
  private final EventLoopGroup group;
  private final Class<? extends Channel> channelClass;
  private final JsonRpcNettyChannelInitializer handler;

  public ChannelPool(Map<SocketAddress, Channel> channelsPool, EventLoopGroup group,
                     Class<? extends Channel> channelClass, JsonRpcNettyChannelInitializer handler) {
    this.channelsPool = channelsPool;
    this.group = group;
    this.channelClass = channelClass;
    this.handler = handler;
  }

  /**
   * @param socketAddress address
   * @return configured channel
   * @throws ChannelException if exception occurred while preparing channel
   */
  public Channel getOrCreate(SocketAddress socketAddress) {

    return channelsPool.computeIfAbsent(socketAddress, address -> {
      try {
        return new Bootstrap()
          .group(group)
          .channel(channelClass)
          //todo channel options configuration
          .option(ChannelOption.SO_KEEPALIVE, true)
          .handler(handler)
          .connect(address)
          .sync()
          .channel();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new ChannelException("Thread was interrupted. Can't create channel for address:" + socketAddress, e);
      } catch (Exception e) {
        throw new ChannelException("Can't create channel for address:" + socketAddress, e);
      }
    });
  }

  @Override
  public void close() {
    group.shutdownGracefully();
  }
}
