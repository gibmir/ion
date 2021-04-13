package com.github.gibmir.ion.lib.netty.server.common.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ssl.SslAppenderDecorator;
import com.github.gibmir.ion.lib.netty.common.configuration.decoder.FrameDecoderConfig;
import com.github.gibmir.ion.lib.netty.common.configuration.group.NettyGroupType;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import com.github.gibmir.ion.lib.netty.common.configuration.ssl.NettySslProvider;
import com.github.gibmir.ion.lib.netty.common.exceptions.NettyInitializationException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.File;
import java.nio.file.Paths;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public class NettyServerConfigurationUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerConfigurationUtils.class);

  private NettyServerConfigurationUtils() {
  }

  //server port
  //int properties
  public static final String NETTY_SERVER_SOCKET_ADDRESS_PORT = ROOT_PREFIX + ".netty.server.port";

  public static Integer getServerPortFrom(Configuration configuration) {
    Integer port = configuration.getValue(NETTY_SERVER_SOCKET_ADDRESS_PORT, Integer.class);
    LOGGER.info("Server port was received [{}]", port);
    return port;
  }

  //channel
  //string properties
  public static final String NETTY_SERVER_CHANNEL_TYPE = ROOT_PREFIX + ".netty.server.channel.type";

  public static Class<? extends ServerChannel> resolveChannelClass(Configuration configuration) {
    Class<? extends ServerChannel> serverChannelClass = configuration.getOptionalValue(NETTY_SERVER_CHANNEL_TYPE, String.class)
      .map(NettyServerChannelType::valueOf)
      .orElse(NettyServerChannelType.NIO)
      .resolveChannelClass();
    LOGGER.info("Server channel was received [{}]", serverChannelClass);
    return serverChannelClass;
  }

  //event loop group
  //string properties
  public static final String NETTY_SERVER_GROUP_TYPE = ROOT_PREFIX + ".netty.server.group.type";
  //int properties
  public static final String NETTY_SERVER_GROUP_THREADS_COUNT = ROOT_PREFIX + ".netty.server.group.threads.count";
  public static final Integer DEFAULT_NETTY_SERVER_GROUP_THREADS_COUNT = Runtime.getRuntime().availableProcessors();

  public static EventLoopGroup createEventLoopGroup(Configuration configuration) {
    NettyGroupType nettyGroupType = configuration.getOptionalValue(NETTY_SERVER_GROUP_TYPE, String.class)
      .map(NettyGroupType::valueOf)
      .orElse(NettyGroupType.NIO);
    Integer threadsCount = configuration.getOptionalValue(NETTY_SERVER_GROUP_THREADS_COUNT, Integer.class)
      .orElse(DEFAULT_NETTY_SERVER_GROUP_THREADS_COUNT);
    LOGGER.info("Netty event loop group was received. Type [{}], threads count [{}]", nettyGroupType, threadsCount);
    return createNettyGroup(nettyGroupType, threadsCount);
  }

  private static EventLoopGroup createNettyGroup(NettyGroupType nettyGroupType, Integer threadsCount) {
    switch (nettyGroupType) {
      case EPOLL:
        return new EpollEventLoopGroup(threadsCount);
      case NIO:
      default:
        return new NioEventLoopGroup(threadsCount);
    }
  }

  //log level
  //string properties
  public static final String NETTY_SERVER_LOG_LEVEL = ROOT_PREFIX + ".netty.server.log.level";

  public static NettyLogLevel resolveLogLevel(Configuration configuration) {
    NettyLogLevel nettyLogLevel = configuration.getOptionalValue(NETTY_SERVER_LOG_LEVEL, String.class)
      .map(NettyLogLevel::valueOf).orElse(NettyLogLevel.DISABLED);
    LOGGER.info("Netty log level was received [{}]", nettyLogLevel);
    return nettyLogLevel;
  }

  public static void appendLoggingTo(ServerBootstrap serverBootstrap, NettyLogLevel nettyLogLevel) {
    if (/*if log is not disabled*/!NettyLogLevel.DISABLED.equals(nettyLogLevel)) {
      serverBootstrap.handler(new LoggingHandler(nettyLogLevel.get()));
    }
  }

  //ssl
  public static ChannelHandlerAppender decorateWithSsl(ChannelHandlerAppender channelHandlerAppender,
                                                       Configuration configuration) {
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
      } catch (SSLException e) {
        throw new NettyInitializationException("Exception occurred during ssl initialization", e);
      }
    }
    return channelHandlerAppender;
  }

  //string
  public static final String NETTY_SERVER_SSL_PROVIDER =
    ROOT_PREFIX + ".netty.server.ssl.provider";

  public static NettySslProvider resolveSslProvider(Configuration configuration) {
    NettySslProvider nettySslProvider = configuration.getOptionalValue(NETTY_SERVER_SSL_PROVIDER, String.class)
      .map(NettySslProvider::valueOf).orElse(NettySslProvider.DISABLED);
    LOGGER.info("Netty ssl provider was resolved [{}]", nettySslProvider);
    return nettySslProvider;
  }

  //string
  public static final String NETTY_SERVER_SSL_TRUST_MANAGER_CERT_PATH =
    ROOT_PREFIX + ".netty.server.ssl.trust.path";

  public static File resolveTrustStore(Configuration configuration) {
    String trustManagerCertPathString = configuration.getValue(NETTY_SERVER_SSL_TRUST_MANAGER_CERT_PATH, String.class);
    LOGGER.info("Trust manager cert path was resolved [{}]", trustManagerCertPathString);
    if (trustManagerCertPathString == null) {
      //for system default
      return null;
    }
    return Paths.get(trustManagerCertPathString).toFile();
  }

  //string
  public static final String NETTY_SERVER_SSL_KEY_MANAGER_CERT_PATH =
    ROOT_PREFIX + ".netty.server.ssl.certificate.path";

  public static File resolveCertificate(Configuration configuration) {
    String keyManagerCertPathString = configuration.getValue(NETTY_SERVER_SSL_KEY_MANAGER_CERT_PATH, String.class);
    LOGGER.info("Key manager cert path was resolved [{}]", keyManagerCertPathString);
    if (keyManagerCertPathString == null) {
      //for system default
      return null;
    }
    return Paths.get(keyManagerCertPathString).toFile();
  }

  //string
  public static final String NETTY_SERVER_SSL_KEY_PATH =
    ROOT_PREFIX + ".netty.server.ssl.key.path";

  public static File resolvePrivateKey(Configuration configuration) {
    String keyManagerCertPathString = configuration.getValue(NETTY_SERVER_SSL_KEY_PATH, String.class);
    LOGGER.info("Key path was resolved [{}]", keyManagerCertPathString);
    if (keyManagerCertPathString == null) {
      //for system default
      return null;
    }
    return Paths.get(keyManagerCertPathString).toFile();
  }

  //string
  public static final String NETTY_SERVER_SSL_KEY_PASSWORD =
    ROOT_PREFIX + ".netty.server.ssl.key.password";

  public static String resolveKeyPassword(Configuration configuration) {
    String keyPassword = configuration.getValue(NETTY_SERVER_SSL_KEY_PASSWORD, String.class);
    if (keyPassword == null) {
      LOGGER.info("Key password is null");
      //for system default
      return null;
    }
    LOGGER.info("Key password is not null");
    return keyPassword;
  }

  //frame decoder
  //int
  public static final String NETTY_SERVER_FRAME_DECODER_MAX_FRAME_LENGTH =
    ROOT_PREFIX + "netty.server.frame.decoder.max.frame.length";
  public static final String NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_OFFSET =
    ROOT_PREFIX + "netty.server.frame.decoder.length.field.offset";
  public static final String NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_LENGTH =
    ROOT_PREFIX + "netty.server.frame.decoder.length.field.length";
  public static final String NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_ADJUSTMENT =
    ROOT_PREFIX + "netty.server.frame.decoder.length.field.adjustment";
  public static final String NETTY_SERVER_FRAME_DECODER_LENGTH_STRIP_BITES =
    ROOT_PREFIX + "netty.server.frame.decoder.length.strip.bites";

  public static final Integer DEFAULT_NETTY_SERVER_FRAME_DECODER_MAX_FRAME_LENGTH = 1048576;
  public static final Integer DEFAULT_NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_OFFSET = 0;
  public static final Integer DEFAULT_NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_LENGTH = 4;
  public static final Integer DEFAULT_NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_ADJUSTMENT = 0;
  public static final Integer DEFAULT_NETTY_SERVER_FRAME_DECODER_LENGTH_STRIP_BITES = 4;

  /**
   * @see io.netty.handler.codec.bytes.ByteArrayDecoder for defaults
   */
  public static FrameDecoderConfig resolveFrameDecoderConfig(Configuration configuration) {
    int maxFrameLength = configuration.getOptionalValue(NETTY_SERVER_FRAME_DECODER_MAX_FRAME_LENGTH, Integer.class)
      .orElse(DEFAULT_NETTY_SERVER_FRAME_DECODER_MAX_FRAME_LENGTH);
    int fieldOffset = configuration.getOptionalValue(NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_OFFSET, Integer.class)
      .orElse(DEFAULT_NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_OFFSET);
    int fieldLength = configuration.getOptionalValue(NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_LENGTH, Integer.class)
      .orElse(DEFAULT_NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_LENGTH);
    int adjustment = configuration.getOptionalValue(NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_ADJUSTMENT, Integer.class)
      .orElse(DEFAULT_NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_ADJUSTMENT);
    int stripBites = configuration.getOptionalValue(NETTY_SERVER_FRAME_DECODER_LENGTH_STRIP_BITES, Integer.class)
      .orElse(DEFAULT_NETTY_SERVER_FRAME_DECODER_LENGTH_STRIP_BITES);
    return new FrameDecoderConfig(maxFrameLength, fieldOffset, fieldLength, adjustment, stripBites);
  }

  //frame encoder
  //int
  public static final String NETTY_SERVER_FRAME_ENCODER_LENGTH_FIELD_LENGTH =
    ROOT_PREFIX + "netty.server.frame.encoder.length.field.length";
  public static final Integer DEFAULT_NETTY_SERVER_FRAME_ENCODER_LENGTH_FIELD_LENGTH = 4;

  public static int resolveEncoderFrameLength(Configuration configuration) {
    return configuration.getOptionalValue(NETTY_SERVER_FRAME_ENCODER_LENGTH_FIELD_LENGTH, Integer.class)
      .orElse(DEFAULT_NETTY_SERVER_FRAME_ENCODER_LENGTH_FIELD_LENGTH);
  }
}
