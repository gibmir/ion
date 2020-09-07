package com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.logging;

import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class LoggingAppenderDecorator implements ChannelHandlerAppender {
  private final ChannelHandlerAppender channelHandlerAppender;
  private final LogLevel logLevel;

  private LoggingAppenderDecorator(ChannelHandlerAppender channelHandlerAppender, LogLevel logLevel) {
    this.channelHandlerAppender = channelHandlerAppender;
    this.logLevel = logLevel;
  }

  public static ChannelHandlerAppender decorate(ChannelHandlerAppender channelHandlerAppender, LogLevel logLevel) {
    return new LoggingAppenderDecorator(channelHandlerAppender, logLevel);
  }

  @Override
  public void appendTo(ChannelPipeline channelPipeline) {
    channelPipeline.addLast(new LoggingHandler(logLevel));
    channelHandlerAppender.appendTo(channelPipeline);
  }
}
