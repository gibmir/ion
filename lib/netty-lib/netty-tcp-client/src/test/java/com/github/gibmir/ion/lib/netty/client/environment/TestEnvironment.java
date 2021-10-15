package com.github.gibmir.ion.lib.netty.client.environment;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ResponseListenerRegistryMock;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TestEnvironment {
  public static final Jsonb TEST_REAL_JSONB = JsonbBuilder.create();

  public static final String TEST_JSON = "test-json";
  public static final ResponseListenerRegistry TEST_EMPTY_RESPONSE_LISTENER_REGISTRY = ResponseListenerRegistryMock.emptyMock();
  public static final Charset TEST_CHARSET = StandardCharsets.UTF_8;
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
