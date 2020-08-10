package com.github.gibmir.ion.lib.netty.client.sender.pool;

import com.github.gibmir.ion.lib.netty.client.sender.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.sender.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.sender.initializer.JsonRpcNettyClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.json.bind.Jsonb;
import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.Map;

//todo redo on netty channel pool
public class ChannelPool implements Closeable {
  private final Map<SocketAddress, Channel> channelsPool;
  private final EventLoopGroup group;
  private final Class<? extends Channel> channelClass;
  private final LogLevel logLevel;
  private final ResponseListenerRegistry responseListenerRegistry;
  private final Jsonb channelJsonb;
  private final Charset channelCharset;

  public ChannelPool(Map<SocketAddress, Channel> channelsPool, EventLoopGroup group,
                     Class<? extends Channel> channelClass, LogLevel logLevel,
                     ResponseListenerRegistry responseListenerRegistry, Jsonb channelJsonb,
                     Charset channelCharset) {
    this.channelsPool = channelsPool;
    this.group = group;
    this.channelClass = channelClass;
    this.logLevel = logLevel;
    this.responseListenerRegistry = responseListenerRegistry;
    this.channelJsonb = channelJsonb;
    this.channelCharset = channelCharset;
  }

  public Channel getOrCreate(SocketAddress socketAddress) {
    return channelsPool.computeIfAbsent(socketAddress, address -> {
      try {
        return new Bootstrap()
          .group(group)
          .channel(channelClass)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .handler(new JsonRpcNettyClientInitializer(new LoggingHandler(logLevel),
            new JsonRpcRequestEncoder(), new JsonRpcResponseDecoder(channelJsonb, channelCharset),
            new JsonRpcResponseHandler(channelJsonb, responseListenerRegistry)))
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
