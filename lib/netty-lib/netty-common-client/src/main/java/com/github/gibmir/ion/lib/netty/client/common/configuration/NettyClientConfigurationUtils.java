package com.github.gibmir.ion.lib.netty.client.common.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.logging.LoggingAppenderDecorator;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ssl.SslAppenderDecorator;
import com.github.gibmir.ion.lib.netty.common.configuration.decoder.FrameDecoderConfig;
import com.github.gibmir.ion.lib.netty.common.configuration.group.NettyGroupType;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import com.github.gibmir.ion.lib.netty.common.configuration.ssl.NettySslProvider;
import com.github.gibmir.ion.lib.netty.common.exceptions.NettyInitializationException;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLException;
import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.nio.file.Paths;
import java.time.Duration;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public final class NettyClientConfigurationUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientConfigurationUtils.class);

  private NettyClientConfigurationUtils() {
  }

  /*socket address*/
  //int properties
  public static final String NETTY_CLIENT_SOCKET_ADDRESS_PORT = ROOT_PREFIX + ".netty.client.socket.address.port";
  //string properties
  public static final String NETTY_CLIENT_SOCKET_ADDRESS_HOST = ROOT_PREFIX + ".netty.client.socket.address.host";

  /**
   * @param configuration application configuration
   * @return socket address from specified configuration
   */
  public static SocketAddress createSocketAddressWith(final Configuration configuration) {
    String socketAddressHost = configuration.getValue(NETTY_CLIENT_SOCKET_ADDRESS_HOST, String.class);
    Integer socketAddressPort = configuration.getValue(NETTY_CLIENT_SOCKET_ADDRESS_PORT, Integer.class);
    String address = String.format("host:port [%s:%s]", socketAddressHost, socketAddressPort);
    LOGGER.info("Socket address was received. {}", address);
    if (socketAddressHost == null || socketAddressPort == null) {
      throw new IllegalArgumentException(String.format("Socket address [%s] was configured incorrectly", address));
    }
    return new InetSocketAddress(socketAddressHost, socketAddressPort);
  }

  /*uri*/
  //string properties
  public static final String NETTY_CLIENT_URI = ROOT_PREFIX + ".netty.client.uri";

  /**
   * @param configuration application config
   * @return uri from config
   */
  public static URI createUriWith(final Configuration configuration) {
    String uri = configuration.getValue(NETTY_CLIENT_URI, String.class);
    LOGGER.info("URI [{}] was resolved from configuration", uri);
    if (uri == null || uri.isBlank()) {
      throw new IllegalArgumentException(String.format("URI [%s] was resolved incorrectly", uri));
    }
    return URI.create(uri);
  }

  /*channel type*/
  //string properties
  public static final String NETTY_CLIENT_CHANNEL_TYPE = ROOT_PREFIX + ".netty.client.channel.type";

  /**
   * @param configuration application configuration
   * @return channel class
   */
  public static Class<? extends Channel> resolveChannelClass(final Configuration configuration) {
    Class<? extends Channel> channelClass = configuration.getOptionalValue(NETTY_CLIENT_CHANNEL_TYPE, String.class)
      .map(NettyClientChannelType::valueOf)
      .orElse(NettyClientChannelType.NIO)
      .resolveChannelClass();
    LOGGER.info("Channel class was resolved [{}]", channelClass);
    return channelClass;
  }

  /*event loop group*/
  //int properties
  public static final String NETTY_CLIENT_GROUP_THREADS_COUNT = ROOT_PREFIX + ".netty.client.group.threads.count";
  //string properties
  public static final String NETTY_CLIENT_GROUP_TYPE = ROOT_PREFIX + ".netty.client.group.type";
  //defaults
  public static final Integer DEFAULT_NETTY_CLIENT_GROUP_THREADS_COUNT = Runtime.getRuntime().availableProcessors();

  /**
   * @param configuration application configuration
   * @return event loop group
   */
  public static EventLoopGroup createEventLoopGroup(final Configuration configuration) {
    NettyGroupType nettyGroupType = configuration.getOptionalValue(NETTY_CLIENT_GROUP_TYPE, String.class)
      .map(NettyGroupType::valueOf)
      .orElse(NettyGroupType.NIO);
    Integer threadsCount = configuration.getOptionalValue(NETTY_CLIENT_GROUP_THREADS_COUNT, Integer.class)
      .orElse(DEFAULT_NETTY_CLIENT_GROUP_THREADS_COUNT);
    LOGGER.info("Netty event loop group was received. Type [{}], threads count [{}]", nettyGroupType, threadsCount);
    return createGroup(nettyGroupType, threadsCount);
  }

  private static EventLoopGroup createGroup(final NettyGroupType nettyGroupType, final Integer threadsCount) {
    switch (nettyGroupType) {
      case EPOLL:
        return new EpollEventLoopGroup(threadsCount);
      case NIO:
      default:
        return new NioEventLoopGroup(threadsCount);
    }
  }

  /*log level*/
  //string properties
  public static final String NETTY_CLIENT_LOG_LEVEL = ROOT_PREFIX + ".netty.client.log.level";

  /**
   * @param configuration application configuration
   * @return resolved log level
   */
  public static NettyLogLevel resolveLogLevel(final Configuration configuration) {
    NettyLogLevel nettyLogLevel = configuration.getOptionalValue(NETTY_CLIENT_LOG_LEVEL, String.class)
      .map(NettyLogLevel::valueOf)
      .orElse(NettyLogLevel.DISABLED);
    LOGGER.info("Netty log level was received [{}]", nettyLogLevel);
    return nettyLogLevel;
  }

  /**
   * @param configuration          application configuration
   * @param channelHandlerAppender handler appender
   * @return decorated channel handler
   */
  public static ChannelHandlerAppender appendLogging(final Configuration configuration,
                                                     final ChannelHandlerAppender channelHandlerAppender) {
    NettyLogLevel level = NettyClientConfigurationUtils.resolveLogLevel(configuration);
    if (/*if logging is not disabled*/!NettyLogLevel.DISABLED.equals(level)) {
      return LoggingAppenderDecorator.decorate(channelHandlerAppender, level.get());
    }
    return channelHandlerAppender;
  }

  /*caffeine*/
  //int properties
  public static final String NETTY_CLIENT_RESPONSE_CACHE_EVICTION_TIMEOUT =
    ROOT_PREFIX + ".netty.client.response.cache.eviction.timeout";
  public static final String NETTY_CLIENT_RESPONSE_CACHE_EVICTION_SIZE =
    ROOT_PREFIX + ".netty.client.response.cache.eviction.size";
  //boolean properties
  public static final String NETTY_CLIENT_RESPONSE_CACHE_WEAK_REFERENCE_ENABLED =
    ROOT_PREFIX + ".netty.client.response.cache.weak.reference.enabled";
  public static final String NETTY_CLIENT_RESPONSE_CACHE_SOFT_VALUES_ENABLED =
    ROOT_PREFIX + ".netty.client.response.cache.soft.values.enabled";
  public static final String NETTY_CLIENT_RESPONSE_CACHE_RECORD_STATS_ENABLED =
    ROOT_PREFIX + ".netty.client.response.cache.record.stats.enabled";
  //defaults
  public static final int DEFAULT_EVICTION_TIMEOUT = 60_000;

  /**
   * @param configuration application configuration
   * @return response futures cache
   */
  public static Cache<String, ResponseFuture> createResponseFuturesCache(final Configuration configuration) {
    Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
    Integer evictionTimeout = configuration.getOptionalValue(NETTY_CLIENT_RESPONSE_CACHE_EVICTION_TIMEOUT, Integer.class)
      .orElse(DEFAULT_EVICTION_TIMEOUT);
    caffeine.expireAfterWrite(Duration.ofMillis(evictionTimeout));
    configuration.getOptionalValue(NETTY_CLIENT_RESPONSE_CACHE_EVICTION_SIZE, Integer.class)
      .ifPresent(caffeine::maximumSize);
    if (configuration.getOptionalValue(NETTY_CLIENT_RESPONSE_CACHE_WEAK_REFERENCE_ENABLED, Boolean.class)
      .orElse(false)) {
      caffeine.weakKeys();
      caffeine.weakValues();
    }
    if (configuration.getOptionalValue(NETTY_CLIENT_RESPONSE_CACHE_SOFT_VALUES_ENABLED, Boolean.class)
      .orElse(false)) {
      caffeine.softValues();
    }
    if (configuration.getOptionalValue(NETTY_CLIENT_RESPONSE_CACHE_RECORD_STATS_ENABLED, Boolean.class)
      .orElse(false)) {
      caffeine.softValues();
    }
    LOGGER.info("Caffeine response futures cache was received [{}]", caffeine);
    return caffeine.build();
  }

  /*ssl*/
  //string
  public static final String NETTY_CLIENT_SSL_PROVIDER =
    ROOT_PREFIX + ".netty.client.ssl.provider";

  /**
   * @param configuration application configuration
   * @return resolved ssl provider
   */
  public static NettySslProvider resolveSslProvider(final Configuration configuration) {
    NettySslProvider nettySslProvider = configuration.getOptionalValue(NETTY_CLIENT_SSL_PROVIDER, String.class)
      .map(NettySslProvider::valueOf).orElse(NettySslProvider.DISABLED);
    LOGGER.info("Netty ssl provider was resolved [{}]", nettySslProvider);
    return nettySslProvider;
  }

  //string
  public static final String NETTY_CLIENT_SSL_TRUST_STORE_PATH =
    ROOT_PREFIX + ".netty.client.ssl.trust.path";

  /**
   * @param configuration application configuration
   * @return resolved trust store
   */
  public static File resolveTrustStore(final Configuration configuration) {
    String trustManagerCertPathString = configuration.getValue(NETTY_CLIENT_SSL_TRUST_STORE_PATH, String.class);
    LOGGER.info("Trust store path was resolved [{}]", trustManagerCertPathString);
    if (trustManagerCertPathString == null) {
      //for system default
      return null;
    }
    File file = Paths.get(trustManagerCertPathString).toFile();
    if (/*file is not exists*/!file.exists()) {
      String message = String.format("Trust store was resolved incorrectly. File [%s] doesn't exists",
        trustManagerCertPathString);
      throw new IllegalArgumentException(message);
    }
    return file;
  }

  //string
  public static final String NETTY_CLIENT_SSL_KEY_MANAGER_CERT_PATH =
    ROOT_PREFIX + ".netty.client.ssl.key.store.path";

  /**
   * @param configuration application config
   * @return resolved key store
   */
  public static File resolveKeyStore(final Configuration configuration) {
    String keyManagerCertPathString = configuration.getValue(NETTY_CLIENT_SSL_KEY_MANAGER_CERT_PATH, String.class);
    LOGGER.info("Key store path was resolved [{}]", keyManagerCertPathString);
    if (keyManagerCertPathString == null) {
      //for system default
      return null;
    }
    File file = Paths.get(keyManagerCertPathString).toFile();
    if (/*file is not exists*/!file.exists()) {
      String message = String.format("Key manager was resolved incorrectly. File [%s] doesn't exists",
        keyManagerCertPathString);
      throw new IllegalArgumentException(message);
    }
    return file;
  }

  //string
  public static final String NETTY_CLIENT_SSL_KEY_PATH =
    ROOT_PREFIX + ".netty.client.ssl.key.path";

  /**
   * @param configuration application config
   * @return key
   */
  public static File resolveKey(final Configuration configuration) {
    String keyManagerCertPathString = configuration.getValue(NETTY_CLIENT_SSL_KEY_PATH, String.class);
    LOGGER.info("Key path was resolved [{}]", keyManagerCertPathString);
    if (keyManagerCertPathString == null) {
      //for system default
      return null;
    }
    File file = Paths.get(keyManagerCertPathString).toFile();
    if (/*file is not exists*/!file.exists()) {
      String message = String.format("Private key was resolved incorrectly. File [%s] doesn't exists",
        keyManagerCertPathString);
      throw new IllegalArgumentException(message);
    }
    return file;
  }

  //string
  public static final String NETTY_CLIENT_SSL_KEY_PASSWORD =
    ROOT_PREFIX + ".netty.client.ssl.key.password";

  /**
   * @param configuration application config
   * @return resolved key password
   */
  public static String resolveKeyPassword(final Configuration configuration) {
    String keyPassword = configuration.getValue(NETTY_CLIENT_SSL_KEY_PASSWORD, String.class);
    if (keyPassword == null) {
      LOGGER.info("Key password is null");
      //for system default
      return null;
    }
    LOGGER.info("Key password is not null");
    return keyPassword;
  }

  //string
  public static final String NETTY_CLIENT_SSL_AUTH_MODE =
    ROOT_PREFIX + ".netty.client.ssl.auth";

  /**
   * @param configuration application config
   * @return resolved client auth
   */
  public static ClientAuth resolveClientAuth(final Configuration configuration) {
    ClientAuth clientAuth = configuration.getOptionalValue(NETTY_CLIENT_SSL_AUTH_MODE, String.class)
      .map(ClientAuth::valueOf)
      .orElse(ClientAuth.NONE);
    LOGGER.info("Client auth was resolved [{}]", clientAuth);
    return clientAuth;
  }

  /**
   * @param configuration          application config
   * @param channelHandlerAppender handler appender
   * @return handler appender decorated with ssl
   */
  public static ChannelHandlerAppender appendSsl(final Configuration configuration,
                                                 final ChannelHandlerAppender channelHandlerAppender) {
    NettySslProvider sslProvider = NettyClientConfigurationUtils.resolveSslProvider(configuration);
    if (/*if ssl is enabled*/!sslProvider.equals(NettySslProvider.DISABLED)) {
      try {
        SslContext sslContext = SslContextBuilder.forClient()
          .sslProvider(sslProvider.get())
          .trustManager(NettyClientConfigurationUtils.resolveTrustStore(configuration))
          .keyManager(NettyClientConfigurationUtils.resolveKeyStore(configuration),
            NettyClientConfigurationUtils.resolveKey(configuration),
            NettyClientConfigurationUtils.resolveKeyPassword(configuration))
          .clientAuth(NettyClientConfigurationUtils.resolveClientAuth(configuration))
          .build();
        return SslAppenderDecorator.decorate(channelHandlerAppender, sslContext);
      } catch (SSLException e) {
        throw new NettyInitializationException("Exception occurred during ssl initialization", e);
      }
    }
    return channelHandlerAppender;
  }


  //frame decoder
  public static final String NETTY_CLIENT_FRAME_DECODER_MAX_FRAME_LENGTH =
    ROOT_PREFIX + "netty.client.frame.decoder.max.frame.length";
  public static final String NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_OFFSET =
    ROOT_PREFIX + "netty.client.frame.decoder.length.field.offset";
  public static final String NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_LENGTH =
    ROOT_PREFIX + "netty.client.frame.decoder.length.field.length";
  public static final String NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_ADJUSTMENT =
    ROOT_PREFIX + "netty.client.frame.decoder.length.field.adjustment";
  public static final String NETTY_CLIENT_FRAME_DECODER_LENGTH_STRIP_BITES =
    ROOT_PREFIX + "netty.client.frame.decoder.length.strip.bites";

  public static final Integer DEFAULT_NETTY_CLIENT_FRAME_DECODER_MAX_FRAME_LENGTH = 1048576;
  public static final Integer DEFAULT_NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_OFFSET = 0;
  public static final Integer DEFAULT_NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_LENGTH = 4;
  public static final Integer DEFAULT_NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_ADJUSTMENT = 0;
  public static final Integer DEFAULT_NETTY_CLIENT_FRAME_DECODER_LENGTH_STRIP_BITES = 4;

  /**
   * @param configuration application config
   * @return resolved frame decoder config
   * @see io.netty.handler.codec.bytes.ByteArrayDecoder for defaults
   */
  public static FrameDecoderConfig resolveFrameDecoderConfig(final Configuration configuration) {
    int maxFrameLength = configuration.getOptionalValue(NETTY_CLIENT_FRAME_DECODER_MAX_FRAME_LENGTH, Integer.class)
      .orElse(DEFAULT_NETTY_CLIENT_FRAME_DECODER_MAX_FRAME_LENGTH);
    int fieldOffset = configuration.getOptionalValue(NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_OFFSET, Integer.class)
      .orElse(DEFAULT_NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_OFFSET);
    int fieldLength = configuration.getOptionalValue(NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_LENGTH, Integer.class)
      .orElse(DEFAULT_NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_LENGTH);
    int adjustment = configuration.getOptionalValue(NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_ADJUSTMENT, Integer.class)
      .orElse(DEFAULT_NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_ADJUSTMENT);
    int stripBites = configuration.getOptionalValue(NETTY_CLIENT_FRAME_DECODER_LENGTH_STRIP_BITES, Integer.class)
      .orElse(DEFAULT_NETTY_CLIENT_FRAME_DECODER_LENGTH_STRIP_BITES);
    return new FrameDecoderConfig(maxFrameLength, fieldOffset, fieldLength, adjustment, stripBites);
  }

  //frame encoder

  public static final String NETTY_CLIENT_FRAME_ENCODER_LENGTH_FIELD_LENGTH =
    ROOT_PREFIX + "netty.client.frame.encoder.length.field.length";
  public static final Integer DEFAULT_NETTY_CLIENT_FRAME_ENCODER_LENGTH_FIELD_LENGTH = 4;

  /**
   * @param configuration application config
   * @return resolved encoder frame length
   */
  public static int resolveEncoderFrameLength(final Configuration configuration) {
    return configuration.getOptionalValue(NETTY_CLIENT_FRAME_ENCODER_LENGTH_FIELD_LENGTH, Integer.class)
      .orElse(DEFAULT_NETTY_CLIENT_FRAME_ENCODER_LENGTH_FIELD_LENGTH);
  }
}
