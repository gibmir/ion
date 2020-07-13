package com.github.gibmir.ion.api.server.environment;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;

public class ServerTestEnvironment {

  public interface TestProcedure0 extends JsonRemoteProcedure0<String> {

  }

  public interface TestProcedure1 extends JsonRemoteProcedure1<String, String> {

  }

  public interface TestProcedure2 extends JsonRemoteProcedure2<String, String, String> {

  }

  public interface TestProcedure3 extends JsonRemoteProcedure3<String, String, String, String> {

  }

  public interface IncorrectTestProcedure0 extends TestProcedure0 {

  }

  public interface IncorrectTestProcedure1 extends TestProcedure1 {

  }

  public interface IncorrectTestProcedure2 extends TestProcedure2 {

  }

  public interface IncorrectTestProcedure3 extends TestProcedure3 {

  }

  public interface RawTestProcedure1 extends JsonRemoteProcedure1 {

  }

  public interface RawTestProcedure2 extends JsonRemoteProcedure2 {

  }

  public interface RawTestProcedure3 extends JsonRemoteProcedure3 {

  }

  public interface MultipleTestProcedure1 extends Comparable<String>, JsonRemoteProcedure1<String, String> {

  }

  public interface MultipleTestProcedure2 extends Comparable<String>, JsonRemoteProcedure2<String, String, String> {

  }

  public interface MultipleTestProcedure3 extends Comparable<String>,
    JsonRemoteProcedure3<String, String, String, String> {

  }

  public static class ServerTestException extends RuntimeException {

  }
}
