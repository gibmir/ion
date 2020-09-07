package com.github.gibmir.ion.lib.netty.common.channel.initializer.appender;

import io.netty.channel.ChannelPipeline;

public interface ChannelHandlerAppender {
  void appendTo(ChannelPipeline channelPipeline);
}
