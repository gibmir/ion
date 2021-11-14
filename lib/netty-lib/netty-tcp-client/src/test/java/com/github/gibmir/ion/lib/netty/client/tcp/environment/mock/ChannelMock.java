package com.github.gibmir.ion.lib.netty.client.tcp.environment.mock;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class ChannelMock {

  private ChannelMock() {
  }

  public static Channel emptyMock() {
    Channel channel = mock(Channel.class);
    doAnswer(__ -> mock(ChannelFuture.class)).when(channel).writeAndFlush(any());
    return channel;
  }
}
