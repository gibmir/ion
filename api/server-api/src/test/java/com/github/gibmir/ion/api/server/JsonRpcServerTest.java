package com.github.gibmir.ion.api.server;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import org.junit.jupiter.api.Test;

class JsonRpcServerTest {
  public interface TestProcedure extends JsonRemoteProcedure0<String> {

  }

  public class TestProcedureImpl implements TestProcedure {

    @Override
    public String call() {
      return "pog";
    }
  }

  @Test
  void smoke() {
  }
}
