package com.github.gibmir.ion.lib.netty.client.environment.mock;

import com.github.gibmir.ion.lib.netty.client.sender.pool.ChannelPool;
import io.netty.channel.Channel;

import javax.json.bind.Jsonb;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.charset.Charset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class ChannelPoolMock {
  public static ChannelPool newMock(Channel channel) {
    ChannelPool channelPool = mock(ChannelPool.class);
    doAnswer(__ -> channel).when(channelPool).getOrCreate(any(Jsonb.class), any(Charset.class), any(SocketAddress.class));
    return channelPool;
  }

  public static ChannelPool newMock(Class<? extends RuntimeException> throwableClass) throws IOException {
    ChannelPool channelPool = mock(ChannelPool.class);
    doThrow(throwableClass).when(channelPool).getOrCreate(any(Jsonb.class), any(Charset.class), any(SocketAddress.class));
    doThrow(throwableClass).when(channelPool).close();
    return channelPool;
  }

  public static ChannelPool emptyMock() {
    return mock(ChannelPool.class);
  }
}
