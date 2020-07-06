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
import com.github.gibmir.ion.lib.netty.server.factory.NettyJsonRpcServerFactory;
import com.github.gibmir.ion.lib.netty.server.handler.JsonRpcRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.HashMap;

public class NettyJsonRpcServerFactoryProvider implements JsonRpcServerFactoryProvider {
  @Override
  public JsonRpcServerFactory provide() {
    Configuration configuration = ConfigurationProvider.load().provide();
    //todo registry module. Provide through SPI
    SignatureRegistry signatureRegistry = new SimpleSignatureRegistry(new HashMap<>());
    ProcedureProcessorRegistry procedureProcessorRegistry = new SimpleProcedureProcessorRegistry(new HashMap<>());
    Charset charset = ServerConfigurationUtils.createCharsetWith(configuration);
    Jsonb jsonb = ConfigurationUtils.createJsonbWith(configuration);
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    NioEventLoopGroup bossGroup = new NioEventLoopGroup();
    NioEventLoopGroup workerGroup = new NioEventLoopGroup();

    try {
      serverBootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(new ChannelInitializer<>() {
          @Override
          protected void initChannel(Channel channel) {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(new JsonRpcRequestDecoder(jsonb, charset, signatureRegistry))
              .addLast(new JsonRpcResponseEncoder(jsonb, charset))
              .addLast(new JsonRpcRequestHandler(procedureProcessorRegistry));
          }
        });
      serverBootstrap.bind(52_222).sync().channel().closeFuture().sync();
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
