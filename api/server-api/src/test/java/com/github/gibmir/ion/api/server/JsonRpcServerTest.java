package com.github.gibmir.ion.api.server;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import org.junit.jupiter.api.Test;

class JsonRpcServerTest {
  public interface TestApi extends JsonRemoteProcedure0<String> {

  }

  public class TestApiImpl implements TestApi {

    @Override
    public String call() {
      return "pog";
    }
  }

  @Test
  void smoke() {
  }
}
