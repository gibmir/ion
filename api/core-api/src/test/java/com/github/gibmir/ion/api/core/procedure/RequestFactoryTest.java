package com.github.gibmir.ion.api.core.procedure;

import com.github.gibmir.ion.api.client.sender.JsonRpcRequestSender;
import com.github.gibmir.ion.api.core.environment.IonTestEnvironment;
import com.github.gibmir.ion.api.core.environment.mock.JsonRpcChannelMock;
import com.github.gibmir.ion.api.core.request.factory.RequestFactory;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import org.junit.jupiter.api.Test;

import javax.json.stream.JsonParsingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.github.gibmir.ion.api.core.environment.IonTestEnvironment.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RequestFactoryTest {

  @Test
  void testExecuteCorrectSuccessResponse() throws Throwable {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithResponsePayload(SUCCESS_RESPONSE_PAYLOAD);
    TestDto resultDto = TEST_API_PROCEDURE
      .call(TEST_CORRECT_DTO)
      .initialize(() -> JSON_RPC_ID, jsonRpcRequestSender)
      .jsonb(JSONB)
      .execute();
    assertThat(resultDto, equalTo(TEST_CORRECT_DTO));
  }

  @Test
  void testExecuteCorrectErrorResponse() throws Throwable {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithResponsePayload(ERROR_RESPONSE_PAYLOAD);
    JsonRpcProcessingException jsonRpcProcessingException = assertThrows(JsonRpcProcessingException.class,
      () -> TEST_API_PROCEDURE
        .call(TEST_CORRECT_DTO)
        .initialize(() -> JSON_RPC_ID, jsonRpcRequestSender)
        .jsonb(JSONB)
        .execute());
    assertThat(jsonRpcProcessingException.getMessage(), containsString(TEST_ERROR_RESPONSE.toString()));
  }

  @Test
  void testExecuteWithTimeout() throws Throwable {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithTimeout(5, TimeUnit.SECONDS);
    assertThrows(TimeoutException.class, () -> TEST_API_PROCEDURE
      .call(TEST_CORRECT_DTO)
      .initialize(() -> JSON_RPC_ID, jsonRpcRequestSender)
      .timeout(1, TimeUnit.SECONDS)
      .jsonb(JSONB)
      .execute());
  }

  @Test
  void testExecuteWithIncorrectResponseBody() throws Throwable {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithResponsePayload(new byte[]{1, 2, 3, 4, 5});
    JsonRpcProcessingException jsonRpcProcessingException = assertThrows(JsonRpcProcessingException.class,
      () -> TEST_API_PROCEDURE.call(TEST_CORRECT_DTO)
        .initialize(() -> JSON_RPC_ID, jsonRpcRequestSender)
        .timeout(1, TimeUnit.SECONDS)
        .jsonb(JSONB)
        .execute());
    assertThat(jsonRpcProcessingException.getCause(), instanceOf(JsonParsingException.class));
  }

  @Test
  void testExecutionWithChannelException() throws Throwable {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithException(new TestException());
    JsonRpcProcessingException jsonRpcProcessingException = assertThrows(JsonRpcProcessingException.class,
      () -> TEST_API_PROCEDURE.call(TEST_CORRECT_DTO)
        .initialize(() -> JSON_RPC_ID, jsonRpcRequestSender)
        .timeout(1, TimeUnit.SECONDS)
        .jsonb(JSONB)
        .execute());
    assertThat(jsonRpcProcessingException.getCause(), instanceOf(TestException.class));
  }

  @Test
  void testExecuteAsyncCorrectSuccessResponse() throws Throwable {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithResponsePayload(SUCCESS_RESPONSE_PAYLOAD);
    IonTestEnvironment.SyncCallback<TestDto> responseCallback = new IonTestEnvironment.SyncCallback<>(1, TimeUnit.SECONDS);
    TEST_API_PROCEDURE.call(TEST_CORRECT_DTO)
      .initialize(() -> JSON_RPC_ID, jsonRpcRequestSender)
      .jsonb(JSONB)
      .executeAsync(responseCallback);
    responseCallback.awaitResult();
    assertThat(responseCallback.getResult(), equalTo(TEST_CORRECT_DTO));
  }

  @Test
  void testExecuteAsyncCorrectErrorResponse() throws Throwable {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithResponsePayload(ERROR_RESPONSE_PAYLOAD);
    JsonRpcProcessingException jsonRpcProcessingException = assertThrows(JsonRpcProcessingException.class,
      () -> {
        IonTestEnvironment.SyncCallback<TestDto> responseCallback = new IonTestEnvironment.SyncCallback<>(1, TimeUnit.MINUTES);
        TEST_API_PROCEDURE
          .call(TEST_CORRECT_DTO)
          .initialize(() -> JSON_RPC_ID, jsonRpcRequestSender)
          .jsonb(JSONB)
          .executeAsync(responseCallback);
        responseCallback.awaitResult();
        throw responseCallback.getThrowable();
      });
    assertThat(jsonRpcProcessingException.getMessage(), containsString(TEST_ERROR_RESPONSE.toString()));
  }

  @Test
  void testExecuteAsyncWithTimeout() throws Throwable {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithTimeout(15, TimeUnit.SECONDS);
    assertThrows(TimeoutException.class, () -> {
      IonTestEnvironment.SyncCallback<TestDto> responseCallback = new IonTestEnvironment.SyncCallback<>(1, TimeUnit.MINUTES);
      TEST_API_PROCEDURE
        .call(TEST_CORRECT_DTO)
        .initialize(() -> JSON_RPC_ID, jsonRpcRequestSender)
        .timeout(1, TimeUnit.SECONDS)
        .jsonb(JSONB)
        .executeAsync(responseCallback);
      responseCallback.awaitResult();
      throw responseCallback.getThrowable();
    });
  }
}
