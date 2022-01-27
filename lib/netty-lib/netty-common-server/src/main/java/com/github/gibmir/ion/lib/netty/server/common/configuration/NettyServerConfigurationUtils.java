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

import java.io.File;
import java.nio.file.Paths;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public final class NettyServerConfigurationUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerConfigurationUtils.class);

  private NettyServerConfigurationUtils() {
  }

  //server port
  //int properties
  public static final String NETTY_SERVER_SOCKET_ADDRESS_PORT = ROOT_PREFIX + ".netty.server.port";

  /**
   * @param configuration application config
   * @return server port
   */
  public static Integer getServerPortFrom(final Configuration configuration) {
    Integer port = configuration.getValue(NETTY_SERVER_SOCKET_ADDRESS_PORT, Integer.class);
    if (port == null) {
      throw new IllegalArgumentException("Please, specify [" + NETTY_SERVER_SOCKET_ADDRESS_PORT + "]");
    }
    LOGGER.info("Server port was received [{}]", port);
    return port;
  }

  //channel
  //string properties
  public static final String NETTY_SERVER_CHANNEL_TYPE = ROOT_PREFIX + ".netty.server.channel.type";

  /**
   * @param configuration application config
   * @return server channel class
   * @implNote return {@link NettyServerChannelType#NIO} by default
   */
  public static Class<? extends ServerChannel> resolveChannelClass(final Configuration configuration) {
    Class<? extends ServerChannel> serverChannelClass = configuration.getOptionalValue(NETTY_SERVER_CHANNEL_TYPE, String.class)
      .map(NettyServerChannelType::valueOf)
      .orElse(NettyServerChannelType.NIO)
      .resolveChannelClass();
    LOGGER.info("Server channel was resolved [{}]", serverChannelClass);
    return serverChannelClass;
  }

  //event loop group
  //string properties
  public static final String NETTY_SERVER_GROUP_TYPE = ROOT_PREFIX + ".netty.server.group.type";
  //int properties
  public static final String NETTY_SERVER_GROUP_THREADS_COUNT = ROOT_PREFIX + ".netty.server.group.threads.count";
  public static final Integer DEFAULT_NETTY_SERVER_GROUP_THREADS_COUNT = Runtime.getRuntime().availableProcessors();

  /**
   * @param configuration application config
   * @return event loop group
   * @implNote return {@link NettyGroupType#NIO} by default
   */
  public static EventLoopGroup createEventLoopGroup(final Configuration configuration) {
    NettyGroupType nettyGroupType = configuration.getOptionalValue(NETTY_SERVER_GROUP_TYPE, String.class)
      .map(NettyGroupType::valueOf)
      .orElse(NettyGroupType.NIO);
    Integer threadsCount = configuration.getOptionalValue(NETTY_SERVER_GROUP_THREADS_COUNT, Integer.class)
      .orElse(DEFAULT_NETTY_SERVER_GROUP_THREADS_COUNT);
    LOGGER.info("Netty event loop group was received. Type [{}], threads count [{}]", nettyGroupType, threadsCount);
    return createNettyGroup(nettyGroupType, threadsCount);
  }

  private static EventLoopGroup createNettyGroup(final NettyGroupType nettyGroupType, final Integer threadsCount) {
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

  /**
   * @param configuration application config
   * @return resolved log level
   * @implNote disabled by default
   */
  public static NettyLogLevel resolveLogLevel(final Configuration configuration) {
    NettyLogLevel nettyLogLevel = configuration.getOptionalValue(NETTY_SERVER_LOG_LEVEL, String.class)
      .map(NettyLogLevel::valueOf).orElse(NettyLogLevel.DISABLED);
    LOGGER.info("Netty log level was received [{}]", nettyLogLevel);
    return nettyLogLevel;
  }

  /**
   * Appends logging(if enabled) to server.
   *
   * @param serverBootstrap netty server bootstrap
   * @param nettyLogLevel   log level
   */
  public static void appendLoggingTo(final ServerBootstrap serverBootstrap, final NettyLogLevel nettyLogLevel) {
    if (/*if log is not disabled*/!NettyLogLevel.DISABLED.equals(nettyLogLevel)) {
      LOGGER.info("Appending Netty Logging with level [{}]", nettyLogLevel);
      serverBootstrap.handler(new LoggingHandler(nettyLogLevel.get()));
    }
  }

  //ssl

  /**
   * Decorates handler with ssl.
   *
   * @param channelHandlerAppender handler appender
   * @param configuration          application config
   * @return handler decorated with ssl(if enabled)
   */
  public static ChannelHandlerAppender decorateWithSsl(final ChannelHandlerAppender channelHandlerAppender,
                                                       final Configuration configuration) {
    NettySslProvider sslProvider = NettyServerConfigurationUtils.resolveSslProvider(configuration);
    if (/*if ssl is enabled*/!NettySslProvider.DISABLED.equals(sslProvider)) {
      LOGGER.info("Appending SSL with provider [{}]", sslProvider);
      try {
        SslContext sslContext = SslContextBuilder.forServer(NettyServerConfigurationUtils.resolveCertificate(configuration),
            NettyServerConfigurationUtils.resolvePrivateKey(configuration),
            NettyServerConfigurationUtils.resolveKeyPassword(configuration))
          .sslProvider(sslProvider.get())
          .trustManager(NettyServerConfigurationUtils.resolveTrustStore(configuration))
          .build();
        return SslAppenderDecorator.decorate(channelHandlerAppender, sslContext);
      } catch (Exception e) {
        throw new NettyInitializationException("Exception occurred during ssl initialization", e);
      }
    }
    return channelHandlerAppender;
  }

  //string
  public static final String NETTY_SERVER_SSL_PROVIDER =
    ROOT_PREFIX + ".netty.server.ssl.provider";

  /**
   * @param configuration application config
   * @return netty ssl provider
   * @implNote return {@link NettySslProvider#DISABLED} by default
   */
  public static NettySslProvider resolveSslProvider(final Configuration configuration) {
    NettySslProvider nettySslProvider = configuration.getOptionalValue(NETTY_SERVER_SSL_PROVIDER, String.class)
      .map(NettySslProvider::valueOf).orElse(NettySslProvider.DISABLED);
    LOGGER.info("Netty ssl provider was resolved [{}]", nettySslProvider);
    return nettySslProvider;
  }

  //string
  public static final String NETTY_SERVER_SSL_TRUST_MANAGER_CERT_PATH =
    ROOT_PREFIX + ".netty.server.ssl.trust.path";

  /**
   * @param configuration application config
   * @return trust store
   */
  public static File resolveTrustStore(final Configuration configuration) {
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

  /**
   * @param configuration application config
   * @return key manager certificate
   */
  public static File resolveCertificate(final Configuration configuration) {
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

  /**
   * @param configuration application config
   * @return private key
   */
  public static File resolvePrivateKey(final Configuration configuration) {
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

  /**
   * @param configuration application config
   * @return key password
   */
  public static String resolveKeyPassword(final Configuration configuration) {
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
   * @param configuration application config
   * @return decoder config
   * @see io.netty.handler.codec.bytes.ByteArrayDecoder for defaults
   */
  public static FrameDecoderConfig resolveFrameDecoderConfig(final Configuration configuration) {
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

  /**
   * @param configuration application config
   * @return encoder frame length
   */
  public static int resolveEncoderFrameLength(final Configuration configuration) {
    return configuration.getOptionalValue(NETTY_SERVER_FRAME_ENCODER_LENGTH_FIELD_LENGTH, Integer.class)
      .orElse(DEFAULT_NETTY_SERVER_FRAME_ENCODER_LENGTH_FIELD_LENGTH);
  }
}
