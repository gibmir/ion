package com.github.gibmir.ion.lib.netty.client.environment;

import com.github.gibmir.ion.lib.netty.client.environment.mock.JsonbMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ResponseListenerRegistryMock;
import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.common.channel.initializer.JsonRpcClientChannelInitializer;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NettyClientTestEnvironment {
  public static final Jsonb TEST_REAL_JSONB = JsonbBuilder.create();
  public static final JsonRpcRequestEncoder TEST_REQUEST_ENCODER = new JsonRpcRequestEncoder();

  public static final String TEST_JSON = "test-json";
  public static final ResponseListenerRegistry TEST_EMPTY_RESPONSE_LISTENER_REGISTRY = ResponseListenerRegistryMock.emptyMock();
  public static final JsonRpcResponseHandler TEST_RESPONSE_HANDLER = new JsonRpcResponseHandler(JsonbMock.newMock(TEST_JSON),
    TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
  public static final Charset TEST_CHARSET = StandardCharsets.UTF_8;
  public static final JsonRpcResponseDecoder TEST_RESPONSE_DECODER = new JsonRpcResponseDecoder(JsonbMock.newMock(TEST_JSON), TEST_CHARSET);
  public static final LoggingHandler TEST_LOGGING_HANDLER = new LoggingHandler(LogLevel.ERROR);
  public static final JsonRpcClientChannelInitializer TEST_NETTY_CLIENT_INITIALIZER =
    JsonRpcClientChannelInitializer.builder(TEST_REQUEST_ENCODER, TEST_RESPONSE_DECODER, TEST_RESPONSE_HANDLER)
      .withLogging(TEST_LOGGING_HANDLER).build();
  public static final String TEST_PROCEDURE_NAME = "test-procedure";
  public static final InetSocketAddress TEST_SOCKET_ADDRESS = InetSocketAddress.createUnresolved("localhost", 55_555);
  public static final String TEST_ID = "test-id";
  public static final Object[] TEST_ARGS = new Object[]{1, "arg"};

  private NettyClientTestEnvironment() {
  }

  public static class TestException extends RuntimeException {

  }
}
