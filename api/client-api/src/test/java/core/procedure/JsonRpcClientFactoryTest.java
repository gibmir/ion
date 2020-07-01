package core.procedure;

import com.github.gibmir.ion.api.client.sender.JsonRpcRequestSender;
import core.environment.mock.JsonRpcChannelMock;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import core.environment.IonTestEnvironment;
import org.junit.jupiter.api.Test;

import javax.json.stream.JsonParsingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonRpcClientFactoryTest {

  @Test
  void testExecuteCorrectSuccessResponse() throws Throwable {

  }

  @Test
  void testExecuteCorrectErrorResponse() {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithResponsePayload(IonTestEnvironment.ERROR_RESPONSE_PAYLOAD);
    ExecutionException executionException = assertThrows(ExecutionException.class,
      () -> IonTestEnvironment.TEST_API_PROCEDURE
        .call(IonTestEnvironment.TEST_CORRECT_DTO)
        .initialize(() -> IonTestEnvironment.JSON_RPC_ID, jsonRpcRequestSender)
        .jsonb(IonTestEnvironment.JSONB)
        .execute()
        .get());
    Throwable cause = executionException.getCause();
    assertThat(cause, instanceOf(JsonRpcProcessingException.class));
    assertThat(cause.getMessage(), containsString(IonTestEnvironment.TEST_ERROR_RESPONSE.toString()));
  }

  @Test
  void testExecuteWithTimeout() {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithTimeout(5, TimeUnit.SECONDS);
    assertThrows(TimeoutException.class, () -> IonTestEnvironment.TEST_API_PROCEDURE
      .call(IonTestEnvironment.TEST_CORRECT_DTO)
      .initialize(() -> IonTestEnvironment.JSON_RPC_ID, jsonRpcRequestSender)
      .jsonb(IonTestEnvironment.JSONB)
      .execute()
      .get(1, TimeUnit.MILLISECONDS));
  }

  @Test
  void testExecuteWithIncorrectResponseBody() {
    JsonRpcRequestSender jsonRpcRequestSender = JsonRpcChannelMock.newMockWithResponsePayload(new byte[]{1, 2, 3, 4, 5});
    ExecutionException executionException = assertThrows(ExecutionException.class,
      () -> IonTestEnvironment.TEST_API_PROCEDURE.call(IonTestEnvironment.TEST_CORRECT_DTO)
        .initialize(() -> IonTestEnvironment.JSON_RPC_ID, jsonRpcRequestSender)
        .jsonb(IonTestEnvironment.JSONB)
        .execute()
        .get());
    assertThat(executionException.getCause(), instanceOf(JsonParsingException.class));
  }
}
