package com.github.gibmir.ion.api.core.environment;

import com.github.gibmir.ion.api.core.request.Request.Initializer;
import com.github.gibmir.ion.api.core.request.factory.RequestFactory;
import com.github.gibmir.ion.api.core.request.callback.ResponseCallback;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class IonTestEnvironment {
  public static final TestDto TEST_CORRECT_DTO = new TestDto("correct", 1);
  public static final String JSON_RPC_ID = "id";
  public static final SuccessResponse TEST_CORRECT_SUCCESS_RESPONSE = new SuccessResponse(JSON_RPC_ID, TEST_CORRECT_DTO);
  public static final Jsonb JSONB = JsonbBuilder.create();
  public static final byte[] SUCCESS_RESPONSE_PAYLOAD = JSONB.toJson(TEST_CORRECT_SUCCESS_RESPONSE)
    .getBytes(StandardCharsets.UTF_8);
  public static final ErrorResponse TEST_ERROR_RESPONSE = ErrorResponse.fromThrowable(JSON_RPC_ID, new IllegalArgumentException());
  public static final byte[] ERROR_RESPONSE_PAYLOAD = JSONB.toJson(TEST_ERROR_RESPONSE).getBytes(StandardCharsets.UTF_8);
  public static final JsonRemoteProcedure1<TestDto, Initializer<TestDto>> TEST_API_PROCEDURE = RequestFactory.singleArg(TestApi.class, TestDto.class);

  public static class TestDto {
    private String string;
    private int anInt;

    public TestDto() {
    }

    public TestDto(String string, int anInt) {
      this.string = string;
      this.anInt = anInt;
    }

    public String getString() {
      return string;
    }

    public void setString(String string) {
      this.string = string;
    }

    public int getAnInt() {
      return anInt;
    }

    public void setAnInt(int anInt) {
      this.anInt = anInt;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TestDto testDto = (TestDto) o;
      return anInt == testDto.anInt &&
        Objects.equals(string, testDto.string);
    }

    @Override
    public int hashCode() {
      return Objects.hash(string, anInt);
    }

    @Override
    public String toString() {
      return "TestDto{" +
        "string='" + string + '\'' +
        ", anInt=" + anInt +
        '}';
    }
  }

  public static class SyncCallback<R> implements ResponseCallback<R> {
    private final Semaphore semaphore = new Semaphore(0);
    private final long timeout;
    private final TimeUnit timeUnit;
    private Throwable throwable;
    private R result;

    public SyncCallback(long timeout, TimeUnit timeUnit) {
      this.timeout = timeout;
      this.timeUnit = timeUnit;
    }

    @Override
    public void onComplete(R result, Throwable throwable) {
      try {
        this.result = result;
        this.throwable = throwable;
      } finally {
        semaphore.release();
      }
    }

    public void awaitResult() throws Throwable {
      if (!semaphore.tryAcquire(timeout, timeUnit)) {
        throw new TimeoutException();
      }
    }

    public Throwable getThrowable() {
      return throwable;
    }

    public R getResult() {
      return result;
    }
  }

  public static class TestException extends RuntimeException {

  }

  public interface TestApi extends JsonRemoteProcedure0<TestDto>, JsonRemoteProcedure1<TestDto, TestDto> {

  }
}
