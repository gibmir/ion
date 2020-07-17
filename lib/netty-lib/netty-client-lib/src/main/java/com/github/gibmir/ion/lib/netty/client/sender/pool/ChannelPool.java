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
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.Map;

public class ChannelPool {
  private final Map<SocketAddress, Channel> channelsPool;
  private final EventLoopGroup group;
  private final Class<? extends Channel> channelClass;
  private final LogLevel logLevel;
  private final ResponseListenerRegistry responseListenerRegistry;

  public ChannelPool(Map<SocketAddress, Channel> channelsPool, EventLoopGroup group,
                     Class<? extends Channel> channelClass, LogLevel logLevel,
                     ResponseListenerRegistry responseListenerRegistry) {
    this.channelsPool = channelsPool;
    this.group = group;
    this.channelClass = channelClass;
    this.logLevel = logLevel;
    this.responseListenerRegistry = responseListenerRegistry;
  }

  public Channel getOrCreate(Jsonb jsonb, Charset charset,
                             SocketAddress socketAddress) {
    return channelsPool.computeIfAbsent(socketAddress, address -> {
      try {
        return new Bootstrap()
          .group(group)
          .channel(channelClass)
          .option(ChannelOption.SO_KEEPALIVE, true)
          .handler(new JsonRpcNettyClientInitializer(new LoggingHandler(logLevel),
            new JsonRpcRequestEncoder(jsonb, charset), new JsonRpcResponseDecoder(jsonb, charset),
            new JsonRpcResponseHandler(jsonb, responseListenerRegistry)))
          .connect(address)
          .sync()
          .channel();
      } catch (Exception e) {
        Thread.currentThread().interrupt();
        throw new ChannelException("Can't create channel for address:" + socketAddress, e);
      }
    });
  }
}
