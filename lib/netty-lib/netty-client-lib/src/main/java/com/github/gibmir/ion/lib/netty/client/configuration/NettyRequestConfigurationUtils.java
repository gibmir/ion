package com.github.gibmir.ion.lib.netty.client.configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.common.configuration.group.NettyGroupType;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.time.Duration;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public class NettyRequestConfigurationUtils {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyRequestConfigurationUtils.class);

  private NettyRequestConfigurationUtils() {
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
  public static final String NETTY_CLIENT_RESPONSE_LISTENER_CACHE_EVICTION_TIMEOUT =
    ROOT_PREFIX + ".netty.client.response.listener.cache.eviction.timeout";
  public static final String NETTY_CLIENT_RESPONSE_LISTENER_CACHE_EVICTION_SIZE =
    ROOT_PREFIX + ".netty.client.response.listener.cache.eviction.size";
  //boolean properties
  public static final String NETTY_CLIENT_RESPONSE_LISTENER_CACHE_WEAK_REFERENCE_ENABLED =
    ROOT_PREFIX + ".netty.client.response.listener.cache.weak.reference.enabled";
  public static final String NETTY_CLIENT_RESPONSE_LISTENER_CACHE_SOFT_VALUES_ENABLED =
    ROOT_PREFIX + ".netty.client.response.listener.cache.soft.values.enabled";
  public static final String NETTY_CLIENT_RESPONSE_LISTENER_CACHE_RECORD_STATS_ENABLED =
    ROOT_PREFIX + ".netty.client.response.listener.cache.record.stats.enabled";
  //defaults
  public static final int DEFAULT_EVICTION_TIMEOUT = 60_000;

  public static Cache<String, ResponseFuture> createResponseFuturesCache(Configuration configuration) {
    Caffeine<Object, Object> caffeine = Caffeine.newBuilder();
    Integer evictionTimeout = configuration.getOptionalValue(NETTY_CLIENT_RESPONSE_LISTENER_CACHE_EVICTION_TIMEOUT, Integer.class)
      .orElse(DEFAULT_EVICTION_TIMEOUT);
    caffeine.expireAfterWrite(Duration.ofMillis(evictionTimeout));
    configuration.getOptionalValue(NETTY_CLIENT_RESPONSE_LISTENER_CACHE_EVICTION_SIZE, Integer.class)
      .ifPresent(caffeine::maximumSize);
    if (configuration.getOptionalValue(NETTY_CLIENT_RESPONSE_LISTENER_CACHE_WEAK_REFERENCE_ENABLED, Boolean.class)
      .orElse(false)) {
      caffeine.weakKeys();
      caffeine.weakValues();
    }
    if (configuration.getOptionalValue(NETTY_CLIENT_RESPONSE_LISTENER_CACHE_SOFT_VALUES_ENABLED, Boolean.class)
      .orElse(false)) {
      caffeine.softValues();
    }
    if (configuration.getOptionalValue(NETTY_CLIENT_RESPONSE_LISTENER_CACHE_RECORD_STATS_ENABLED, Boolean.class)
      .orElse(false)) {
      caffeine.softValues();
    }
    LOGGER.info("Caffeine response futures cache was received [{}]", caffeine);
    return caffeine.build();
  }
}
