package com.github.gibmir.ion.lib.netty.client.factory.provider;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.gibmir.ion.api.client.factory.configuration.RequestConfigurationUtils;
import com.github.gibmir.ion.api.client.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.lib.netty.client.configuration.NettyRequestConfigurationUtils;
import com.github.gibmir.ion.lib.netty.client.factory.NettyRequestFactory;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.sender.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry.SimpleResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.sender.initializer.JsonRpcNettyChannelInitializer;
import com.github.gibmir.ion.lib.netty.client.sender.pool.NettyChannelPool;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import com.github.gibmir.ion.lib.netty.common.configuration.ssl.NettySslProvider;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;

import javax.json.bind.Jsonb;
import javax.net.ssl.SSLException;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public class NettyRequestFactoryProvider implements RequestFactoryProvider {
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
    Cache<String, ResponseFuture> responseFuturesCache = NettyRequestConfigurationUtils.createResponseFuturesCache(configuration);
    ResponseListenerRegistry responseListenerRegistry = new SimpleResponseListenerRegistry(responseFuturesCache.asMap());
    Charset charset = RequestConfigurationUtils.readCharsetFrom(configuration);
    Jsonb jsonb = ConfigurationUtils.createJsonbWith(configuration);
    JsonRpcNettyChannelInitializer channelInitializer = createJsonRpcNettyChannelInitializer(configuration,
      responseListenerRegistry, charset, jsonb);
    ChannelPoolMap<SocketAddress, SimpleChannelPool> nettyChannelPool =
      new NettyChannelPool(NettyRequestConfigurationUtils.createEventLoopGroup(configuration),
        NettyRequestConfigurationUtils.resolveChannelClass(configuration), channelInitializer);
    return new NettyRequestFactory(new JsonRpcNettySender(nettyChannelPool, responseListenerRegistry),
      NettyRequestConfigurationUtils.createSocketAddressWith(configuration), jsonb, charset);
  }

  private static JsonRpcNettyChannelInitializer createJsonRpcNettyChannelInitializer(Configuration configuration,
                                                                                     ResponseListenerRegistry responseListenerRegistry,
                                                                                     Charset charset, Jsonb jsonb) {
    JsonRpcRequestEncoder jsonRpcRequestEncoder = new JsonRpcRequestEncoder();
    JsonRpcResponseDecoder jsonRpcResponseDecoder = new JsonRpcResponseDecoder(jsonb, charset);
    JsonRpcResponseHandler jsonRpcResponseHandler = new JsonRpcResponseHandler(jsonb, responseListenerRegistry);

    JsonRpcNettyChannelInitializer.Builder builder = JsonRpcNettyChannelInitializer.builder(jsonRpcRequestEncoder,
      jsonRpcResponseDecoder, jsonRpcResponseHandler);
    appendLogging(configuration, builder);
    appendSsl(configuration, builder);
    return builder.build();
  }

  private static void appendLogging(Configuration configuration, JsonRpcNettyChannelInitializer.Builder builder) {
    NettyLogLevel level = NettyRequestConfigurationUtils.resolveLogLevel(configuration);
    if (/*if logging is not disabled*/!NettyLogLevel.DISABLED.equals(level)) {
      builder.withLogging(new LoggingHandler(level.get()));
    }
  }

  private static void appendSsl(Configuration configuration, JsonRpcNettyChannelInitializer.Builder builder) {
    NettySslProvider sslProvider = NettyRequestConfigurationUtils.resolveSslProvider(configuration);
    if (/*if ssl is enabled*/!sslProvider.equals(NettySslProvider.DISABLED)) {
      try {
        SslContext sslContext = SslContextBuilder.forClient()
          .sslProvider(sslProvider.get())
          .trustManager(NettyRequestConfigurationUtils.resolveTrustManagerCert(configuration))
          .keyManager(NettyRequestConfigurationUtils.resolveKeyManagerCert(configuration),
            NettyRequestConfigurationUtils.resolveKey(configuration),
            NettyRequestConfigurationUtils.resolveKeyPassword(configuration))
          .clientAuth(NettyRequestConfigurationUtils.resolveClientAuth(configuration))
          .build();
        builder.withSsl(sslContext.newHandler(ByteBufAllocator.DEFAULT));
      } catch (SSLException e) {
        e.printStackTrace();
      }
    }
  }
}
