package com.github.gibmir.ion.lib.netty.common.channel.initializer;

import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public final class JsonRpcChannelInitializer extends ChannelInitializer<Channel> {
  private final ChannelHandlerAppender channelHandlerAppender;

  public JsonRpcChannelInitializer(final ChannelHandlerAppender channelHandlerAppender) {
    this.channelHandlerAppender = channelHandlerAppender;
  }

  @Override
  protected void initChannel(final Channel ch) {
    channelHandlerAppender.appendTo(ch.pipeline());
  }
}
