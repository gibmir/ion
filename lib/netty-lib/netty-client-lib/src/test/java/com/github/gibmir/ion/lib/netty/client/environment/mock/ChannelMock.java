package com.github.gibmir.ion.lib.netty.client.environment.mock;

import io.netty.channel.Channel;

import static org.mockito.Mockito.mock;

public class ChannelMock {

  public static Channel newMock() {
    return mock(Channel.class);
  }
}
