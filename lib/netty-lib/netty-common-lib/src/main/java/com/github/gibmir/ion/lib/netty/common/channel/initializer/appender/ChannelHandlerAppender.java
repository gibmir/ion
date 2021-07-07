package com.github.gibmir.ion.lib.netty.common.channel.initializer.appender;

import io.netty.channel.ChannelPipeline;

public interface ChannelHandlerAppender {
  /**
   * Appends all necessary handlers to specified pipeline.
   *
   * @param channelPipeline handlers pipeline
   */
  void appendTo(ChannelPipeline channelPipeline);
}
