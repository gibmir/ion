package com.github.gibmir.ion.lib.netty.server.environment;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;

public class NettyServerTestEnvironment {
  public interface TestApi0 extends JsonRemoteProcedure0<String> {

  }

  public interface TestApi1 extends JsonRemoteProcedure1<String, String> {

  }

  public interface TestApi2 extends JsonRemoteProcedure2<String, String, String> {

  }

  public interface TestApi3 extends JsonRemoteProcedure3<String, String, String, String> {

  }

  public static class TestException extends RuntimeException {

  }
}
