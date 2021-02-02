package com.github.gibmir.ion.lib.netty.client.common.environment;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.NettyBatch;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.reflect.Type;

public class TestEnvironment {
  public static final String TEST_ID = "someId";
  public static final String TEST_FIRST_ARG = "testFirstArg";
  public static final String TEST_SECOND_ARG = "testSecondArg";
  public static final String TEST_THIRD_ARG = "testThirdArg";

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

  // matchers
  public static class AwaitBatchPartWithId extends TypeSafeMatcher<NettyBatch.AwaitBatchPart> {
    private final String id;
    private String checkedId;

    private AwaitBatchPartWithId(String id) {
      this.id = id;
    }

    @Override
    protected boolean matchesSafely(NettyBatch.AwaitBatchPart item) {
      return (checkedId = item.getId()).equals(id);
    }

    @Override
    public void describeTo(Description description) {
      description.appendText(String.format("id %s should be equal to %s", checkedId, id));
    }

    public static TypeSafeMatcher<NettyBatch.AwaitBatchPart> awaitBatchPartWithId(String id) {
      return new AwaitBatchPartWithId(id);
    }
  }

  public static class AwaitBatchPartWithReturnType extends TypeSafeMatcher<NettyBatch.AwaitBatchPart> {
    private final Type type;
    private Type checkedType;

    private AwaitBatchPartWithReturnType(Type type) {
      this.type = type;
    }

    @Override
    protected boolean matchesSafely(NettyBatch.AwaitBatchPart item) {
      return (checkedType = item.getReturnType()).equals(type);
    }

    @Override
    public void describeTo(Description description) {
      description.appendText(String.format("type %s should be equal to %s", checkedType, type));
    }

    public static TypeSafeMatcher<NettyBatch.AwaitBatchPart> awaitBatchPartWithReturnType(Type type) {
      return new AwaitBatchPartWithReturnType(type);
    }
  }
}