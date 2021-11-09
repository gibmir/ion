package com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.error.JsonRpcError;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.common.environment.TestEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SimpleResponseListenerRegistryTest {
  public static final Jsonb JSONB = JsonbBuilder.newBuilder().build();
  public Logger logger;

  @BeforeEach
  void beforeEach() {
    logger = mock(Logger.class);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testRegisterSuccessfully() {
    Map<String, ResponseFuture> registryMap = mock(Map.class);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(registryMap, logger);
    String testId = "test-id";
    ResponseFuture responseFuture = new ResponseFuture(testId, String.class, mock(Jsonb.class),
      mock(ResponseCallback.class));
    registry.register(responseFuture);
    verify(registryMap).compute(eq(testId), any());
  }

  @Test
  @SuppressWarnings("unchecked")
  void testRegisterSameIdTwice() {
    Map<String, ResponseFuture> registryMap = mock(Map.class);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(registryMap, logger);
    String testId = "test-id";
    ResponseFuture responseFuture = new ResponseFuture(testId, String.class, mock(Jsonb.class),
      mock(ResponseCallback.class));
    ResponseFuture otherResponseFuture = new ResponseFuture(testId, String.class, mock(Jsonb.class),
      mock(ResponseCallback.class));
    registry.register(responseFuture);
    registry.register(otherResponseFuture);
    verify(registryMap, times(2)).compute(eq(testId), any());
  }

  @Test
  @SuppressWarnings("unchecked")
  void testComputeRegistrationTwiceRegistration() {
    String testId = "test-id";
    ResponseCallback<Object> responseCallback = mock(ResponseCallback.class);
    ResponseFuture currentlyRegisteredFuture = new ResponseFuture(testId, String.class, mock(Jsonb.class),
      responseCallback);
    ResponseFuture alreadyRegistered = new ResponseFuture(testId, String.class, mock(Jsonb.class),
      mock(ResponseCallback.class));
    ResponseFuture registered = SimpleResponseListenerRegistry.computeRegistration(currentlyRegisteredFuture, testId,
      alreadyRegistered);
    assertThat(registered, is(alreadyRegistered));
    verify(responseCallback).onResponse(isNull(), isNotNull());
  }

  @Test
  @SuppressWarnings("unchecked")
  void testComputeRegistration() {
    String testId = "test-id";
    ResponseCallback<Object> responseCallback = mock(ResponseCallback.class);
    ResponseFuture currentlyRegisteredFuture = new ResponseFuture(testId, String.class, mock(Jsonb.class),
      responseCallback);
    ResponseFuture registered = SimpleResponseListenerRegistry.computeRegistration(currentlyRegisteredFuture, testId,
      null);
    assertThat(registered, is(currentlyRegisteredFuture));
  }

  @Test
  void notifyWithJsonValueException() {
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(Collections.emptyMap(), logger);
    JsonValue jsonValue = mock(JsonValue.class);
    doThrow(TestException.class).when(jsonValue).getValueType();
    registry.notifyListenerWith(jsonValue);

    verify(jsonValue).getValueType();
    ArgumentCaptor<JsonRpcError> errorCaptor = ArgumentCaptor.forClass(JsonRpcError.class);
    verify(logger).error(anyString(), errorCaptor.capture());

    assertThat(errorCaptor.getValue().getMessage(), containsString(Errors.INVALID_RPC.getError().getMessage()));
    assertThat(errorCaptor.getValue().getCode(), is(Errors.INVALID_RPC.getError().getCode()));
  }

  @Test
  void notifyWithIncorrectJsonValue() {
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(Collections.emptyMap(), logger);
    JsonValue jsonValue = mock(JsonValue.class);
    doAnswer(__ -> JsonValue.ValueType.NULL).when(jsonValue).getValueType();
    registry.notifyListenerWith(jsonValue);

    verify(jsonValue).getValueType();
    ArgumentCaptor<JsonRpcError> errorCaptor = ArgumentCaptor.forClass(JsonRpcError.class);
    verify(logger).error(anyString(), errorCaptor.capture());

    assertThat(errorCaptor.getValue().getMessage(), containsString(Errors.INVALID_RPC.getError().getMessage()));
    assertThat(errorCaptor.getValue().getCode(), is(Errors.INVALID_RPC.getError().getCode()));
  }

  @Test
  void testCorrectRegister() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    ResponseFuture responseFuture = new ResponseFuture(TestEnvironment.TEST_ID, int.class, JSONB, mock(ResponseCallback.class));
    assertDoesNotThrow(() -> registry.register(responseFuture));

    assertTrue(idPerResponseListener.containsKey(TestEnvironment.TEST_ID));
    assertTrue(idPerResponseListener.containsValue(responseFuture));
  }

  @Test
  void testRegisterWithSameId() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    ResponseFuture firstResponseFuture = new ResponseFuture(TestEnvironment.TEST_ID, int.class, JSONB, mock(ResponseCallback.class));
    ResponseFuture secondResponseFuture = new ResponseFuture(TestEnvironment.TEST_ID, int.class, JSONB, mock(ResponseCallback.class));
    assertDoesNotThrow(() -> registry.register(firstResponseFuture));
    assertDoesNotThrow(() -> registry.register(secondResponseFuture));

    assertTrue(idPerResponseListener.containsKey(TestEnvironment.TEST_ID));
    assertTrue(idPerResponseListener.containsValue(firstResponseFuture));
    assertFalse(idPerResponseListener.containsValue(secondResponseFuture));
  }


  @Test
  @SuppressWarnings("unchecked")
  void notifyListenerWithCorrectJsonValue() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<Object> responseCallback = mock(ResponseCallback.class);
    ResponseFuture responseFuture = new ResponseFuture(TestEnvironment.TEST_ID, int.class, JSONB, responseCallback);
    idPerResponseListener.put(TestEnvironment.TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    int expectedResult = 1;
    String correctResponse = "{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":" + expectedResult + "}";

    registry.notifyListenerWith(JSONB.fromJson(correctResponse, JsonValue.class));

    verify(responseCallback, times(1)).onResponse(eq(expectedResult), any());
  }

  /**
   * @implNote return type of {@link ResponseFuture} is broken.
   */
  @Test
  void notifyIncorrectListenerWithCorrectJsonValue() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);
    ResponseFuture responseFuture = new ResponseFuture(TestEnvironment.TEST_ID, null, JSONB, responseCallback);
    idPerResponseListener.put(TestEnvironment.TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String correctResponse = "{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}";

    registry.notifyListenerWith(JSONB.fromJson(correctResponse, JsonValue.class));

    verify(responseCallback, times(1)).onResponse(any(), any());
//    assertThrows(ExecutionException.class, () -> responseFuture.getFuture().get());
  }

  /**
   * If response future wasn't found - error log will be produced.
   *
   * @see SimpleResponseListenerRegistry
   */
  @Test
  void notifyListenerWithEmptyRegistryCorrectJsonValue() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String correctResponse = "{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}";

    assertDoesNotThrow(
      () -> registry.notifyListenerWith(JSONB.fromJson(correctResponse, JsonValue.class)));
  }

  @Test
  void notifyListenerWithCorrectJsonValueWithNumericId() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String correctResponse = "{\"jsonrpc\":\"2.0\",\"id\":15,\"result\":1}";

    assertDoesNotThrow(
      () -> registry.notifyListenerWith(JSONB.fromJson(correctResponse, JsonValue.class)));
  }

  @Test
  void notifyListenerWithJsonValueWithoutProtocol() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);
    ResponseFuture responseFuture = new ResponseFuture(TestEnvironment.TEST_ID, int.class, JSONB, responseCallback);
    idPerResponseListener.put(TestEnvironment.TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String response = "{\"id\":\"test-id\",\"result\":1}";

    registry.notifyListenerWith(JSONB.fromJson(response, JsonValue.class));
    ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
    verify(responseCallback).onResponse(any(), exceptionCaptor.capture());
    Throwable exception = exceptionCaptor.getValue();
    assertTrue(exception instanceof JsonRpcProcessingException);
    assertTrue(exception.getMessage().contains(Errors.INVALID_RPC.getError().getMessage()));
  }

  /**
   * There is no id to notify response listener.
   */
  @Test
  void notifyListenerWithJsonValueWithoutId() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);

    ResponseFuture responseFuture = new ResponseFuture(TestEnvironment.TEST_ID, int.class, JSONB, responseCallback);
    idPerResponseListener.put(TestEnvironment.TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String response = "{\"jsonrpc\":\"2.0\",\"result\":1}";
    registry.notifyListenerWith(JSONB.fromJson(response, JsonValue.class));

    verify(responseCallback, never()).onResponse(any(), any());
  }

  @Test
  void notifyListenerWithJsonValueWithoutResult() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);

    ResponseFuture responseFuture = new ResponseFuture(TestEnvironment.TEST_ID, int.class, JSONB, responseCallback);
    idPerResponseListener.put(TestEnvironment.TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String response = "{\"jsonrpc\":\"2.0\",\"id\":\"test-id\"}";
    registry.notifyListenerWith(JSONB.fromJson(response, JsonValue.class));

    ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
    verify(responseCallback, times(1)).onResponse(any(), exceptionCaptor.capture());
    Throwable exception = exceptionCaptor.getValue();
    assertTrue(exception instanceof JsonRpcProcessingException);
    assertTrue(exception.getMessage().contains(Errors.INVALID_RPC.getError().getMessage()));
  }

  @Test
  void notifyListenerWithNullJsonValue() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);

    ResponseFuture responseFuture = new ResponseFuture(TestEnvironment.TEST_ID, int.class, JSONB, responseCallback);
    idPerResponseListener.put(TestEnvironment.TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);

    assertDoesNotThrow(() -> registry.notifyListenerWith(null));

    verify(responseCallback, never()).onResponse(any(), any());
  }

  @Test
  @SuppressWarnings("unchecked")
  void notifyListenerWithJsonValueArray() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<Object> firstResponseCallback = mock(ResponseCallback.class);
    ResponseCallback<Object> secondResponseCallback = mock(ResponseCallback.class);
    ResponseFuture responseFuture = new ResponseFuture(TestEnvironment.TEST_ID, int.class, JSONB, firstResponseCallback);
    ResponseFuture secondResponseFuture = new ResponseFuture(TestEnvironment.TEST_ID + 2, int.class, JSONB, secondResponseCallback);
    idPerResponseListener.put(TestEnvironment.TEST_ID, responseFuture);
    idPerResponseListener.put(TestEnvironment.TEST_ID + 2, secondResponseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String arrayResponse = "[{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}," +
      "{\"jsonrpc\":\"2.0\",\"id\":\"test-id2\",\"result\":2}]";
    registry.notifyListenerWith(JSONB.fromJson(arrayResponse, JsonValue.class));

    ArgumentCaptor<Integer> firstArgumentCaptor = ArgumentCaptor.forClass(int.class);
    verify(firstResponseCallback, times(1)).onResponse(firstArgumentCaptor.capture(), any());
    assertEquals(1, firstArgumentCaptor.getValue());
    ArgumentCaptor<Integer> secondArgumentCaptor = ArgumentCaptor.forClass(int.class);
    verify(secondResponseCallback, times(1)).onResponse(secondArgumentCaptor.capture(), any());
    assertEquals(2, secondArgumentCaptor.getValue());
  }

  @Test
  @SuppressWarnings("unchecked")
  void notifyListenerWithJsonValueArrayFutureException() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<Object> firstResponseCallback = mock(ResponseCallback.class);

    ResponseFuture responseFuture = new ResponseFuture(TestEnvironment.TEST_ID, int.class, JSONB, firstResponseCallback);

    ResponseCallback<Object> secondResponseCallback = mock(ResponseCallback.class);

    ResponseFuture secondResponseFuture = new ResponseFuture(TestEnvironment.TEST_ID + 2, int.class, JSONB,
      secondResponseCallback);
    idPerResponseListener.put(TestEnvironment.TEST_ID, responseFuture);
    idPerResponseListener.put(TestEnvironment.TEST_ID + 2, secondResponseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String arrayResponse = "[{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}," +
      "{\"jsonrpc\":\"2.0\",\"id\":\"test-id2\",\"result\":2}]";
    registry.notifyListenerWith(JSONB.fromJson(arrayResponse, JsonValue.class));


    ArgumentCaptor<Integer> firstArgumentCaptor = ArgumentCaptor.forClass(int.class);
    verify(firstResponseCallback, times(1)).onResponse(firstArgumentCaptor.capture(), any());
    assertEquals(1, firstArgumentCaptor.getValue());
    ArgumentCaptor<Integer> secondArgumentCaptor = ArgumentCaptor.forClass(int.class);
    verify(secondResponseCallback, times(1)).onResponse(secondArgumentCaptor.capture(), any());
    assertEquals(2, secondArgumentCaptor.getValue());
  }

  @Test
  @SuppressWarnings("unchecked")
  void notifyListenerWithIncorrectJsonValueArray() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<Object> firstResponseCallback = mock(ResponseCallback.class);
    ResponseFuture responseFuture = new ResponseFuture(TestEnvironment.TEST_ID, int.class, JSONB, firstResponseCallback);
    ResponseCallback<?> secondResponseCallback = mock(ResponseCallback.class);
    ResponseFuture secondResponseFuture = new ResponseFuture(TestEnvironment.TEST_ID + 2, int.class, JSONB, secondResponseCallback);
    idPerResponseListener.put(TestEnvironment.TEST_ID, responseFuture);
    idPerResponseListener.put(TestEnvironment.TEST_ID + 2, secondResponseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String arrayResponse = "[{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}," +
      "{\"jsonrpc\":\"2.0\",\"id\":\"test-id2\", \"error\": {\"code\": -32700, \"message\": \"Parse error\"}}]";
    registry.notifyListenerWith(JSONB.fromJson(arrayResponse, JsonValue.class));

    ArgumentCaptor<Integer> firstArgumentCaptor = ArgumentCaptor.forClass(int.class);
    verify(firstResponseCallback, times(1)).onResponse(firstArgumentCaptor.capture(), any());
    assertEquals(1, firstArgumentCaptor.getValue());

    ArgumentCaptor<Throwable> secondArgumentCaptor = ArgumentCaptor.forClass(Throwable.class);
    verify(secondResponseCallback, times(1)).onResponse(any(), secondArgumentCaptor.capture());
    Throwable throwable = secondArgumentCaptor.getValue();
    assertTrue(throwable instanceof JsonRpcProcessingException);
    assertTrue(throwable.getMessage().contains(TestEnvironment.TEST_ID + 2));
  }

  public static class TestException extends RuntimeException {

  }
}
