package com.github.gibmir.ion.lib.netty.common.channel.initializer;

import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JsonRpcChannelInitializerTest {

  @Test
  void testInitChannel() {
    ChannelHandlerAppender channelHandlerAppender = mock(ChannelHandlerAppender.class);
    JsonRpcChannelInitializer jsonRpcChannelInitializer = new JsonRpcChannelInitializer(channelHandlerAppender);
    Channel channel = mock(Channel.class);
    ChannelPipeline channelPipeline = mock(ChannelPipeline.class);
    doAnswer(__ -> channelPipeline).when(channel).pipeline();
    jsonRpcChannelInitializer.initChannel(channel);
    verify(channelHandlerAppender).appendTo(eq(channelPipeline));
  }
}
