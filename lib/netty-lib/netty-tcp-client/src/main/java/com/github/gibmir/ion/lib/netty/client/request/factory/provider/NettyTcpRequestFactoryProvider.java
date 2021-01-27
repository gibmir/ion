package com.github.gibmir.ion.lib.netty.client.request.factory.provider;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.gibmir.ion.api.client.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.SimpleResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.channel.appender.TcpClientChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.client.common.channel.pool.NettyChannelPool;
import com.github.gibmir.ion.lib.netty.client.common.configuration.NettyClientConfigurationUtils;
import com.github.gibmir.ion.lib.netty.client.sender.NettyTcpJsonRpcSender;
import com.github.gibmir.ion.lib.netty.client.request.factory.NettyTcpRequestFactory;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.JsonRpcChannelInitializer;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.logging.LoggingAppenderDecorator;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ssl.SslAppenderDecorator;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import com.github.gibmir.ion.lib.netty.common.configuration.ssl.NettySslProvider;
import com.github.gibmir.ion.lib.netty.common.exceptions.NettyInitializationException;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import javax.net.ssl.SSLException;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public class NettyTcpRequestFactoryProvider implements RequestFactoryProvider {
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
    ResponseListenerRegistry responseListenerRegistry = new SimpleResponseListenerRegistry(responseFuturesCache.asMap());
    Charset charset = ConfigurationUtils.readCharsetFrom(configuration);
    Jsonb jsonb = ConfigurationUtils.createJsonbWith(configuration);
    ChannelInitializer<Channel> channelInitializer = createJsonRpcNettyChannelInitializer(configuration,
      responseListenerRegistry, charset, jsonb);
    ChannelPoolMap<SocketAddress, SimpleChannelPool> nettyChannelPool =
      new NettyChannelPool(NettyClientConfigurationUtils.createEventLoopGroup(configuration),
        NettyClientConfigurationUtils.resolveChannelClass(configuration), channelInitializer);
    LOGGER.info("Ion tcp json-rpc 2.0 client is ready to run");
    return new NettyTcpRequestFactory(new NettyTcpJsonRpcSender(nettyChannelPool, responseListenerRegistry),
      NettyClientConfigurationUtils.createSocketAddressWith(configuration), jsonb, charset);
  }

  private static ChannelInitializer<Channel> createJsonRpcNettyChannelInitializer(Configuration configuration,
                                                                                  ResponseListenerRegistry responseListenerRegistry,
                                                                                  Charset charset, Jsonb jsonb) {
    ChannelHandlerAppender channelHandlerAppender = NettyClientConfigurationUtils.appendSsl(configuration,
      NettyClientConfigurationUtils.appendLogging(configuration,
      new TcpClientChannelHandlerAppender(responseListenerRegistry, charset, jsonb)));
    return new JsonRpcChannelInitializer(channelHandlerAppender);
  }



}
