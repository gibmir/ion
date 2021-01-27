package com.github.gibmir.ion.lib.netty.client.http.request.factory.provider;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.gibmir.ion.api.client.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.SimpleResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.common.channel.pool.NettyChannelPool;
import com.github.gibmir.ion.lib.netty.client.common.configuration.NettyClientConfigurationUtils;
import com.github.gibmir.ion.lib.netty.client.http.channel.appender.HttpClientChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.client.http.request.factory.NettyHttpRequestFactory;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.JsonRpcChannelInitializer;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.SimpleChannelPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public class NettyHttpRequestFactoryProvider implements RequestFactoryProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyHttpRequestFactoryProvider.class);
  private static volatile NettyHttpRequestFactory nettyRequestFactoryInstance;

  @Override
  public NettyHttpRequestFactory provide() {
    NettyHttpRequestFactory localInstance = nettyRequestFactoryInstance;
    //double-check singleton
    if (localInstance == null) {
      synchronized (NettyHttpRequestFactory.class) {
        localInstance = nettyRequestFactoryInstance;
        if (localInstance == null) {
          nettyRequestFactoryInstance = localInstance = createNettyHttpRequestFactory();
        }
      }
    }
    return localInstance;
  }

  private NettyHttpRequestFactory createNettyHttpRequestFactory() {
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
    LOGGER.info("Ion http json-rpc 2.0 client is ready to run");
    return new NettyHttpRequestFactory(new NettyHttpJsonRpcSender(nettyChannelPool, responseListenerRegistry),
      NettyClientConfigurationUtils.createUriWith(configuration), jsonb, charset);
  }

  private static ChannelInitializer<Channel> createJsonRpcNettyChannelInitializer(Configuration configuration,
                                                                                  ResponseListenerRegistry responseListenerRegistry,
                                                                                  Charset charset, Jsonb jsonb) {
    ChannelHandlerAppender channelHandlerAppender = NettyClientConfigurationUtils.appendSsl(configuration,
      NettyClientConfigurationUtils.appendLogging(configuration,
        new HttpClientChannelHandlerAppender(responseListenerRegistry, charset, jsonb)));
    return new JsonRpcChannelInitializer(channelHandlerAppender);
  }
}
