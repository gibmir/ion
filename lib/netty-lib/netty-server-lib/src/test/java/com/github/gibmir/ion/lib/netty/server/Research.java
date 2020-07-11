package com.github.gibmir.ion.lib.netty.server;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.server.cache.processor.SimpleProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.cache.signature.SignatureRegistry;
import com.github.gibmir.ion.api.server.cache.signature.SimpleSignatureRegistry;
import com.github.gibmir.ion.lib.netty.common.configuration.group.TestProcedure;
import com.github.gibmir.ion.lib.netty.server.codecs.decoder.JsonRpcRequestDecoder;
import com.github.gibmir.ion.lib.netty.server.codecs.encoder.JsonRpcResponseEncoder;
import com.github.gibmir.ion.lib.netty.server.handler.JsonRpcRequestHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

public class Research {


  @Test
  void smoke() {
    SignatureRegistry signatureRegistry = new SimpleSignatureRegistry(new ConcurrentHashMap<>());
    SimpleProcedureProcessorRegistry simpleProcedureProcessorRegistry = new SimpleProcedureProcessorRegistry(new ConcurrentHashMap<>());
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(signatureRegistry, simpleProcedureProcessorRegistry);
    nettyJsonRpcServer.registerProcedureProcessor(TestProcedure.class, String::toUpperCase);
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();
    Jsonb jsonb = JsonbBuilder.create();
    Charset charset = StandardCharsets.UTF_8;
    try {
      serverBootstrap.group(bossGroup, workerGroup)
        .channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.DEBUG))
        .childHandler(new ChannelInitializer<>() {
          @Override
          protected void initChannel(Channel channel) {
            ChannelPipeline pipeline = channel.pipeline();
            pipeline.addLast(new JsonRpcRequestDecoder(jsonb, charset, signatureRegistry))
              .addLast(new JsonRpcResponseEncoder(jsonb, charset))
              .addLast(new JsonRpcRequestHandler(simpleProcedureProcessorRegistry));
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
  }
}
