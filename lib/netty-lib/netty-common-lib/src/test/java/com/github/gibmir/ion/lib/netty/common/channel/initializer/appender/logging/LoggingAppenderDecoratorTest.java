package com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.logging;

import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class LoggingAppenderDecoratorTest {

  @Test
  void smoke() {
    ChannelHandlerAppender channelHandlerAppender = mock(ChannelHandlerAppender.class);
    ChannelPipeline channelPipeline = mock(ChannelPipeline.class);

    ChannelHandlerAppender logging = LoggingAppenderDecorator.decorate(channelHandlerAppender, LogLevel.DEBUG);
    logging.appendTo(channelPipeline);

    verify(channelHandlerAppender, atLeastOnce()).appendTo(channelPipeline);
    ArgumentCaptor<ChannelHandler> captor = ArgumentCaptor.forClass(ChannelHandler.class);
    verify(channelPipeline, atLeastOnce()).addLast(captor.capture());
    ChannelHandler capturedHandler = captor.getValue();
    assertThat(capturedHandler, instanceOf(LoggingHandler.class));
  }
}
