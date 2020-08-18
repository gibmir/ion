package com.github.gibmir.ion.lib.netty.common.configuration.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;

public class NettyChannelOption<T> {
  private final ChannelOption<T> channelOption;
  private final T optionValue;

  public NettyChannelOption(ChannelOption<T> channelOption, T optionValue) {
    this.channelOption = channelOption;
    this.optionValue = optionValue;
  }

  public void appendTo(Bootstrap bootstrap) {
    bootstrap.option(channelOption, optionValue);
  }

  public static final String SO_BACKLOG_POSTFIX = "so.backlog";

  public static NettyChannelOption<Integer> soBacklog(Integer value) {
    return new NettyChannelOption<>(ChannelOption.SO_BACKLOG, value);
  }

  public static final String SO_LINGER_POSTFIX = "so.linger";

  public static NettyChannelOption<Integer> soLinger(Integer value) {
    return new NettyChannelOption<>(ChannelOption.SO_LINGER, value);
  }

  public static final String SO_RCVBUF_POSTFIX = "so.rcvbuf";

  public static NettyChannelOption<Integer> soRcvbuf(Integer value) {
    return new NettyChannelOption<>(ChannelOption.SO_RCVBUF, value);
  }

  public static final String SO_SNDBUF_POSTFIX = "so.sndbuf";

  public static NettyChannelOption<Integer> soSndbuf(Integer value) {
    return new NettyChannelOption<>(ChannelOption.SO_SNDBUF, value);
  }

  public static final String SO_TIMEOUT_POSTFIX = "so.timeout";

  public static NettyChannelOption<Integer> soTimeout(Integer value) {
    return new NettyChannelOption<>(ChannelOption.SO_TIMEOUT, value);
  }

  public static final String SO_BROADCAST_POSTFIX = "so.broadcast";

  public static NettyChannelOption<Boolean> soBroadcast(Boolean value) {
    return new NettyChannelOption<>(ChannelOption.SO_BROADCAST, value);
  }

  public static final String SO_KEEPALIVE_POSTFIX = "so.keepalive";

  public static NettyChannelOption<Boolean> soKeepalive(Boolean value) {
    return new NettyChannelOption<>(ChannelOption.SO_KEEPALIVE, value);
  }

  public static final String SO_REUSEADDR_POSTFIX = "so.reuseaddr";

  public static NettyChannelOption<Boolean> soReuseaddr(Boolean value) {
    return new NettyChannelOption<>(ChannelOption.SO_REUSEADDR, value);
  }

}
