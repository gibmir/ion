package com.github.gibmir.ion.lib.netty.client.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public class NettyRequestConfigurationUtils {
  //int properties
  public static final String NETTY_CLIENT_SOCKET_ADDRESS_PORT = ROOT_PREFIX + ".netty.client.socket.address.port";
  public static final String NETTY_CLIENT_GROUP_THREADS_COUNT = ROOT_PREFIX + ".netty.client.group.threads.count";
  //string properties
  public static final String NETTY_CLIENT_SOCKET_ADDRESS_HOST = ROOT_PREFIX + ".netty.client.socket.address.host";
  public static final String NETTY_CLIENT_CHANNEL_TYPE = ROOT_PREFIX + ".netty.client.channel";
  public static final String NETTY_CLIENT_GROUP_TYPE = ROOT_PREFIX + ".netty.client.group";

  public static final Integer DEFAULT_NETTY_CLIENT_GROUP_THREADS_COUNT = Runtime.getRuntime().availableProcessors();

  private NettyRequestConfigurationUtils() {
  }

  public static SocketAddress createSocketAddressWith(Configuration configuration) {
    String socketAddressHost = configuration.getValue(NETTY_CLIENT_SOCKET_ADDRESS_HOST, String.class);

    Integer socketAddressPort = configuration.getValue(NETTY_CLIENT_SOCKET_ADDRESS_PORT, Integer.class);

    return new InetSocketAddress(socketAddressHost, socketAddressPort);
  }

  public static Class<? extends Channel> resolveChannelWith(Configuration configuration) {
    return configuration.getOptionalValue(NETTY_CLIENT_CHANNEL_TYPE, String.class)
      .map(NettyChannelType::valueOf)
      .orElse(NettyChannelType.NIO)
      .resolveChannelClass();
  }

  public static EventLoopGroup resolveEventLoopGroup(Configuration configuration) {
    NettyGroupType nettyGroupType = configuration.getOptionalValue(NETTY_CLIENT_GROUP_TYPE, String.class)
      .map(NettyGroupType::valueOf)
      .orElse(NettyGroupType.NIO);
    Integer threadsCount = configuration.getOptionalValue(NETTY_CLIENT_GROUP_THREADS_COUNT, Integer.class)
      .orElse(DEFAULT_NETTY_CLIENT_GROUP_THREADS_COUNT);
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
}
