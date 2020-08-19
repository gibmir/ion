package com.github.gibmir.ion.lib.netty.server.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.lib.netty.common.configuration.group.NettyGroupType;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
}
