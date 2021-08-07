package com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ssl;

import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.logging.LoggingAppenderDecorator;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class SslAppenderDecoratorTest {

  @Test
  void smoke() {
    ChannelHandlerAppender channelHandlerAppender = mock(ChannelHandlerAppender.class);
    ChannelPipeline channelPipeline = mock(ChannelPipeline.class);
    Channel channel = mock(Channel.class);
    doAnswer(__ -> channel).when(channelPipeline).channel();
    SslContext sslContext = mock(SslContext.class);
    ChannelHandlerAppender ssl = SslAppenderDecorator.decorate(channelHandlerAppender, sslContext);
    ssl.appendTo(channelPipeline);

    verify(channelHandlerAppender, atLeastOnce()).appendTo(channelPipeline);
  }
}
