package com.github.gibmir.ion.lib.netty.server.factory.provider;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.cache.processor.ServerProcessor;
import com.github.gibmir.ion.api.server.cache.processor.SimpleProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.factory.JsonRpcServerFactory;
import com.github.gibmir.ion.api.server.factory.configuration.ServerConfigurationUtils;
import com.github.gibmir.ion.api.server.factory.provider.JsonRpcServerFactoryProvider;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import com.github.gibmir.ion.lib.netty.server.codecs.decoder.JsonRpcRequestDecoder;
import com.github.gibmir.ion.lib.netty.server.codecs.encoder.JsonRpcResponseEncoder;
import com.github.gibmir.ion.lib.netty.server.configuration.NettyServerConfigurationUtils;
import com.github.gibmir.ion.lib.netty.server.factory.NettyJsonRpcServerFactory;
import com.github.gibmir.ion.lib.netty.server.handler.JsonRpcRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.logging.LoggingHandler;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

public class NettyJsonRpcServerFactoryProvider implements JsonRpcServerFactoryProvider {
  private static volatile NettyJsonRpcServerFactory nettyJsonRpcServerFactory;

  @Override
  public JsonRpcServerFactory provide() {
    NettyJsonRpcServerFactory localInstance = nettyJsonRpcServerFactory;
    //double-check singleton
    if (localInstance == null) {
      synchronized (NettyJsonRpcServerFactory.class) {
        localInstance = nettyJsonRpcServerFactory;
        if (localInstance == null) {
          nettyJsonRpcServerFactory = localInstance = createJsonRpcServerFactory();
        }
      }
    }
    return localInstance;
  }

  private NettyJsonRpcServerFactory createJsonRpcServerFactory() {
    ProcedureProcessorRegistry procedureProcessorRegistry =
      new SimpleProcedureProcessorRegistry(new ConcurrentHashMap<>());
    ServerProcessor serverProcessor = new ServerProcessor(procedureProcessorRegistry);
    Configuration configuration = ConfigurationProvider.load().provide();
    Charset charset = ServerConfigurationUtils.createCharsetWith(configuration);
    Jsonb jsonb = ConfigurationUtils.createJsonbWith(configuration);
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    EventLoopGroup bossGroup = NettyServerConfigurationUtils.createEventLoopGroup(configuration);
    EventLoopGroup workerGroup = NettyServerConfigurationUtils.createEventLoopGroup(configuration);
    NettyLogLevel nettyLogLevel = NettyServerConfigurationUtils.resolveLogLevel(configuration);
    if (/*if log is not disabled*/!NettyLogLevel.DISABLED.equals(nettyLogLevel)) {
      serverBootstrap.handler(new LoggingHandler(nettyLogLevel.get()));
    }
    serverBootstrap.group(bossGroup, workerGroup)
      .channel(NettyServerConfigurationUtils.resolveChannelClass(configuration))
      .childHandler(new ChannelInitializer<>() {
        @Override
        protected void initChannel(Channel channel) {
          ChannelPipeline pipeline = channel.pipeline();
          pipeline.addLast(new JsonRpcRequestDecoder(jsonb, charset))
            .addLast(new JsonRpcResponseEncoder())
            .addLast(new JsonRpcRequestHandler(serverProcessor, jsonb, charset));
        }
      });
    serverBootstrap.bind(NettyServerConfigurationUtils.getServerPortFrom(configuration));
    return new NettyJsonRpcServerFactory(bossGroup, workerGroup, procedureProcessorRegistry);
  }
}
