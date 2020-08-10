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
import com.github.gibmir.ion.lib.netty.client.sender.pool.ChannelPool;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

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
    JsonRpcNettyChannelInitializer channelInitializer = createJsonRpcNettyChannelInitializer(configuration, responseListenerRegistry, charset, jsonb);
    ChannelPool channelPool = new ChannelPool(new ConcurrentHashMap<>(),
      NettyRequestConfigurationUtils.createEventLoopGroup(configuration),
      NettyRequestConfigurationUtils.resolveChannelClass(configuration),
      channelInitializer);
    return new NettyRequestFactory(new JsonRpcNettySender(channelPool, responseListenerRegistry),
      NettyRequestConfigurationUtils.createSocketAddressWith(configuration), jsonb, charset);
  }

  private JsonRpcNettyChannelInitializer createJsonRpcNettyChannelInitializer(Configuration configuration,
                                                                              ResponseListenerRegistry responseListenerRegistry,
                                                                              Charset charset, Jsonb jsonb) {
    JsonRpcRequestEncoder jsonRpcRequestEncoder = new JsonRpcRequestEncoder();
    JsonRpcResponseDecoder jsonRpcResponseDecoder = new JsonRpcResponseDecoder(jsonb, charset);
    JsonRpcResponseHandler jsonRpcResponseHandler = new JsonRpcResponseHandler(jsonb, responseListenerRegistry);
    NettyLogLevel level = NettyRequestConfigurationUtils.resolveLogLevel(configuration);
    if (level.equals(NettyLogLevel.DISABLED)) {
      return JsonRpcNettyChannelInitializer.withoutLogging(jsonRpcRequestEncoder, jsonRpcResponseDecoder, jsonRpcResponseHandler);
    } else {
      return JsonRpcNettyChannelInitializer.withLogging(new LoggingHandler(level.get()), jsonRpcRequestEncoder,
        jsonRpcResponseDecoder, jsonRpcResponseHandler);
    }
  }
}
