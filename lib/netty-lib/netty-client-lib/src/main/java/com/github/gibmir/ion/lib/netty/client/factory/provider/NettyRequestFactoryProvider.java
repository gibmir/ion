package com.github.gibmir.ion.lib.netty.client.factory.provider;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.gibmir.ion.api.client.factory.configuration.RequestConfigurationUtils;
import com.github.gibmir.ion.api.client.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.lib.netty.client.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.channel.handler.response.registry.SimpleResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.channel.initializer.appender.JsonRpcClientChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.client.channel.pool.NettyChannelPool;
import com.github.gibmir.ion.lib.netty.client.configuration.NettyClientConfigurationUtils;
import com.github.gibmir.ion.lib.netty.common.exceptions.NettyInitializationException;
import com.github.gibmir.ion.lib.netty.client.factory.NettyRequestFactory;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.JsonRpcChannelInitializer;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.logging.LoggingAppenderDecorator;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ssl.SslAppenderDecorator;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import com.github.gibmir.ion.lib.netty.common.configuration.ssl.NettySslProvider;
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

public class NettyRequestFactoryProvider implements RequestFactoryProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyRequestFactoryProvider.class);
  private static volatile NettyRequestFactory nettyRequestFactoryInstance;

  @Override
  public NettyRequestFactory provide() {
    NettyRequestFactory localInstance = nettyRequestFactoryInstance;
    //double-check singleton
    if (localInstance == null) {
      synchronized (NettyRequestFactory.class) {
        localInstance = nettyRequestFactoryInstance;
        if (localInstance == null) {
          nettyRequestFactoryInstance = localInstance = createNettyRequestFactory();
        }
      }
    }
    return localInstance;
  }

  private NettyRequestFactory createNettyRequestFactory() {
    Configuration configuration = ConfigurationProvider.load().provide();
    Cache<String, ResponseFuture> responseFuturesCache = NettyClientConfigurationUtils.createResponseFuturesCache(configuration);
    ResponseListenerRegistry responseListenerRegistry = new SimpleResponseListenerRegistry(responseFuturesCache.asMap());
    Charset charset = RequestConfigurationUtils.readCharsetFrom(configuration);
    Jsonb jsonb = ConfigurationUtils.createJsonbWith(configuration);
    ChannelInitializer<Channel> channelInitializer = createJsonRpcNettyChannelInitializer(configuration,
      responseListenerRegistry, charset, jsonb);
    ChannelPoolMap<SocketAddress, SimpleChannelPool> nettyChannelPool =
      new NettyChannelPool(NettyClientConfigurationUtils.createEventLoopGroup(configuration),
        NettyClientConfigurationUtils.resolveChannelClass(configuration), channelInitializer);
    return new NettyRequestFactory(new JsonRpcNettySender(nettyChannelPool, responseListenerRegistry),
      NettyClientConfigurationUtils.createSocketAddressWith(configuration), jsonb, charset);
  }

  private static ChannelInitializer<Channel> createJsonRpcNettyChannelInitializer(Configuration configuration,
                                                                                  ResponseListenerRegistry responseListenerRegistry,
                                                                                  Charset charset, Jsonb jsonb) {
    ChannelHandlerAppender channelHandlerAppender = appendSsl(configuration, appendLogging(configuration,
      new JsonRpcClientChannelHandlerAppender(responseListenerRegistry, charset, jsonb)));
    return new JsonRpcChannelInitializer(channelHandlerAppender);
  }

  private static ChannelHandlerAppender appendLogging(Configuration configuration, ChannelHandlerAppender channelHandlerAppender) {
    NettyLogLevel level = NettyClientConfigurationUtils.resolveLogLevel(configuration);
    if (/*if logging is not disabled*/!NettyLogLevel.DISABLED.equals(level)) {
      return LoggingAppenderDecorator.decorate(channelHandlerAppender, level.get());
    }
    return channelHandlerAppender;
  }

  private static ChannelHandlerAppender appendSsl(Configuration configuration, ChannelHandlerAppender channelHandlerAppender) {
    NettySslProvider sslProvider = NettyClientConfigurationUtils.resolveSslProvider(configuration);
    if (/*if ssl is enabled*/!sslProvider.equals(NettySslProvider.DISABLED)) {
      try {
        SslContext sslContext = SslContextBuilder.forClient()
          .sslProvider(sslProvider.get())
          .trustManager(NettyClientConfigurationUtils.resolveTrustStore(configuration))
          .keyManager(NettyClientConfigurationUtils.resolveKeyStore(configuration),
            NettyClientConfigurationUtils.resolveKey(configuration),
            NettyClientConfigurationUtils.resolveKeyPassword(configuration))
          .clientAuth(NettyClientConfigurationUtils.resolveClientAuth(configuration))
          .build();
        return SslAppenderDecorator.decorate(channelHandlerAppender, sslContext);
      } catch (SSLException e) {
        throw new NettyInitializationException("Exception occurred during ssl initialization", e);
      }
    }
    return channelHandlerAppender;
  }
}
