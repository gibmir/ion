package com.github.gibmir.ion.lib.netty.client.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.lib.netty.common.configuration.group.NettyGroupType;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.handler.logging.LogLevel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public class NettyRequestConfigurationUtils {
  //int properties
  public static final String NETTY_CLIENT_SOCKET_ADDRESS_PORT = ROOT_PREFIX + ".netty.client.socket.address.port";
  public static final String NETTY_CLIENT_GROUP_THREADS_COUNT = ROOT_PREFIX + ".netty.client.group.threads.count";
  //string properties
  public static final String NETTY_CLIENT_SOCKET_ADDRESS_HOST = ROOT_PREFIX + ".netty.client.socket.address.host";
  public static final String NETTY_CLIENT_CHANNEL_TYPE = ROOT_PREFIX + ".netty.client.channel.type";
  public static final String NETTY_CLIENT_GROUP_TYPE = ROOT_PREFIX + ".netty.client.group.type";
  public static final String NETTY_CLIENT_LOG_LEVEL = ROOT_PREFIX + ".netty.client.log.level";

  public static final Integer DEFAULT_NETTY_CLIENT_GROUP_THREADS_COUNT = Runtime.getRuntime().availableProcessors();

  private NettyRequestConfigurationUtils() {
  }

  public static SocketAddress createSocketAddressWith(Configuration configuration) {
    String socketAddressHost = configuration.getValue(NETTY_CLIENT_SOCKET_ADDRESS_HOST, String.class);

    Integer socketAddressPort = configuration.getValue(NETTY_CLIENT_SOCKET_ADDRESS_PORT, Integer.class);

    return new InetSocketAddress(socketAddressHost, socketAddressPort);
  }

  public static Class<? extends Channel> resolveChannelClass(Configuration configuration) {
    return configuration.getOptionalValue(NETTY_CLIENT_CHANNEL_TYPE, String.class)
      .map(NettyClientChannelType::valueOf)
      .orElse(NettyClientChannelType.NIO)
      .resolveChannelClass();
  }

  public static EventLoopGroup createEventLoopGroup(Configuration configuration) {
    NettyGroupType nettyGroupType = configuration.getOptionalValue(NETTY_CLIENT_GROUP_TYPE, String.class)
      .map(NettyGroupType::valueOf)
      .orElse(NettyGroupType.NIO);
    Integer threadsCount = configuration.getOptionalValue(NETTY_CLIENT_GROUP_THREADS_COUNT, Integer.class)
      .orElse(DEFAULT_NETTY_CLIENT_GROUP_THREADS_COUNT);
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

  public static LogLevel resolveLogLevel(Configuration configuration) {
    return configuration.getOptionalValue(NETTY_CLIENT_LOG_LEVEL, String.class)
      .map(LogLevel::valueOf)
      .orElse(LogLevel.INFO);
  }
}
