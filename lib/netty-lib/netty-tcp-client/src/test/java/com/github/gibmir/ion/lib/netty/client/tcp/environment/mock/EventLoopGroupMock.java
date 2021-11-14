package com.github.gibmir.ion.lib.netty.client.tcp.environment.mock;

import io.netty.channel.EventLoopGroup;

import static org.mockito.Mockito.mock;

public class EventLoopGroupMock {
  private EventLoopGroupMock() {
  }

  public static EventLoopGroup emptyMock() {
    return mock(EventLoopGroup.class);
  }
}
