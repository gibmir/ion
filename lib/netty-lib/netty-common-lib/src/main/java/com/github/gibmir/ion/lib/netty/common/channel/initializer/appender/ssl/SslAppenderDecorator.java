package com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ssl;

import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.ssl.SslContext;

public class SslAppenderDecorator implements ChannelHandlerAppender {
  private final ChannelHandlerAppender channelHandlerAppender;
  private final SslContext sslContext;

  private SslAppenderDecorator(ChannelHandlerAppender channelHandlerAppender, SslContext sslContext) {
    this.channelHandlerAppender = channelHandlerAppender;
    this.sslContext = sslContext;
  }

  public static ChannelHandlerAppender decorate(ChannelHandlerAppender channelHandlerAppender, SslContext sslContext) {
    return new SslAppenderDecorator(channelHandlerAppender, sslContext);
  }

  @Override
  public void appendTo(ChannelPipeline channelPipeline) {
    channelPipeline.addLast(sslContext.newHandler(channelPipeline.channel().alloc()));
    channelHandlerAppender.appendTo(channelPipeline);
  }
}
