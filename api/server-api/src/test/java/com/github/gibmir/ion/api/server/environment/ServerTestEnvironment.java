package com.github.gibmir.ion.api.server.environment;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.procedure.named.Named;

public class ServerTestEnvironment {
  //argument types
  public interface FirstTestType {

  }

  public interface SecondTestType {

  }

  public interface ThirdTestType {

  }

  public interface ReturnTestType {

  }

  //classic procedures
  public interface TestProcedure0 extends JsonRemoteProcedure0<ReturnTestType> {

  }

  public interface TestProcedure1 extends JsonRemoteProcedure1<FirstTestType, ReturnTestType> {
    @Override
    ReturnTestType call(@Named(name = "some") FirstTestType arg);
  }

  public interface TestProcedure2 extends JsonRemoteProcedure2<FirstTestType, SecondTestType, ReturnTestType> {

  }

  public interface TestProcedure3 extends JsonRemoteProcedure3<FirstTestType, SecondTestType, ThirdTestType, ReturnTestType> {

  }

  //hierarchy procedures
  public interface HierarchyTestProcedure0 extends TestProcedure0 {

  }

  public interface HierarchyTestProcedure1 extends TestProcedure1 {

  }

  public interface HierarchyTestProcedure2 extends TestProcedure2 {

  }

  public interface HierarchyTestProcedure3 extends TestProcedure3 {

  }

  //procedures with raw generics
  @SuppressWarnings("rawtypes")
  public interface RawTestProcedure1 extends JsonRemoteProcedure1 {

  }

  @SuppressWarnings("rawtypes")
  public interface RawTestProcedure2 extends JsonRemoteProcedure2 {

  }

  @SuppressWarnings("rawtypes")
  public interface RawTestProcedure3 extends JsonRemoteProcedure3 {

  }

  //multiple implementation procedures
  public interface MultipleTestProcedure1 extends Comparable<String>, JsonRemoteProcedure1<String, String> {

  }

  public interface MultipleTestProcedure2 extends Comparable<String>, JsonRemoteProcedure2<String, String, String> {

  }

  public interface MultipleTestProcedure3 extends Comparable<String>,
    JsonRemoteProcedure3<String, String, String, String> {

  }

  //test exception
  public static class ServerTestException extends RuntimeException {

  }
}
