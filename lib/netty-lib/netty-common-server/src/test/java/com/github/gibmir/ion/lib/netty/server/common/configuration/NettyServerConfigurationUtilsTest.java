package com.github.gibmir.ion.lib.netty.server.common.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.common.configuration.decoder.FrameDecoderConfig;
import com.github.gibmir.ion.lib.netty.common.configuration.group.NettyGroupType;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import com.github.gibmir.ion.lib.netty.common.configuration.ssl.NettySslProvider;
import com.github.gibmir.ion.lib.netty.common.exceptions.NettyInitializationException;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.handler.logging.LoggingHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
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
  void testCreateNioEventLoopGroup() {
    doAnswer(__ -> Optional.of(NettyGroupType.NIO.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_GROUP_TYPE, String.class);
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
  void testAppendLoggingDisabled() {
    ServerBootstrap serverBootstrap = mock(ServerBootstrap.class);
    NettyServerConfigurationUtils.appendLoggingTo(serverBootstrap, NettyLogLevel.DISABLED);

    verify(serverBootstrap, never()).handler(any());
  }

  @Test
  void testAppendLoggingError() {
    ServerBootstrap serverBootstrap = mock(ServerBootstrap.class);
    NettyServerConfigurationUtils.appendLoggingTo(serverBootstrap, NettyLogLevel.ERROR);

    ArgumentCaptor<LoggingHandler> captor = ArgumentCaptor.forClass(LoggingHandler.class);
    verify(serverBootstrap).handler(captor.capture());
    LoggingHandler handler = captor.getValue();
    assertThat(handler.level(), is(NettyLogLevel.ERROR.get()));
  }

  @Test
  void testAppendLoggingWarn() {
    ServerBootstrap serverBootstrap = mock(ServerBootstrap.class);
    NettyServerConfigurationUtils.appendLoggingTo(serverBootstrap, NettyLogLevel.WARN);

    ArgumentCaptor<LoggingHandler> captor = ArgumentCaptor.forClass(LoggingHandler.class);
    verify(serverBootstrap).handler(captor.capture());
    LoggingHandler handler = captor.getValue();
    assertThat(handler.level(), is(NettyLogLevel.WARN.get()));
  }

  @Test
  void testAppendLoggingTrace() {
    ServerBootstrap serverBootstrap = mock(ServerBootstrap.class);
    NettyServerConfigurationUtils.appendLoggingTo(serverBootstrap, NettyLogLevel.TRACE);

    ArgumentCaptor<LoggingHandler> captor = ArgumentCaptor.forClass(LoggingHandler.class);
    verify(serverBootstrap).handler(captor.capture());
    LoggingHandler handler = captor.getValue();
    assertThat(handler.level(), is(NettyLogLevel.TRACE.get()));
  }

  @Test
  void testAppendLoggingDebug() {
    ServerBootstrap serverBootstrap = mock(ServerBootstrap.class);
    NettyServerConfigurationUtils.appendLoggingTo(serverBootstrap, NettyLogLevel.DEBUG);

    ArgumentCaptor<LoggingHandler> captor = ArgumentCaptor.forClass(LoggingHandler.class);
    verify(serverBootstrap).handler(captor.capture());
    LoggingHandler handler = captor.getValue();
    assertThat(handler.level(), is(NettyLogLevel.DEBUG.get()));
  }

  @Test
  void testAppendLoggingInfo() {
    ServerBootstrap serverBootstrap = mock(ServerBootstrap.class);
    NettyServerConfigurationUtils.appendLoggingTo(serverBootstrap, NettyLogLevel.INFO);

    ArgumentCaptor<LoggingHandler> captor = ArgumentCaptor.forClass(LoggingHandler.class);
    verify(serverBootstrap).handler(captor.capture());
    LoggingHandler handler = captor.getValue();
    assertThat(handler.level(), is(NettyLogLevel.INFO.get()));
  }

  @Test
  void testResolveDefaultSslProvider() {
    assertThat(NettyServerConfigurationUtils.resolveSslProvider(configuration), equalTo(NettySslProvider.DISABLED));
  }

  @Test
  void testResolveDisabledSslProvider() {
    doAnswer(__ -> Optional.of(NettySslProvider.DISABLED.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_SSL_PROVIDER, String.class);

    assertThat(NettyServerConfigurationUtils.resolveSslProvider(configuration), equalTo(NettySslProvider.DISABLED));
  }

  @Test
  void testResolveJdkSslProvider() {
    doAnswer(__ -> Optional.of(NettySslProvider.JDK.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_SSL_PROVIDER, String.class);

    assertThat(NettyServerConfigurationUtils.resolveSslProvider(configuration), equalTo(NettySslProvider.JDK));
  }

  @Test
  void testResolveOpenSslProvider() {
    doAnswer(__ -> Optional.of(NettySslProvider.OPENSSL.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_SSL_PROVIDER, String.class);

    assertThat(NettyServerConfigurationUtils.resolveSslProvider(configuration), equalTo(NettySslProvider.OPENSSL));
  }

  @Test
  void testResolveOpenSslREFCNTProvider() {
    doAnswer(__ -> Optional.of(NettySslProvider.OPENSSL_REFCNT.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_SSL_PROVIDER, String.class);

    assertThat(NettyServerConfigurationUtils.resolveSslProvider(configuration), equalTo(NettySslProvider.OPENSSL_REFCNT));
  }

  @Test
  void testResolveIncorrectSslProvider() {
    String incorrectSslProvider = "test-incorrect-provider";
    doAnswer(__ -> Optional.of(incorrectSslProvider)).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_SSL_PROVIDER, String.class);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
      () -> NettyServerConfigurationUtils.resolveSslProvider(configuration));
    assertThat(exception.getMessage(), containsString(incorrectSslProvider));
  }

  @Test
  void testDecorateWithDisabledSsl() {
    ChannelHandlerAppender appender = mock(ChannelHandlerAppender.class);
    ChannelHandlerAppender decorated = NettyServerConfigurationUtils.decorateWithSsl(appender, configuration);
    assertThat(decorated, equalTo(appender));
  }

  @Test
  void testDecorateWithJdkSslAndException() {
    doAnswer(__ -> Optional.of(NettySslProvider.JDK.name())).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_SSL_PROVIDER, String.class);
    ChannelHandlerAppender appender = mock(ChannelHandlerAppender.class);
    assertThrows(NettyInitializationException.class,
      () -> NettyServerConfigurationUtils.decorateWithSsl(appender, configuration));
  }

  @Test
  void testResolveNullTrustStore() {
    assertThat(NettyServerConfigurationUtils.resolveTrustStore(configuration), is(nullValue()));
  }

  @Test
  void testResolveTrustStore() {
    String testResolveTrustStore = "testResolveTrustStore";
    doAnswer(__ -> testResolveTrustStore).when(configuration)
      .getValue(NettyServerConfigurationUtils.NETTY_SERVER_SSL_TRUST_MANAGER_CERT_PATH, String.class);
    File trustStore = NettyServerConfigurationUtils.resolveTrustStore(configuration);
    assertThat(trustStore, not(nullValue()));
    assertThat(trustStore.getName(), is(testResolveTrustStore));
  }

  @Test
  void testResolveNullCertificate() {
    assertThat(NettyServerConfigurationUtils.resolveCertificate(configuration), is(nullValue()));
  }

  @Test
  void testResolveCertificate() {
    String certificate = "test-Certificate";
    doAnswer(__ -> certificate).when(configuration)
      .getValue(NettyServerConfigurationUtils.NETTY_SERVER_SSL_KEY_MANAGER_CERT_PATH, String.class);

    File resolved = NettyServerConfigurationUtils.resolveCertificate(configuration);
    assertThat(resolved, not(nullValue()));
    assertThat(resolved.getName(), is(certificate));
  }

  @Test
  void testResolveNullPrivateKey() {
    assertThat(NettyServerConfigurationUtils.resolvePrivateKey(configuration), is(nullValue()));
  }

  @Test
  void testResolvePrivateKey() {
    String testPrivateKey = "testPrivateKey";
    doAnswer(__ -> testPrivateKey).when(configuration)
      .getValue(NettyServerConfigurationUtils.NETTY_SERVER_SSL_KEY_PATH, String.class);
    File privateKey = NettyServerConfigurationUtils.resolvePrivateKey(configuration);
    assertThat(privateKey, not(nullValue()));
    assertThat(privateKey.getName(), equalTo(testPrivateKey));
  }

  @Test
  void testResolveNullKeyPassword() {
    assertThat(NettyServerConfigurationUtils.resolveKeyPassword(configuration), is(nullValue()));
  }

  @Test
  void testResolveKeyPassword() {
    String testKeyPassword = "testKeyPassword";
    doAnswer(__ -> testKeyPassword).when(configuration)
      .getValue(NettyServerConfigurationUtils.NETTY_SERVER_SSL_KEY_PASSWORD, String.class);
    assertThat(NettyServerConfigurationUtils.resolveKeyPassword(configuration), is(testKeyPassword));
  }

  @Test
  void testResolveFrameDecoderConfigDefault() {
    FrameDecoderConfig frameDecoderConfig = NettyServerConfigurationUtils.resolveFrameDecoderConfig(configuration);
    assertThat(frameDecoderConfig, not(nullValue()));
    assertThat(frameDecoderConfig.getLengthAdjustment(),
      equalTo(NettyServerConfigurationUtils.DEFAULT_NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_ADJUSTMENT));
    assertThat(frameDecoderConfig.getMaxFrameLength(),
      equalTo(NettyServerConfigurationUtils.DEFAULT_NETTY_SERVER_FRAME_DECODER_MAX_FRAME_LENGTH));
    assertThat(frameDecoderConfig.getLengthFieldLength(),
      equalTo(NettyServerConfigurationUtils.DEFAULT_NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_LENGTH));
    assertThat(frameDecoderConfig.getLengthFieldOffset(),
      equalTo(NettyServerConfigurationUtils.DEFAULT_NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_OFFSET));
    assertThat(frameDecoderConfig.getInitialBytesToStrip(),
      equalTo(NettyServerConfigurationUtils.DEFAULT_NETTY_SERVER_FRAME_DECODER_LENGTH_STRIP_BITES));
  }

  @Test
  void testResolveFrameDecoderConfig() {
    int testLengthFieldAdjustment = 60100;
    doAnswer(__ -> Optional.of(testLengthFieldAdjustment)).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_ADJUSTMENT, Integer.class);
    int testMaxFrameLength = 1010;
    doAnswer(__ -> Optional.of(testMaxFrameLength)).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_FRAME_DECODER_MAX_FRAME_LENGTH, Integer.class);
    int testLengthFieldLength = 1020;
    doAnswer(__ -> Optional.of(testLengthFieldLength)).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_LENGTH, Integer.class);
    int testLengthFieldOffset = 1030;
    doAnswer(__ -> Optional.of(testLengthFieldOffset)).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_FRAME_DECODER_LENGTH_FIELD_OFFSET, Integer.class);
    int testLengthStripBites = 1040;
    doAnswer(__ -> Optional.of(testLengthStripBites)).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_FRAME_DECODER_LENGTH_STRIP_BITES, Integer.class);
    FrameDecoderConfig frameDecoderConfig = NettyServerConfigurationUtils.resolveFrameDecoderConfig(configuration);
    assertThat(frameDecoderConfig, not(nullValue()));
    assertThat(frameDecoderConfig.getLengthAdjustment(), equalTo(testLengthFieldAdjustment));
    assertThat(frameDecoderConfig.getMaxFrameLength(), equalTo(testMaxFrameLength));
    assertThat(frameDecoderConfig.getLengthFieldLength(), equalTo(testLengthFieldLength));
    assertThat(frameDecoderConfig.getLengthFieldOffset(), equalTo(testLengthFieldOffset));
    assertThat(frameDecoderConfig.getInitialBytesToStrip(), equalTo(testLengthStripBites));
  }

  @Test
  void testResolveEncoderFrameLengthDefault() {
    assertThat(NettyServerConfigurationUtils.resolveEncoderFrameLength(configuration),
      is(NettyServerConfigurationUtils.DEFAULT_NETTY_SERVER_FRAME_ENCODER_LENGTH_FIELD_LENGTH));
  }

  @Test
  void testResolveEncoderFrameLength() {
    int testLengthFieldAdjustment = 60100;
    doAnswer(__ -> Optional.of(testLengthFieldAdjustment)).when(configuration)
      .getOptionalValue(NettyServerConfigurationUtils.NETTY_SERVER_FRAME_ENCODER_LENGTH_FIELD_LENGTH, Integer.class);
    assertThat(NettyServerConfigurationUtils.resolveEncoderFrameLength(configuration), is(testLengthFieldAdjustment));
  }
}
