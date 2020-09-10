package com.github.gibmir.ion.lib.netty.client.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.lib.netty.client.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.common.configuration.group.NettyGroupType;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import com.github.gibmir.ion.lib.netty.common.configuration.ssl.NettySslProvider;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.ssl.ClientAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Paths;
import java.time.Duration;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public class NettyClientConfigurationUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyClientConfigurationUtils.class);

  private NettyClientConfigurationUtils() {
  }

  /*socket address*/
  //int properties
  public static final String NETTY_CLIENT_SOCKET_ADDRESS_PORT = ROOT_PREFIX + ".netty.client.socket.address.port";
  //string properties
  public static final String NETTY_CLIENT_SOCKET_ADDRESS_HOST = ROOT_PREFIX + ".netty.client.socket.address.host";

  public static SocketAddress createSocketAddressWith(Configuration configuration) {
    String socketAddressHost = configuration.getValue(NETTY_CLIENT_SOCKET_ADDRESS_HOST, String.class);

    Integer socketAddressPort = configuration.getValue(NETTY_CLIENT_SOCKET_ADDRESS_PORT, Integer.class);
    LOGGER.info("Socket address was received. Host:port [{}:{}]", socketAddressHost, socketAddressPort);
    return new InetSocketAddress(socketAddressHost, socketAddressPort);
  }

  /*channel type*/
  //string properties
  public static final String NETTY_CLIENT_CHANNEL_TYPE = ROOT_PREFIX + ".netty.client.channel.type";

  public static Class<? extends Channel> resolveChannelClass(Configuration configuration) {
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

  public static EventLoopGroup createEventLoopGroup(Configuration configuration) {
    NettyGroupType nettyGroupType = configuration.getOptionalValue(NETTY_CLIENT_GROUP_TYPE, String.class)
      .map(NettyGroupType::valueOf)
      .orElse(NettyGroupType.NIO);
    Integer threadsCount = configuration.getOptionalValue(NETTY_CLIENT_GROUP_THREADS_COUNT, Integer.class)
      .orElse(DEFAULT_NETTY_CLIENT_GROUP_THREADS_COUNT);
    LOGGER.info("Netty event loop group was received. Type [{}], threads count [{}]", nettyGroupType, threadsCount);
    return createGroup(nettyGroupType, threadsCount);
  }

  private static EventLoopGroup createGroup(NettyGroupType nettyGroupType, Integer threadsCount) {
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

  public static NettyLogLevel resolveLogLevel(Configuration configuration) {
    NettyLogLevel nettyLogLevel = configuration.getOptionalValue(NETTY_CLIENT_LOG_LEVEL, String.class)
      .map(NettyLogLevel::valueOf)
      .orElse(NettyLogLevel.DISABLED);
    LOGGER.info("Netty log level was received [{}]", nettyLogLevel);
    return nettyLogLevel;
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

  public static Cache<String, ResponseFuture> createResponseFuturesCache(Configuration configuration) {
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

  public static NettySslProvider resolveSslProvider(Configuration configuration) {
    NettySslProvider nettySslProvider = configuration.getOptionalValue(NETTY_CLIENT_SSL_PROVIDER, String.class)
      .map(NettySslProvider::valueOf).orElse(NettySslProvider.DISABLED);
    LOGGER.info("Netty ssl provider was resolved [{}]", nettySslProvider);
    return nettySslProvider;
  }

  //string
  public static final String NETTY_CLIENT_SSL_TRUST_STORE_PATH =
    ROOT_PREFIX + ".netty.client.ssl.trust.path";

  public static File resolveTrustStore(Configuration configuration) {
    String trustManagerCertPathString = configuration.getValue(NETTY_CLIENT_SSL_TRUST_STORE_PATH, String.class);
    LOGGER.info("Trust store path was resolved [{}]", trustManagerCertPathString);
    if (trustManagerCertPathString == null) {
      //for system default
      return null;
    }
    return Paths.get(trustManagerCertPathString).toFile();
  }

  //string
  public static final String NETTY_CLIENT_SSL_KEY_MANAGER_CERT_PATH =
    ROOT_PREFIX + ".netty.client.ssl.key.store.path";

  public static File resolveKeyStore(Configuration configuration) {
    String keyManagerCertPathString = configuration.getValue(NETTY_CLIENT_SSL_KEY_MANAGER_CERT_PATH, String.class);
    LOGGER.info("Key store path was resolved [{}]", keyManagerCertPathString);
    if (keyManagerCertPathString == null) {
      //for system default
      return null;
    }
    return Paths.get(keyManagerCertPathString).toFile();
  }

  //string
  public static final String NETTY_CLIENT_SSL_KEY_PATH =
    ROOT_PREFIX + ".netty.client.ssl.key.path";

  public static File resolveKey(Configuration configuration) {
    String keyManagerCertPathString = configuration.getValue(NETTY_CLIENT_SSL_KEY_PATH, String.class);
    LOGGER.info("Key path was resolved [{}]", keyManagerCertPathString);
    if (keyManagerCertPathString == null) {
      //for system default
      return null;
    }
    return Paths.get(keyManagerCertPathString).toFile();
  }

  //string
  public static final String NETTY_CLIENT_SSL_KEY_PASSWORD =
    ROOT_PREFIX + ".netty.client.ssl.key.password";

  public static String resolveKeyPassword(Configuration configuration) {
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

  public static ClientAuth resolveClientAuth(Configuration configuration) {
    ClientAuth clientAuth = configuration.getOptionalValue(NETTY_CLIENT_SSL_AUTH_MODE, String.class)
      .map(ClientAuth::valueOf)
      .orElse(ClientAuth.NONE);
    LOGGER.info("Client auth was resolved [{}]", clientAuth);
    return clientAuth;
  }
}