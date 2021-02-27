package com.github.gibmir.ion.lib.netty.client.environment;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
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

public class TestEnvironment {
  public static final Jsonb TEST_REAL_JSONB = JsonbBuilder.create();
  public static final JsonRpcRequestEncoder TEST_REQUEST_ENCODER = new JsonRpcRequestEncoder();

  public static final String TEST_JSON = "test-json";
  public static final ResponseListenerRegistry TEST_EMPTY_RESPONSE_LISTENER_REGISTRY = ResponseListenerRegistryMock.emptyMock();
  public static final JsonRpcResponseHandler TEST_RESPONSE_HANDLER = new JsonRpcResponseHandler(TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
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
  public static final String TEST_FIRST_ARG = "test-first-arg";
  public static final String TEST_SECOND_ARG = "test-second-arg";
  public static final String TEST_THIRD_ARG = "test-third-arg";

  private TestEnvironment() {
  }

  public static class TestException extends RuntimeException {

  }


  //procedures
  public static class TestProcedure0 implements JsonRemoteProcedure0<String> {
    @Override
    public String call() {
      return "testValue";
    }
  }

  public static class TestProcedure1 implements JsonRemoteProcedure1<String, String> {
    @Override
    public String call(String arg) {
      return arg + "testValue";
    }
  }

  public static class TestProcedure2 implements JsonRemoteProcedure2<String, String, String> {

    @Override
    public String call(String arg1, String arg2) {
      return arg1 + arg2 + "testValue";
    }
  }

  public static class TestProcedure3 implements JsonRemoteProcedure3<String, String, String, String> {

    @Override
    public String call(String arg1, String arg2, String arg3) {
      return arg1 + arg2 + arg3 + "testValue";
    }
  }
}
