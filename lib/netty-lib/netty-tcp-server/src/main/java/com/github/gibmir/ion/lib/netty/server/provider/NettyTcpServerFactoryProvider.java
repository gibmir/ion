package com.github.gibmir.ion.lib.netty.server.provider;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.api.server.factory.configuration.ServerConfigurationUtils;
import com.github.gibmir.ion.api.server.factory.provider.JsonRpcServerFactoryProvider;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessorFactory;
import com.github.gibmir.ion.api.server.processor.request.registry.ProcedureProcessorRegistry;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.JsonRpcChannelInitializer;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.common.configuration.decoder.FrameDecoderConfig;
import com.github.gibmir.ion.lib.netty.server.appender.TcpServerChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.server.common.channel.codecs.encoder.ResponseEncoder;
import com.github.gibmir.ion.lib.netty.server.common.configuration.NettyServerConfigurationUtils;
import com.github.gibmir.ion.lib.netty.server.common.factory.NettyJsonRpcServerFactory;
import com.github.gibmir.ion.lib.netty.server.common.processor.ServerProcessor;
import com.github.gibmir.ion.lib.netty.server.common.processor.factory.NettyProcedureProcessorFactory;
import com.github.gibmir.ion.lib.netty.server.common.processor.registry.NettyProcedureProcessorRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

public final class NettyTcpServerFactoryProvider implements JsonRpcServerFactoryProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyTcpServerFactoryProvider.class);
  private static volatile NettyJsonRpcServerFactory nettyJsonRpcServerFactory;

  @Override
  public NettyJsonRpcServerFactory provide() {
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
      new NettyProcedureProcessorRegistry(new ConcurrentHashMap<>());
    ServerProcessor serverProcessor = new ServerProcessor(LoggerFactory.getLogger(ServerProcessor.class),
      procedureProcessorRegistry);
    Configuration configuration = ConfigurationProvider.load().provide();
    Charset charset = ServerConfigurationUtils.createCharsetWith(configuration);
    Jsonb jsonb = ConfigurationUtils.createJsonbWith(configuration);
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    EventLoopGroup bossGroup = NettyServerConfigurationUtils.createEventLoopGroup(configuration);
    EventLoopGroup workerGroup = NettyServerConfigurationUtils.createEventLoopGroup(configuration);
    NettyServerConfigurationUtils.appendLoggingTo(serverBootstrap,
      NettyServerConfigurationUtils.resolveLogLevel(configuration));
    FrameDecoderConfig frameDecoderConfig = NettyServerConfigurationUtils.resolveFrameDecoderConfig(configuration);
    int encoderFrameLength = NettyServerConfigurationUtils.resolveEncoderFrameLength(configuration);
    JsonRpcChannelInitializer jsonRpcChannelInitializer =
      createJsonRpcServerChannelInitializer(serverProcessor, configuration, charset, jsonb, frameDecoderConfig,
        encoderFrameLength);
    serverBootstrap.group(bossGroup, workerGroup)
      .channel(NettyServerConfigurationUtils.resolveChannelClass(configuration))
      .childHandler(jsonRpcChannelInitializer);
    ProcedureProcessorFactory procedureProcessorFactory = new NettyProcedureProcessorFactory(jsonb, charset);
    serverBootstrap.bind(NettyServerConfigurationUtils.getServerPortFrom(configuration));
    LOGGER.info("Ion tcp json-rpc 2.0 server is ready to run");
    return new NettyJsonRpcServerFactory(LoggerFactory.getLogger(NettyJsonRpcServerFactory.class), bossGroup,
      workerGroup, procedureProcessorRegistry, procedureProcessorFactory);
  }

  private static JsonRpcChannelInitializer createJsonRpcServerChannelInitializer(final ServerProcessor serverProcessor,
                                                                                 final Configuration configuration,
                                                                                 final Charset charset, final Jsonb jsonb,
                                                                                 final FrameDecoderConfig decoderConfig,
                                                                                 final int encoderFrameLength) {
    ChannelHandlerAppender channelHandlerAppender = new TcpServerChannelHandlerAppender(serverProcessor, charset,
      jsonb, decoderConfig, encoderFrameLength, LoggerFactory.getLogger(ResponseEncoder.class));
    return new JsonRpcChannelInitializer(NettyServerConfigurationUtils.decorateWithSsl(channelHandlerAppender,
      configuration));
  }
}
