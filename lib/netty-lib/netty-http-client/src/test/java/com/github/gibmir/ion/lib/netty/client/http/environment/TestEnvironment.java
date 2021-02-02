package com.github.gibmir.ion.lib.netty.client.http.environment;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;

import java.net.URI;
import java.nio.charset.Charset;

public class TestEnvironment {

  public static final Charset TEST_CHARSET = Charset.defaultCharset();
  public static final URI TEST_URI = URI.create("http://localhost/test");
  public static final String TEST_ID = "test-id";
  public static final String TEST_FIRST_ARG = "test-first-arg";
  public static final String TEST_SECOND_ARG = "test-second-arg";
  public static final String TEST_THIRD_ARG = "test-third-arg";

  private TestEnvironment() {
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
