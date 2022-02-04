package com.github.gibmir.ion.lib.netty.server.common.processor.factory;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;

import java.lang.invoke.MethodHandle;

import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TestProcedure0;
import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TestProcedure1;
import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TestProcedure2;
import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TestProcedure3;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JsonRpcRequestProcessorFactoryTest {
  public static final String EXPECTED_METHOD_NAME = "testMethodName";
  public static final String JSON_ARGS_STRING = "testArguments";
  public static final String EXPECTED_ID = "test-id";
  Logger logger;
  Jsonb jsonb;
  TestProcedure0 testProcedure0;
  TestProcedure1 testProcedure1;
  TestProcedure2 testProcedure2;
  TestProcedure3 testProcedure3;
  private JsonValue jsonArgs;
  private JsonRpcRequestProcessorFactory.NamedMethodHandle methodHandle;

  @BeforeEach
  void beforeEach() {
    logger = mock(Logger.class);
    jsonb = mock(Jsonb.class);
    testProcedure0 = mock(TestProcedure0.class);
    testProcedure1 = mock(TestProcedure1.class);
    testProcedure2 = mock(TestProcedure2.class);
    testProcedure3 = mock(TestProcedure3.class);
    jsonArgs = mock(JsonValue.class);
    methodHandle = mock(JsonRpcRequestProcessorFactory.NamedMethodHandle.class);
  }

  @Test
  void testCreateProcessor0WithException() {
    assertThrows(IllegalArgumentException.class,
      () -> JsonRpcRequestProcessorFactory.createProcessor0(String.class, testProcedure0, jsonb));
  }

  @Test
  void testCreateProcessor1WithException() {
    assertThrows(IllegalArgumentException.class,
      () -> JsonRpcRequestProcessorFactory.createProcessor1(String.class, testProcedure1, jsonb));
  }

  @Test
  void testCreateProcessor2WithException() {
    assertThrows(IllegalArgumentException.class,
      () -> JsonRpcRequestProcessorFactory.createProcessor2(String.class, testProcedure2, jsonb));
  }

  @Test
  void testCreateProcessor3WithException() {
    assertThrows(IllegalArgumentException.class,
      () -> JsonRpcRequestProcessorFactory.createProcessor3(String.class, testProcedure3, jsonb));
  }

  @Test
  void testProcessNotificationWithNullResponse() throws Throwable {
    JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<Object> processor =
      new JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<>(methodHandle, testProcedure0, jsonb, logger);

    Object[] expectedArgs = {};

    doAnswer(__ -> null).when(jsonb).fromJson(eq(JSON_ARGS_STRING), eq(JsonValue.class));
    processor.processNotification(EXPECTED_METHOD_NAME, JSON_ARGS_STRING);

    verify(methodHandle).invokeWithArguments(eq(testProcedure0), eq(expectedArgs));
    verify(logger).debug(anyString(), eq(EXPECTED_METHOD_NAME));
  }

  @Test
  void testProcessNotificationWithEmptyArrayArgsAndNotNullResponse() throws Throwable {
    Object[] expectedArgs = {};
    doAnswer(__ -> 0).when(methodHandle).getArgumentsCount();
    doAnswer(__ -> "response").when(methodHandle).invokeWithArguments(eq(testProcedure0), eq(expectedArgs));
    JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<Object> processor =
      new JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<>(methodHandle, testProcedure0, jsonb, logger);

    doAnswer(__ -> JsonValue.ValueType.ARRAY).when(jsonArgs).getValueType();
    doAnswer(__ -> jsonArgs).when(jsonb).fromJson(eq(JSON_ARGS_STRING), eq(JsonValue.class));

    processor.processNotification(EXPECTED_METHOD_NAME, JSON_ARGS_STRING);

    verify(methodHandle).invokeWithArguments(eq(testProcedure0), eq(expectedArgs));
    verify(logger).warn(anyString(), eq(EXPECTED_METHOD_NAME));
  }

  @Test
  void testProcessNotificationWithNamedArgsAndNotNullResponse() throws Throwable {
    Object[] expectedArgs = {};
    doAnswer(__ -> "response").when(methodHandle).invokeWithArguments(eq(testProcedure0), eq(expectedArgs));
    JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<Object> processor =
      new JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<>(methodHandle, testProcedure0, jsonb, logger);

    doAnswer(__ -> JsonValue.ValueType.OBJECT).when(jsonArgs).getValueType();
    doAnswer(__ -> jsonArgs).when(jsonb).fromJson(eq(JSON_ARGS_STRING), eq(JsonValue.class));

    processor.processNotification(EXPECTED_METHOD_NAME, JSON_ARGS_STRING);

    verify(methodHandle).invokeWithArguments(eq(testProcedure0), eq(expectedArgs));
    verify(logger).warn(anyString(), eq(EXPECTED_METHOD_NAME));
  }

  @Test
  void testProcessNotificationWithIncorrectJson() {
    doAnswer(__ -> JsonValue.ValueType.NULL).when(jsonArgs).getValueType();
    doAnswer(__ -> jsonArgs).when(jsonb).fromJson(eq(JSON_ARGS_STRING), eq(JsonValue.class));

    JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<Object> processor =
      new JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<>(methodHandle, testProcedure0, jsonb, logger);

    processor.processNotification(EXPECTED_METHOD_NAME, JSON_ARGS_STRING);

    verify(logger).error(contains(EXPECTED_METHOD_NAME));
    verify(logger).error(contains(String.valueOf(Errors.INVALID_METHOD_PARAMETERS.getError().getCode())));
    verify(logger).error(contains(Errors.INVALID_METHOD_PARAMETERS.getError().getMessage()));
  }

  @Test
  void testProcessNotificationWithException() throws Throwable {
    Object[] expectedArgs = {};
    TestException expectedException = new TestException();
    Mockito.doThrow(expectedException).when(methodHandle).invokeWithArguments(eq(testProcedure0), eq(expectedArgs));
    doAnswer(__ -> JsonValue.ValueType.OBJECT).when(jsonArgs).getValueType();
    doAnswer(__ -> jsonArgs).when(jsonb).fromJson(eq(JSON_ARGS_STRING), eq(JsonValue.class));

    JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<Object> processor =
      new JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<>(methodHandle, testProcedure0, jsonb, logger);

    processor.processNotification(EXPECTED_METHOD_NAME, JSON_ARGS_STRING);

    verify(methodHandle).invokeWithArguments(eq(testProcedure0), eq(expectedArgs));
    verify(logger).error(anyString(), eq(EXPECTED_METHOD_NAME), eq(expectedException));
  }

  @Test
  void testProcessRequestWithNullResponse() {
    JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<Object> processor =
      new JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<>(methodHandle, testProcedure0, jsonb, logger);

    JsonRpcResponse response = processor.processRequest(EXPECTED_ID, EXPECTED_METHOD_NAME, JSON_ARGS_STRING);

    assertThat(response, instanceOf(SuccessResponse.class));
    SuccessResponse success = (SuccessResponse) response;
    assertThat(success.getId(), is(EXPECTED_ID));
  }

  @Test
  void testProcessArrayRequestWithCorrectResponse() {
    doAnswer(__ -> JsonValue.ValueType.ARRAY).when(jsonArgs).getValueType();
    doAnswer(__ -> jsonArgs).when(jsonb).fromJson(eq(JSON_ARGS_STRING), eq(JsonValue.class));

    JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<Object> processor =
      new JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<>(methodHandle, testProcedure0, jsonb, logger);

    JsonRpcResponse response = processor.processRequest(EXPECTED_ID, EXPECTED_METHOD_NAME, JSON_ARGS_STRING);

    assertThat(response, instanceOf(SuccessResponse.class));
    SuccessResponse success = (SuccessResponse) response;
    assertThat(success.getId(), is(EXPECTED_ID));
  }

  @Test
  void testProcessNamedRequestWithCorrectResponse() {
    doAnswer(__ -> JsonValue.ValueType.OBJECT).when(jsonArgs).getValueType();
    doAnswer(__ -> jsonArgs).when(jsonb).fromJson(eq(JSON_ARGS_STRING), eq(JsonValue.class));

    JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<Object> processor =
      new JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<>(methodHandle, testProcedure0, jsonb, logger);

    JsonRpcResponse response = processor.processRequest(EXPECTED_ID, EXPECTED_METHOD_NAME, JSON_ARGS_STRING);

    assertThat(response, instanceOf(SuccessResponse.class));
    SuccessResponse success = (SuccessResponse) response;
    assertThat(success.getId(), is(EXPECTED_ID));
  }

  @Test
  void testProcessIncorrectValueTypeRequest() {
    doAnswer(__ -> JsonValue.ValueType.NULL).when(jsonArgs).getValueType();
    doAnswer(__ -> jsonArgs).when(jsonb).fromJson(eq(JSON_ARGS_STRING), eq(JsonValue.class));

    JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<Object> processor =
      new JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<>(methodHandle, testProcedure0, jsonb, logger);

    JsonRpcResponse response = processor.processRequest(EXPECTED_ID, EXPECTED_METHOD_NAME, JSON_ARGS_STRING);
    assertThat(response, instanceOf(ErrorResponse.class));
    ErrorResponse error = (ErrorResponse) response;
    assertThat(error.getId(), is(EXPECTED_ID));
    assertThat(error.getJsonRpcError().getCode(), equalTo(Errors.INVALID_METHOD_PARAMETERS.getError().getCode()));
    assertThat(error.getJsonRpcError().getMessage(), containsString(Errors.INVALID_METHOD_PARAMETERS.getError().getMessage()));
    assertThat(error.getJsonRpcError().getMessage(), containsString(EXPECTED_ID));
  }

  @Test
  void testProcessRequestWithError() throws Throwable {
    doAnswer(__ -> JsonValue.ValueType.OBJECT).when(jsonArgs).getValueType();
    doAnswer(__ -> jsonArgs).when(jsonb).fromJson(eq(JSON_ARGS_STRING), eq(JsonValue.class));
    TestException testException = new TestException();
    Object[] expectedArgs = {};
    doThrow(testException).when(methodHandle).invokeWithArguments(eq(testProcedure0), eq(expectedArgs));
    JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<Object> processor =
      new JsonRpcRequestProcessorFactory.MethodHandleJsonRpcRequestProcessor<>(methodHandle, testProcedure0, jsonb, logger);

    JsonRpcResponse response = processor.processRequest(EXPECTED_ID, EXPECTED_METHOD_NAME, JSON_ARGS_STRING);

    assertThat(response, instanceOf(ErrorResponse.class));
    ErrorResponse error = (ErrorResponse) response;
    assertThat(error.getId(), is(EXPECTED_ID));
    assertThat(error.getJsonRpcError().getCode(), equalTo(Errors.APPLICATION_ERROR.getError().getCode()));
    assertThat(error.getJsonRpcError().getMessage(), containsString(Errors.APPLICATION_ERROR.getError().getMessage()));
    assertThat(error.getJsonRpcError().getMessage(), containsString(EXPECTED_ID));
  }

  @Test
  void testMethodHandleSmoke() {
    MethodHandle methodHandle = mock(MethodHandle.class);
    Class<String> argumentType = String.class;
    Class<?>[] argumentTypes = {argumentType};
    String argumentName = "argumentName";
    String[] parameterNames = {argumentName};
    JsonRpcRequestProcessorFactory.NamedMethodHandle handle =
      new JsonRpcRequestProcessorFactory.NamedMethodHandle(methodHandle, parameterNames, argumentTypes);

    assertThat(handle.getArgumentsCount(), is(argumentTypes.length));
    assertThat(handle.getParameterNamesCount(), is(parameterNames.length));
    assertThat(handle.getArgumentType(0), is(argumentType));
    assertThat(handle.getParameterName(0), is(argumentName));

  }

  public static class TestException extends RuntimeException {

  }
}
