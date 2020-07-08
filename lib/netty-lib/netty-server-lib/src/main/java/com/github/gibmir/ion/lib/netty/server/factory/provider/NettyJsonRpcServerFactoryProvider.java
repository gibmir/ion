package com.github.gibmir.ion.lib.netty.server.factory.provider;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.cache.processor.SimpleProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.cache.signature.SignatureRegistry;
import com.github.gibmir.ion.api.server.cache.signature.SimpleSignatureRegistry;
import com.github.gibmir.ion.api.server.factory.JsonRpcServerFactory;
import com.github.gibmir.ion.api.server.factory.configuration.ServerConfigurationUtils;
import com.github.gibmir.ion.api.server.factory.provider.JsonRpcServerFactoryProvider;
import com.github.gibmir.ion.lib.netty.server.codecs.decoder.JsonRpcRequestDecoder;
import com.github.gibmir.ion.lib.netty.server.codecs.encoder.JsonRpcResponseEncoder;
import com.github.gibmir.ion.lib.netty.server.configuration.NettyServerConfigurationUtils;
import com.github.gibmir.ion.lib.netty.server.factory.NettyJsonRpcServerFactory;
import com.github.gibmir.ion.lib.netty.server.handler.JsonRpcRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.logging.LoggingHandler;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.HashMap;

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
    Configuration configuration = ConfigurationProvider.load().provide();
    SignatureRegistry signatureRegistry = new SimpleSignatureRegistry(new HashMap<>());
    ProcedureProcessorRegistry procedureProcessorRegistry = new SimpleProcedureProcessorRegistry(new HashMap<>());
    Charset charset = ServerConfigurationUtils.createCharsetWith(configuration);
    Jsonb jsonb = ConfigurationUtils.createJsonbWith(configuration);
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    EventLoopGroup bossGroup = NettyServerConfigurationUtils.createEventLoopGroup(configuration);
    EventLoopGroup workerGroup = NettyServerConfigurationUtils.createEventLoopGroup(configuration);
    try {
      serverBootstrap.group(bossGroup, workerGroup)
        .channel(NettyServerConfigurationUtils.resolveChannelClass(configuration))
        .handler(new LoggingHandler(NettyServerConfigurationUtils.resolveLogLevel(configuration)))
        .childHandler(new ChannelInitializer<>() {
          @Override
          protected void initChannel(Channel channel) {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(new JsonRpcRequestDecoder(jsonb, charset, signatureRegistry))
              .addLast(new JsonRpcResponseEncoder(jsonb, charset))
              .addLast(new JsonRpcRequestHandler(procedureProcessorRegistry));
          }
        });
      serverBootstrap.bind(NettyServerConfigurationUtils.getServerPortFrom(configuration)).sync().channel().closeFuture().sync();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Can't start server", e);
    } finally {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    }
    return new NettyJsonRpcServerFactory(signatureRegistry, procedureProcessorRegistry);
  }
}
