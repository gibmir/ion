package com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ssl;

import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;

public final class SslAppenderDecorator implements ChannelHandlerAppender {
  private final ChannelHandlerAppender channelHandlerAppender;
  private final SslContext sslContext;

  private SslAppenderDecorator(final ChannelHandlerAppender channelHandlerAppender, final SslContext sslContext) {
    this.channelHandlerAppender = channelHandlerAppender;
    this.sslContext = sslContext;
  }

  /**
   * Decorates specified appender with ssl.
   *
   * @param channelHandlerAppender appender
   * @param sslContext             ssl context
   * @return specified appender decorated with ssl
   */
  public static ChannelHandlerAppender decorate(final ChannelHandlerAppender channelHandlerAppender,
                                                final SslContext sslContext) {
    return new SslAppenderDecorator(channelHandlerAppender, sslContext);
  }

  @Override
  public void appendTo(final ChannelPipeline channelPipeline) {
    channelPipeline.addLast(sslContext.newHandler(channelPipeline.channel().alloc()));
    channelHandlerAppender.appendTo(channelPipeline);
  }
}
