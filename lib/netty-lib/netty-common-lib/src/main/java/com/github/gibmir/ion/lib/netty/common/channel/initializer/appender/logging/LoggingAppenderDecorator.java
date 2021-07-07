package com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.logging;

import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public final class LoggingAppenderDecorator implements ChannelHandlerAppender {
  private final ChannelHandlerAppender channelHandlerAppender;
  private final LogLevel logLevel;

  private LoggingAppenderDecorator(final ChannelHandlerAppender channelHandlerAppender, final LogLevel logLevel) {
    this.channelHandlerAppender = channelHandlerAppender;
    this.logLevel = logLevel;
  }

  /**
   * Decorates specified appender with logging.
   *
   * @param channelHandlerAppender appender
   * @param logLevel               log level
   * @return specified appender decorated with logging
   */
  public static ChannelHandlerAppender decorate(final ChannelHandlerAppender channelHandlerAppender, final LogLevel logLevel) {
    return new LoggingAppenderDecorator(channelHandlerAppender, logLevel);
  }

  @Override
  public void appendTo(final ChannelPipeline channelPipeline) {
    channelPipeline.addLast(new LoggingHandler(logLevel));
    channelHandlerAppender.appendTo(channelPipeline);
  }
}
