package com.github.gibmir.ion.lib.netty.server.common.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NettyServerConfigurationUtilsTest {

  private Configuration configuration;

  @BeforeEach
  void beforeEach() {
    configuration = mock(Configuration.class);
  }

  @Test
  void testGetServerPortNull() {
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
      () -> NettyServerConfigurationUtils.getServerPortFrom(configuration));
    assertThat(exception.getMessage(), containsString(NettyServerConfigurationUtils.NETTY_SERVER_SOCKET_ADDRESS_PORT));
  }

  @Test
  void testGetServerPort() {
    int expectedPort = 1;
    doAnswer(__ -> expectedPort).when(configuration)
      .getValue(NettyServerConfigurationUtils.NETTY_SERVER_SOCKET_ADDRESS_PORT, Integer.class);
    Integer actualPort = NettyServerConfigurationUtils.getServerPortFrom(configuration);
    assertThat(actualPort, equalTo(expectedPort));
  }

  @Test
  void testResolveDefaultChannelClass() {
    Class<? extends ServerChannel> channelClass = NettyServerConfigurationUtils.resolveChannelClass(configuration);
    assertThat(channelClass, equalTo(NettyServerChannelType.NIO.resolveChannelClass()));
  }

  @Test
  void testResolveNioChannelClass() {
    doAnswer(__ -> Optional.of(NettyServerChannelType.NIO.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_CHANNEL_TYPE, String.class);
    Class<? extends ServerChannel> channelClass = NettyServerConfigurationUtils.resolveChannelClass(configuration);
    assertThat(channelClass, equalTo(NettyServerChannelType.NIO.resolveChannelClass()));
  }

  @Test
  void testResolveEpollChannelClass() {
    doAnswer(__ -> Optional.of(NettyServerChannelType.EPOLL.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_CHANNEL_TYPE, String.class);
    Class<? extends ServerChannel> channelClass = NettyServerConfigurationUtils.resolveChannelClass(configuration);
    assertThat(channelClass, equalTo(NettyServerChannelType.EPOLL.resolveChannelClass()));
  }

  @Test
  void testResolveIncorrectChannelClass() {
    String incorrectChannelClassName = "incorrect";
    doAnswer(__ -> Optional.of(incorrectChannelClassName)).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_CHANNEL_TYPE, String.class);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
      () -> NettyServerConfigurationUtils.resolveChannelClass(configuration));
    assertThat(exception.getMessage(), containsString(incorrectChannelClassName));
  }

  @Test
  void testCreateDefaultEventLoopGroup() {
    EventLoopGroup eventLoopGroup = NettyServerConfigurationUtils.createEventLoopGroup(configuration);
    assertThat(eventLoopGroup, not(nullValue()));
    verify(configuration)
      .getOptionalValue(eq(NettyServerConfigurationUtils.NETTY_SERVER_GROUP_TYPE), eq(String.class));
    verify(configuration)
      .getOptionalValue(eq(NettyServerConfigurationUtils.NETTY_SERVER_GROUP_THREADS_COUNT), eq(Integer.class));
    eventLoopGroup.shutdownGracefully();
  }

  @Test
  void testResolveDefaultLogLevel() {
    NettyLogLevel nettyLogLevel = NettyServerConfigurationUtils.resolveLogLevel(configuration);
    assertThat(nettyLogLevel, equalTo(NettyLogLevel.DISABLED));
  }

  @Test
  void testResolveDisabledLogLevel() {
    doAnswer(__ -> Optional.of(NettyLogLevel.DISABLED.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_LOG_LEVEL, String.class);
    NettyLogLevel nettyLogLevel = NettyServerConfigurationUtils.resolveLogLevel(configuration);
    assertThat(nettyLogLevel, equalTo(NettyLogLevel.DISABLED));
  }

  @Test
  void testResolveTraceLogLevel() {
    doAnswer(__ -> Optional.of(NettyLogLevel.TRACE.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_LOG_LEVEL, String.class);
    NettyLogLevel nettyLogLevel = NettyServerConfigurationUtils.resolveLogLevel(configuration);
    assertThat(nettyLogLevel, equalTo(NettyLogLevel.TRACE));
  }

  @Test
  void testResolveDebugLogLevel() {
    doAnswer(__ -> Optional.of(NettyLogLevel.DEBUG.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_LOG_LEVEL, String.class);
    NettyLogLevel nettyLogLevel = NettyServerConfigurationUtils.resolveLogLevel(configuration);
    assertThat(nettyLogLevel, equalTo(NettyLogLevel.DEBUG));
  }

  @Test
  void testResolveInfoLogLevel() {
    doAnswer(__ -> Optional.of(NettyLogLevel.INFO.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_LOG_LEVEL, String.class);
    NettyLogLevel nettyLogLevel = NettyServerConfigurationUtils.resolveLogLevel(configuration);
    assertThat(nettyLogLevel, equalTo(NettyLogLevel.INFO));
  }

  @Test
  void testResolveWarnLogLevel() {
    doAnswer(__ -> Optional.of(NettyLogLevel.WARN.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_LOG_LEVEL, String.class);
    NettyLogLevel nettyLogLevel = NettyServerConfigurationUtils.resolveLogLevel(configuration);
    assertThat(nettyLogLevel, equalTo(NettyLogLevel.WARN));
  }

  @Test
  void testResolveErrorLogLevel() {
    doAnswer(__ -> Optional.of(NettyLogLevel.ERROR.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_LOG_LEVEL, String.class);
    NettyLogLevel nettyLogLevel = NettyServerConfigurationUtils.resolveLogLevel(configuration);
    assertThat(nettyLogLevel, equalTo(NettyLogLevel.ERROR));
  }

  @Test
  void testResolveIncorrectLogLevel() {
    String incorrectLogLevel = "incorrect";
    doAnswer(__ -> Optional.of(incorrectLogLevel)).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_LOG_LEVEL, String.class);
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
      () -> NettyServerConfigurationUtils.resolveLogLevel(configuration));
    assertThat(exception.getMessage(), containsString(incorrectLogLevel));
  }

  @Test
  void testAppendLoggingTo() {
  }

  @Test
  void testDecorateWithSsl() {
  }

  @Test
  void testResolveSslProvider() {
  }

  @Test
  void testResolveTrustStore() {
  }

  @Test
  void testResolveCertificate() {
  }

  @Test
  void testResolvePrivateKey() {
  }

  @Test
  void testResolveKeyPassword() {
  }

  @Test
  void testResolveFrameDecoderConfig() {
  }

  @Test
  void testResolveEncoderFrameLength() {
  }
}
