package com.github.gibmir.ion.lib.netty.server.factory.provider;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.cache.processor.ServerProcessor;
import com.github.gibmir.ion.api.server.cache.processor.SimpleProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.factory.configuration.ServerConfigurationUtils;
import com.github.gibmir.ion.api.server.factory.provider.JsonRpcServerFactoryProvider;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import com.github.gibmir.ion.lib.netty.common.configuration.ssl.NettySslProvider;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.JsonRpcChannelInitializer;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.server.channel.initializer.appender.JsonRpcServerChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ssl.SslAppenderDecorator;
import com.github.gibmir.ion.lib.netty.server.configuration.NettyServerConfigurationUtils;
import com.github.gibmir.ion.lib.netty.server.factory.NettyJsonRpcServerFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import javax.net.ssl.SSLException;
import java.nio.charset.Charset;
import java.util.concurrent.ConcurrentHashMap;

public class NettyJsonRpcServerFactoryProvider implements JsonRpcServerFactoryProvider {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyJsonRpcServerFactoryProvider.class);
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
      new SimpleProcedureProcessorRegistry(new ConcurrentHashMap<>());
    ServerProcessor serverProcessor = new ServerProcessor(procedureProcessorRegistry);
    Configuration configuration = ConfigurationProvider.load().provide();
    Charset charset = ServerConfigurationUtils.createCharsetWith(configuration);
    Jsonb jsonb = ConfigurationUtils.createJsonbWith(configuration);
    ServerBootstrap serverBootstrap = new ServerBootstrap();
    EventLoopGroup bossGroup = NettyServerConfigurationUtils.createEventLoopGroup(configuration);
    EventLoopGroup workerGroup = NettyServerConfigurationUtils.createEventLoopGroup(configuration);

    appendLogging(serverBootstrap, NettyServerConfigurationUtils.resolveLogLevel(configuration));
    JsonRpcChannelInitializer jsonRpcChannelInitializer =
      createJsonRpcServerChannelInitializer(serverProcessor, configuration, charset, jsonb);
    serverBootstrap.group(bossGroup, workerGroup)
      .channel(NettyServerConfigurationUtils.resolveChannelClass(configuration))
      .childHandler(jsonRpcChannelInitializer);
    serverBootstrap.bind(NettyServerConfigurationUtils.getServerPortFrom(configuration));
    return new NettyJsonRpcServerFactory(bossGroup, workerGroup, procedureProcessorRegistry);
  }

  private static JsonRpcChannelInitializer createJsonRpcServerChannelInitializer(ServerProcessor serverProcessor,
                                                                                 Configuration configuration,
                                                                                 Charset charset, Jsonb jsonb) {
    ChannelHandlerAppender channelHandlerAppender = new JsonRpcServerChannelHandlerAppender(serverProcessor, charset, jsonb);
    return new JsonRpcChannelInitializer(decorateWithSsl(channelHandlerAppender, configuration));
  }

  private static void appendLogging(ServerBootstrap serverBootstrap, NettyLogLevel nettyLogLevel) {
    if (/*if log is not disabled*/!NettyLogLevel.DISABLED.equals(nettyLogLevel)) {
      serverBootstrap.handler(new LoggingHandler(nettyLogLevel.get()));
    }
  }

  private static ChannelHandlerAppender decorateWithSsl(ChannelHandlerAppender channelHandlerAppender, Configuration configuration) {
    NettySslProvider sslProvider = NettyServerConfigurationUtils.resolveSslProvider(configuration);
    if (/*if ssl is enabled*/!NettySslProvider.DISABLED.equals(sslProvider)) {
      try {
        SslContext sslContext = SslContextBuilder.forServer(NettyServerConfigurationUtils.resolveCertificate(configuration),
          NettyServerConfigurationUtils.resolvePrivateKey(configuration),
          NettyServerConfigurationUtils.resolveKeyPassword(configuration))
          .sslProvider(sslProvider.get())
          .trustManager(NettyServerConfigurationUtils.resolveTrustStore(configuration))
          .build();
        return SslAppenderDecorator.decorate(channelHandlerAppender, sslContext);
        //todo initialization exception
      } catch (SSLException e) {
        LOGGER.error("Exception occurred while server initialize ssl.", e);
      }
    }
    return channelHandlerAppender;
  }
}
