package com.github.gibmir.ion.lib.netty.client.common.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.logging.LoggingAppenderDecorator;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ssl.SslAppenderDecorator;
import com.github.gibmir.ion.lib.netty.common.configuration.decoder.FrameDecoderConfig;
import com.github.gibmir.ion.lib.netty.common.configuration.logging.NettyLogLevel;
import com.github.gibmir.ion.lib.netty.common.configuration.ssl.NettySslProvider;
import io.netty.channel.Channel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.ClientAuth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.SocketAddress;
import java.net.URI;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NettyClientConfigurationUtilsTest {

  private Configuration configuration;

  @BeforeEach
  void beforeEach() {
    configuration = mock(Configuration.class);
  }

  //resolve channel class
  @Test
  void testResolveChannelClassDefault() {
    Class<? extends Channel> channelClass = NettyClientConfigurationUtils.resolveChannelClass(configuration);

    assertThat(channelClass, is(NioSocketChannel.class));
    verify(configuration)
      .getOptionalValue(eq(NettyClientConfigurationUtils.NETTY_CLIENT_CHANNEL_TYPE), eq(String.class));
  }

  @Test
  void testResolveChannelClassNio() {
    doAnswer(__ -> Optional.of("NIO")).when(configuration).getOptionalValue(any(), any());
    Class<? extends Channel> channelClass = NettyClientConfigurationUtils.resolveChannelClass(configuration);

    assertThat(channelClass, is(NioSocketChannel.class));
    verify(configuration)
      .getOptionalValue(eq(NettyClientConfigurationUtils.NETTY_CLIENT_CHANNEL_TYPE), eq(String.class));
  }

  @Test
  void testResolveChannelClassEpoll() {
    doAnswer(__ -> Optional.of("EPOLL")).when(configuration).getOptionalValue(any(), any());
    Class<? extends Channel> channelClass = NettyClientConfigurationUtils.resolveChannelClass(configuration);

    assertThat(channelClass, is(EpollSocketChannel.class));
    verify(configuration)
      .getOptionalValue(eq(NettyClientConfigurationUtils.NETTY_CLIENT_CHANNEL_TYPE), eq(String.class));
  }

  @Test
  void testResolveChannelClassException() {
    doAnswer(__ -> Optional.of("incorrect")).when(configuration).getOptionalValue(any(), any());
    assertThrows(IllegalArgumentException.class, () -> NettyClientConfigurationUtils.resolveChannelClass(configuration));
  }

  //create socket address
  @Test
  void testCreateSocketAddress() {
    String testHost = "test-host";
    doAnswer(__ -> testHost).when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_SOCKET_ADDRESS_HOST, String.class);
    int testPort = 60100;
    doAnswer(__ -> testPort).when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_SOCKET_ADDRESS_PORT, Integer.class);
    SocketAddress address = NettyClientConfigurationUtils.createSocketAddressWith(configuration);
    assertThat(address.toString(), containsString(testHost));
    assertThat(address.toString(), containsString(String.valueOf(testPort)));
  }

  @Test
  void testCreateSocketAddressWithoutHost() {
    int testPort = 60100;
    doAnswer(__ -> testPort).when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_SOCKET_ADDRESS_PORT, Integer.class);
    assertThrows(IllegalArgumentException.class,
      () -> NettyClientConfigurationUtils.createSocketAddressWith(configuration));
  }

  @Test
  void testCreateSocketAddressWithoutPort() {
    String testHost = "test-host";
    doAnswer(__ -> testHost).when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_SOCKET_ADDRESS_HOST, String.class);
    assertThrows(IllegalArgumentException.class,
      () -> NettyClientConfigurationUtils.createSocketAddressWith(configuration));
  }

  //create uri
  @Test
  void testCreateUri() {
    String testUri = "test-uri";
    doAnswer(__ -> testUri).when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_URI, String.class);
    URI uri = NettyClientConfigurationUtils.createUriWith(configuration);
    assertThat(uri, equalTo(URI.create(testUri)));
  }

  @Test
  void testCreateUriEmpty() {
    doAnswer(__ -> "").when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_URI, String.class);
    assertThrows(IllegalArgumentException.class,
      () -> NettyClientConfigurationUtils.createUriWith(configuration));
  }

  @Test
  void testCreateUriNull() {
    assertThrows(IllegalArgumentException.class,
      () -> NettyClientConfigurationUtils.createUriWith(configuration));
  }

  //create event loop
  @Test
  void testCreateEventLoopDefault() {
    assertDoesNotThrow(() -> NettyClientConfigurationUtils.createEventLoopGroup(configuration));
    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_GROUP_TYPE, String.class);
    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_GROUP_THREADS_COUNT, Integer.class);
  }

  @Test
  void testCreateEventLoopNio() {
    doAnswer(__ -> "NIO").when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_GROUP_TYPE, String.class);
    assertDoesNotThrow(() -> NettyClientConfigurationUtils.createEventLoopGroup(configuration));
    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_GROUP_TYPE, String.class);
    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_GROUP_THREADS_COUNT, Integer.class);
  }

  @Test
  void testCreateEventLoopEpoll() {
    doAnswer(__ -> "EPOLL").when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_GROUP_TYPE, String.class);
    assertDoesNotThrow(() -> NettyClientConfigurationUtils.createEventLoopGroup(configuration));
    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_GROUP_TYPE, String.class);
    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_GROUP_THREADS_COUNT, Integer.class);
  }

  @Test
  void testCreateEventLoopThreadCount() {
    doAnswer(__ -> 8).when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_GROUP_THREADS_COUNT, Integer.class);
    assertDoesNotThrow(() -> NettyClientConfigurationUtils.createEventLoopGroup(configuration));
    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_GROUP_TYPE, String.class);
    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_GROUP_THREADS_COUNT, Integer.class);
  }

  // resolve log level
  @Test
  void testResolveLogLevelDefault() {
    assertThat(NettyClientConfigurationUtils.resolveLogLevel(configuration), is(NettyLogLevel.DISABLED));
  }

  @Test
  void testResolveLogLevelDisabled() {
    doAnswer(__ -> Optional.of(NettyLogLevel.DISABLED.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_LOG_LEVEL, String.class);
    assertThat(NettyClientConfigurationUtils.resolveLogLevel(configuration), is(NettyLogLevel.DISABLED));
  }

  @Test
  void testResolveLogLevelWarn() {
    doAnswer(__ -> Optional.of(NettyLogLevel.WARN.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_LOG_LEVEL, String.class);
    assertThat(NettyClientConfigurationUtils.resolveLogLevel(configuration), is(NettyLogLevel.WARN));
  }

  @Test
  void testResolveLogLevelInfo() {
    doAnswer(__ -> Optional.of(NettyLogLevel.INFO.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_LOG_LEVEL, String.class);
    assertThat(NettyClientConfigurationUtils.resolveLogLevel(configuration), is(NettyLogLevel.INFO));
  }

  @Test
  void testResolveLogLevelDebug() {
    doAnswer(__ -> Optional.of(NettyLogLevel.DEBUG.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_LOG_LEVEL, String.class);
    assertThat(NettyClientConfigurationUtils.resolveLogLevel(configuration), is(NettyLogLevel.DEBUG));
  }

  @Test
  void testResolveLogLevelTrace() {
    doAnswer(__ -> Optional.of(NettyLogLevel.TRACE.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_LOG_LEVEL, String.class);
    assertThat(NettyClientConfigurationUtils.resolveLogLevel(configuration), is(NettyLogLevel.TRACE));
  }

  @Test
  void testAppendLoggingDefault() {
    ChannelHandlerAppender appender = mock(ChannelHandlerAppender.class);
    ChannelHandlerAppender handlerAppender = NettyClientConfigurationUtils.appendLogging(configuration, appender);
    //default logging is disabled. nothing to change
    assertThat(handlerAppender, is(appender));
  }

  @Test
  void testAppendLoggingWarn() {
    doAnswer(__ -> Optional.of(NettyLogLevel.WARN.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_LOG_LEVEL, String.class);
    ChannelHandlerAppender appender = mock(ChannelHandlerAppender.class);
    ChannelHandlerAppender handlerAppender = NettyClientConfigurationUtils.appendLogging(configuration, appender);
    assertThat(handlerAppender, instanceOf(LoggingAppenderDecorator.class));
  }

  // caffeine
  @Test
  void testCaffeineDefault() {
    assertDoesNotThrow(() -> NettyClientConfigurationUtils.createResponseFuturesCache(configuration));

    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_RESPONSE_CACHE_EVICTION_TIMEOUT, Integer.class);
    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_RESPONSE_CACHE_EVICTION_SIZE, Integer.class);
    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_RESPONSE_CACHE_WEAK_REFERENCE_ENABLED, Boolean.class);
    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_RESPONSE_CACHE_SOFT_VALUES_ENABLED, Boolean.class);
    verify(configuration, times(1))
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_RESPONSE_CACHE_RECORD_STATS_ENABLED, Boolean.class);
  }

  // test resolve ssl provider
  @Test
  void testResolveSslProviderDefault() {
    assertThat(NettyClientConfigurationUtils.resolveSslProvider(configuration), is(NettySslProvider.DISABLED));
  }

  @Test
  void testResolveSslProviderDisabled() {
    doAnswer(__ -> Optional.of(NettySslProvider.DISABLED.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_SSL_PROVIDER, String.class);
    assertThat(NettyClientConfigurationUtils.resolveSslProvider(configuration), is(NettySslProvider.DISABLED));
  }

  @Test
  void testResolveSslProviderJdk() {
    doAnswer(__ -> Optional.of(NettySslProvider.JDK.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_SSL_PROVIDER, String.class);
    assertThat(NettyClientConfigurationUtils.resolveSslProvider(configuration), is(NettySslProvider.JDK));
  }

  @Test
  void testResolveSslProviderOpenssl() {
    doAnswer(__ -> Optional.of(NettySslProvider.OPENSSL.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_SSL_PROVIDER, String.class);
    assertThat(NettyClientConfigurationUtils.resolveSslProvider(configuration), is(NettySslProvider.OPENSSL));
  }

  @Test
  void testResolveSslProviderOpensslREFCNT() {
    doAnswer(__ -> Optional.of(NettySslProvider.OPENSSL_REFCNT.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_SSL_PROVIDER, String.class);
    assertThat(NettyClientConfigurationUtils.resolveSslProvider(configuration), is(NettySslProvider.OPENSSL_REFCNT));
  }

  //resolve trust store
  @Test
  void testResolveTrustStoreSystemDefault() {
    assertThat(NettyClientConfigurationUtils.resolveTrustStore(configuration), is(nullValue()));
  }

  @Test
  void testResolveTrustStoreIncorrectPath() {
    doAnswer(__ -> "incorrect path").when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_SSL_TRUST_STORE_PATH, String.class);
    assertThrows(IllegalArgumentException.class, () -> NettyClientConfigurationUtils.resolveTrustStore(configuration));
  }

  // resolve key store
  @Test
  void testResolveKeyStoreSystemDefault() {
    assertThat(NettyClientConfigurationUtils.resolveKeyStore(configuration), is(nullValue()));
  }

  @Test
  void testResolveKeyStoreIncorrectPath() {
    doAnswer(__ -> "incorrect path").when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_SSL_KEY_MANAGER_CERT_PATH, String.class);
    assertThrows(IllegalArgumentException.class, () -> NettyClientConfigurationUtils.resolveKeyStore(configuration));
  }

  // resolve private key
  @Test
  void testResolvePrivateKeySystemDefault() {
    assertThat(NettyClientConfigurationUtils.resolveKey(configuration), is(nullValue()));
  }

  @Test
  void testResolvePrivateKeyIncorrectPath() {
    doAnswer(__ -> "incorrect path").when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_SSL_KEY_PATH, String.class);
    assertThrows(IllegalArgumentException.class, () -> NettyClientConfigurationUtils.resolveKey(configuration));
  }

  // resolve private key password
  @Test
  void testResolvePrivateKeyPasswordSystemDefault() {
    assertThat(NettyClientConfigurationUtils.resolveKeyPassword(configuration), is(nullValue()));
  }

  @Test
  void testResolvePrivateKey() {
    String testPassword = "testPassword";
    doAnswer(__ -> testPassword).when(configuration)
      .getValue(NettyClientConfigurationUtils.NETTY_CLIENT_SSL_KEY_PASSWORD, String.class);
    String password = assertDoesNotThrow(() -> NettyClientConfigurationUtils.resolveKeyPassword(configuration));
    assertThat(password, is(testPassword));
  }

  // resolve client auth
  @Test
  void testResolveClientAuthDefault() {
    assertThat(NettyClientConfigurationUtils.resolveClientAuth(configuration), is(ClientAuth.NONE));
  }

  @Test
  void testResolveClientAuthNone() {
    doAnswer(__ -> Optional.of(ClientAuth.NONE.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_SSL_AUTH_MODE, String.class);
    assertThat(NettyClientConfigurationUtils.resolveClientAuth(configuration), is(ClientAuth.NONE));
  }

  @Test
  void testResolveClientAuthOptional() {
    doAnswer(__ -> Optional.of(ClientAuth.OPTIONAL.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_SSL_AUTH_MODE, String.class);
    assertThat(NettyClientConfigurationUtils.resolveClientAuth(configuration), is(ClientAuth.OPTIONAL));
  }

  @Test
  void testResolveClientAuthRequire() {
    doAnswer(__ -> Optional.of(ClientAuth.REQUIRE.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_SSL_AUTH_MODE, String.class);
    assertThat(NettyClientConfigurationUtils.resolveClientAuth(configuration), is(ClientAuth.REQUIRE));
  }

  //test append ssl

  @Test
  void testAppendSslDefault() {
    ChannelHandlerAppender testAppender = mock(ChannelHandlerAppender.class);
    ChannelHandlerAppender appender = NettyClientConfigurationUtils.appendSsl(configuration, testAppender);
    //default ssl is disabled. Nothing to change
    assertThat(appender, is(testAppender));
  }

  @Test
  void testAppendSslEnabled() {
    doAnswer(__ -> Optional.of(NettySslProvider.JDK.toString())).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_SSL_PROVIDER, String.class);
    ChannelHandlerAppender testAppender = mock(ChannelHandlerAppender.class);
    ChannelHandlerAppender appender = NettyClientConfigurationUtils.appendSsl(configuration, testAppender);
    assertThat(appender, instanceOf(SslAppenderDecorator.class));
  }

  //resolve frame decoder config


  @Test
  void testResolveFrameDecoderConfigDefault() {
    FrameDecoderConfig frameDecoderConfig = NettyClientConfigurationUtils.resolveFrameDecoderConfig(configuration);
    assertThat(frameDecoderConfig, not(nullValue()));
    assertThat(frameDecoderConfig.getLengthAdjustment(),
      equalTo(NettyClientConfigurationUtils.DEFAULT_NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_ADJUSTMENT));
    assertThat(frameDecoderConfig.getMaxFrameLength(),
      equalTo(NettyClientConfigurationUtils.DEFAULT_NETTY_CLIENT_FRAME_DECODER_MAX_FRAME_LENGTH));
    assertThat(frameDecoderConfig.getLengthFieldLength(),
      equalTo(NettyClientConfigurationUtils.DEFAULT_NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_LENGTH));
    assertThat(frameDecoderConfig.getLengthFieldOffset(),
      equalTo(NettyClientConfigurationUtils.DEFAULT_NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_OFFSET));
    assertThat(frameDecoderConfig.getInitialBytesToStrip(),
      equalTo(NettyClientConfigurationUtils.DEFAULT_NETTY_CLIENT_FRAME_DECODER_LENGTH_STRIP_BITES));
  }

  @Test
  void testResolveFrameDecoderConfig() {
    int testLengthFieldAdjustment = 60100;
    doAnswer(__ -> Optional.of(testLengthFieldAdjustment)).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_ADJUSTMENT, Integer.class);
    int testMaxFrameLength = 1010;
    doAnswer(__ -> Optional.of(testMaxFrameLength)).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_FRAME_DECODER_MAX_FRAME_LENGTH, Integer.class);
    int testLengthFieldLength = 1020;
    doAnswer(__ -> Optional.of(testLengthFieldLength)).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_LENGTH, Integer.class);
    int testLengthFieldOffset = 1030;
    doAnswer(__ -> Optional.of(testLengthFieldOffset)).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_FRAME_DECODER_LENGTH_FIELD_OFFSET, Integer.class);
    int testLengthStripBites = 1040;
    doAnswer(__ -> Optional.of(testLengthStripBites)).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_FRAME_DECODER_LENGTH_STRIP_BITES, Integer.class);
    FrameDecoderConfig frameDecoderConfig = NettyClientConfigurationUtils.resolveFrameDecoderConfig(configuration);
    assertThat(frameDecoderConfig, not(nullValue()));
    assertThat(frameDecoderConfig.getLengthAdjustment(), equalTo(testLengthFieldAdjustment));
    assertThat(frameDecoderConfig.getMaxFrameLength(), equalTo(testMaxFrameLength));
    assertThat(frameDecoderConfig.getLengthFieldLength(), equalTo(testLengthFieldLength));
    assertThat(frameDecoderConfig.getLengthFieldOffset(), equalTo(testLengthFieldOffset));
    assertThat(frameDecoderConfig.getInitialBytesToStrip(), equalTo(testLengthStripBites));
  }

  //resolve encoder frame length
  @Test
  void testResolveEncoderFrameLengthDefault() {
    assertThat(NettyClientConfigurationUtils.resolveEncoderFrameLength(configuration),
      is(NettyClientConfigurationUtils.DEFAULT_NETTY_CLIENT_FRAME_ENCODER_LENGTH_FIELD_LENGTH));
  }

  @Test
  void testResolveEncoderFrameLength() {
    int testLengthFieldAdjustment = 60100;
    doAnswer(__ -> Optional.of(testLengthFieldAdjustment)).when(configuration)
      .getOptionalValue(NettyClientConfigurationUtils.NETTY_CLIENT_FRAME_ENCODER_LENGTH_FIELD_LENGTH, Integer.class);
    assertThat(NettyClientConfigurationUtils.resolveEncoderFrameLength(configuration), is(testLengthFieldAdjustment));
  }
}
