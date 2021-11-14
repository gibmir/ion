package com.github.gibmir.ion.lib.netty.client.tcp.request.factory.provider;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.gibmir.ion.api.client.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.lib.netty.client.tcp.appender.TcpClientChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.SimpleResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.common.channel.pool.NettyChannelPoolFactory;
import com.github.gibmir.ion.lib.netty.client.common.configuration.NettyClientConfigurationUtils;
import com.github.gibmir.ion.lib.netty.client.tcp.request.factory.NettyTcpRequestFactory;
import com.github.gibmir.ion.lib.netty.client.tcp.sender.NettyTcpJsonRpcSender;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.JsonRpcChannelInitializer;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.common.configuration.decoder.FrameDecoderConfig;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.SimpleChannelPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public final class NettyTcpRequestFactoryProvider implements RequestFactoryProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyTcpRequestFactoryProvider.class);
  private static volatile NettyTcpRequestFactory nettyTcpRequestFactoryInstance;

  @Override
  public NettyTcpRequestFactory provide() {
    NettyTcpRequestFactory localInstance = nettyTcpRequestFactoryInstance;
    //double-check singleton
    if (localInstance == null) {
      synchronized (NettyTcpRequestFactory.class) {
        localInstance = nettyTcpRequestFactoryInstance;
        if (localInstance == null) {
          nettyTcpRequestFactoryInstance = localInstance = createNettyRequestFactory();
        }
      }
    }
    return localInstance;
  }

  private NettyTcpRequestFactory createNettyRequestFactory() {
    Configuration configuration = ConfigurationProvider.load().provide();
    Cache<String, ResponseFuture> responseFuturesCache = NettyClientConfigurationUtils.createResponseFuturesCache(configuration);
    ResponseListenerRegistry responseListenerRegistry = new SimpleResponseListenerRegistry(responseFuturesCache.asMap(),
      LoggerFactory.getLogger(SimpleResponseListenerRegistry.class));
    Charset charset = ConfigurationUtils.readCharsetFrom(configuration);
    Jsonb jsonb = ConfigurationUtils.createJsonbWith(configuration);
    FrameDecoderConfig frameDecoderConfig = NettyClientConfigurationUtils.resolveFrameDecoderConfig(configuration);
    int encoderFrameLength = NettyClientConfigurationUtils.resolveEncoderFrameLength(configuration);
    ChannelInitializer<Channel> channelInitializer = createJsonRpcNettyChannelInitializer(configuration,
      responseListenerRegistry, charset, jsonb, frameDecoderConfig, encoderFrameLength);
    ChannelPoolMap<SocketAddress, SimpleChannelPool> nettyChannelPool =
      new NettyChannelPoolFactory(NettyClientConfigurationUtils.createEventLoopGroup(configuration),
        NettyClientConfigurationUtils.resolveChannelClass(configuration), channelInitializer);
    LOGGER.info("Ion tcp json-rpc 2.0 client is ready to run");
    return new NettyTcpRequestFactory(new NettyTcpJsonRpcSender(nettyChannelPool, responseListenerRegistry),
      NettyClientConfigurationUtils.createSocketAddressWith(configuration), jsonb, charset,
      LoggerFactory.getLogger(NettyTcpJsonRpcSender.class));
  }

  private static ChannelInitializer<Channel> createJsonRpcNettyChannelInitializer(final Configuration configuration,
                                                                                  final ResponseListenerRegistry responseListenerRegistry,
                                                                                  final Charset charset, final Jsonb jsonb,
                                                                                  final FrameDecoderConfig frameDecoderConfig,
                                                                                  final int encoderFrameLength) {
    ChannelHandlerAppender channelHandlerAppender = NettyClientConfigurationUtils.appendSsl(configuration,
      NettyClientConfigurationUtils.appendLogging(configuration,
        new TcpClientChannelHandlerAppender(responseListenerRegistry, charset, jsonb, frameDecoderConfig,
          encoderFrameLength)));
    return new JsonRpcChannelInitializer(channelHandlerAppender);
  }


}
