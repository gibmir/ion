package com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.json.bind.Jsonb;
import java.lang.reflect.Type;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ResponseFutureTest {

  @Test
  void smoke() {
    String expectedId = "test-id";
    Class<String> expectedReturnType = String.class;
    Jsonb expectedJsonb = mock(Jsonb.class);
    ResponseFuture responseFuture = new ResponseFuture(expectedId, expectedReturnType, expectedJsonb,
      mock(ResponseCallback.class));
    assertThat(responseFuture.getId(), is(expectedId));
    assertThat(responseFuture.getResponseJsonb(), is(expectedJsonb));
    assertThat(responseFuture.getReturnType(), is(expectedReturnType));
  }

  @Test
  void testCompleteExceptionally() {
    String expectedId = "test-id";
    Class<String> expectedReturnType = String.class;
    Jsonb jsonb = mock(Jsonb.class);
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);
    ResponseFuture responseFuture = new ResponseFuture(expectedId, expectedReturnType, jsonb, responseCallback);
    RuntimeException testException = new TestException();
    responseFuture.completeExceptionally(testException);
    verify(responseCallback, times(1)).onResponse(null, testException);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCompleteSuccessfully() {
    String testResponseJson = "test-response-json";
    Object testDeserializedObject = "expected-json";
    Type testReturnType = String.class;
    Jsonb testJsonb = mock(Jsonb.class);
    doAnswer(__ -> testDeserializedObject).when(testJsonb).fromJson(testResponseJson, testReturnType);
    ResponseCallback<Object> testResponseCallback = mock(ResponseCallback.class);
    ResponseFuture responseFuture = new ResponseFuture("test-id", testReturnType, testJsonb, testResponseCallback);
    responseFuture.complete(testResponseJson);
    // verify jsonb inner call
    verify(testJsonb).fromJson(eq(testResponseJson), eq(testReturnType));
    // verify response in callback equals result of jsonb inner call
    verify(testResponseCallback).onResponse(eq(testDeserializedObject), isNull());
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCompleteError() {
    String testResponseJson = "test-response-json";
    Object testDeserializedObject = "expected-json";
    Type testReturnType = String.class;
    Jsonb testJsonb = mock(Jsonb.class);
    doAnswer(__ -> testDeserializedObject).when(testJsonb).fromJson(testResponseJson, testReturnType);
    ResponseCallback<Object> testResponseCallback = mock(ResponseCallback.class);
    String testId = "test-id";
    ResponseFuture responseFuture = new ResponseFuture(testId, testReturnType, testJsonb, testResponseCallback);
    responseFuture.completeError(ErrorResponse.fromJsonRpcError(testId, Errors.APPLICATION_ERROR.getError()));
    // verify response has exception type
    ArgumentCaptor<JsonRpcProcessingException> captor = ArgumentCaptor.forClass(JsonRpcProcessingException.class);
    verify(testResponseCallback).onResponse(isNull(), captor.capture());
    assertThat(captor.getValue(), not(nullValue()));
  }

  private static class TestException extends RuntimeException {

  }
}
