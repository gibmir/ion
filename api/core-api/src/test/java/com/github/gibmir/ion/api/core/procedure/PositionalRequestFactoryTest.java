package com.github.gibmir.ion.api.core.procedure;

import com.github.gibmir.ion.api.client.sender.JsonRpcRequestSender;
import com.github.gibmir.ion.api.core.environment.mock.JsonRpcChannelMock;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import org.junit.jupiter.api.Test;

import javax.json.stream.JsonParsingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.github.gibmir.ion.api.core.environment.IonTestEnvironment.ERROR_RESPONSE_PAYLOAD;
import static com.github.gibmir.ion.api.core.environment.IonTestEnvironment.JSONB;
import static com.github.gibmir.ion.api.core.environment.IonTestEnvironment.JSON_RPC_ID;
import static com.github.gibmir.ion.api.core.environment.IonTestEnvironment.SUCCESS_RESPONSE_PAYLOAD;
import static com.github.gibmir.ion.api.core.environment.IonTestEnvironment.TEST_API_PROCEDURE;
import static com.github.gibmir.ion.api.core.environment.IonTestEnvironment.TEST_CORRECT_DTO;
import static com.github.gibmir.ion.api.core.environment.IonTestEnvironment.TEST_ERROR_RESPONSE;
import static com.github.gibmir.ion.api.core.environment.IonTestEnvironment.TestDto;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PositionalRequestFactoryTest {

  @Test
  void testExecuteCorrectSuccessResponse() throws Throwable {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithResponsePayload(SUCCESS_RESPONSE_PAYLOAD);
    TestDto resultDto = TEST_API_PROCEDURE
      .call(TEST_CORRECT_DTO)
      .initialize(() -> JSON_RPC_ID, jsonRpcRequestSender)
      .jsonb(JSONB)
      .execute().get();
    assertThat(resultDto, equalTo(TEST_CORRECT_DTO));
  }

  @Test
  void testExecuteCorrectErrorResponse() {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithResponsePayload(ERROR_RESPONSE_PAYLOAD);
    ExecutionException executionException = assertThrows(ExecutionException.class,
      () -> TEST_API_PROCEDURE
        .call(TEST_CORRECT_DTO)
        .initialize(() -> JSON_RPC_ID, jsonRpcRequestSender)
        .jsonb(JSONB)
        .execute()
        .get());
    Throwable cause = executionException.getCause();
    assertThat(cause, instanceOf(JsonRpcProcessingException.class));
    assertThat(cause.getMessage(), containsString(TEST_ERROR_RESPONSE.toString()));
  }

  @Test
  void testExecuteWithTimeout() {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithTimeout(5, TimeUnit.SECONDS);
    assertThrows(TimeoutException.class, () -> TEST_API_PROCEDURE
      .call(TEST_CORRECT_DTO)
      .initialize(() -> JSON_RPC_ID, jsonRpcRequestSender)
      .jsonb(JSONB)
      .execute()
      .get(1, TimeUnit.MILLISECONDS));
  }

  @Test
  void testExecuteWithIncorrectResponseBody() {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithResponsePayload(new byte[]{1, 2, 3, 4, 5});
    ExecutionException executionException = assertThrows(ExecutionException.class,
      () -> TEST_API_PROCEDURE.call(TEST_CORRECT_DTO)
        .initialize(() -> JSON_RPC_ID, jsonRpcRequestSender)
        .jsonb(JSONB)
        .execute()
        .get());
    assertThat(executionException.getCause(), instanceOf(JsonParsingException.class));
  }
}
