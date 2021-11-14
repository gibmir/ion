package com.github.gibmir.ion.lib.netty.client.tcp.sender.handler.response.registry;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.SimpleResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.tcp.environment.mock.JsonValueMock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import javax.json.JsonValue;
import java.util.HashMap;

import static com.github.gibmir.ion.lib.netty.client.tcp.environment.TestEnvironment.TEST_ID;
import static com.github.gibmir.ion.lib.netty.client.tcp.environment.TestEnvironment.TEST_REAL_JSONB;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SimpleResponseListenerRegistryTest {
  public static Logger logger;

  @BeforeAll
  static void beforeAll() {
    logger = mock(Logger.class);
  }

  @Test
  void testCorrectRegister() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, mock(ResponseCallback.class));
    assertDoesNotThrow(() -> registry.register(responseFuture));

    assertTrue(idPerResponseListener.containsKey(TEST_ID));
    assertTrue(idPerResponseListener.containsValue(responseFuture));
  }

  @Test
  void testRegisterWithSameId() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    ResponseFuture firstResponseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, mock(ResponseCallback.class));
    ResponseFuture secondResponseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, mock(ResponseCallback.class));
    assertDoesNotThrow(() -> registry.register(firstResponseFuture));
    assertDoesNotThrow(() -> registry.register(secondResponseFuture));

    assertTrue(idPerResponseListener.containsKey(TEST_ID));
    assertTrue(idPerResponseListener.containsValue(firstResponseFuture));
    assertFalse(idPerResponseListener.containsValue(secondResponseFuture));
  }


  @Test
  @SuppressWarnings("unchecked")
  void notifyListenerWithCorrectJsonValue() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<Object> responseCallback = mock(ResponseCallback.class);
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, responseCallback);
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    int expectedResult = 1;
    String correctResponse = "{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":" + expectedResult + "}";

    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(correctResponse, JsonValue.class));

    verify(responseCallback, times(1)).onResponse(eq(expectedResult), any());
  }

  /**
   * @implNote return type of {@link ResponseFuture} is broken.
   */
  @Test
  void notifyIncorrectListenerWithCorrectJsonValue() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, null, TEST_REAL_JSONB, responseCallback);
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String correctResponse = "{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}";

    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(correctResponse, JsonValue.class));

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
      () -> registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(correctResponse, JsonValue.class)));
  }

  @Test
  void notifyListenerWithJsonValueWithoutProtocol() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, responseCallback);
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String response = "{\"id\":\"test-id\",\"result\":1}";

    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(response, JsonValue.class));
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

    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, responseCallback);
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String response = "{\"jsonrpc\":\"2.0\",\"result\":1}";
    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(response, JsonValue.class));

    verify(responseCallback, never()).onResponse(any(), any());
  }

  @Test
  void notifyListenerWithJsonValueWithoutResult() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);

    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, responseCallback);
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String response = "{\"jsonrpc\":\"2.0\",\"id\":\"test-id\"}";
    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(response, JsonValue.class));

    ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
    verify(responseCallback, times(1)).onResponse(any(), exceptionCaptor.capture());
    Throwable exception = exceptionCaptor.getValue();
    assertTrue(exception instanceof JsonRpcProcessingException);
    assertTrue(exception.getMessage().contains(Errors.INVALID_RPC.getError().getMessage()));
  }

  @Test
  void notifyListenerWithJsonValueNotObjectOrArray() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);

    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, responseCallback);
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);

    assertDoesNotThrow(() -> registry.notifyListenerWith(JsonValueMock.newMock(JsonValue.ValueType.STRING)));

    verify(responseCallback, never()).onResponse(any(), any());
  }

  @Test
  void notifyListenerWithNullJsonValue() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);

    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, responseCallback);
    idPerResponseListener.put(TEST_ID, responseFuture);
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
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, firstResponseCallback);
    ResponseFuture secondResponseFuture = new ResponseFuture(TEST_ID + 2, int.class, TEST_REAL_JSONB, secondResponseCallback);
    idPerResponseListener.put(TEST_ID, responseFuture);
    idPerResponseListener.put(TEST_ID + 2, secondResponseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String arrayResponse = "[{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}," +
      "{\"jsonrpc\":\"2.0\",\"id\":\"test-id2\",\"result\":2}]";
    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(arrayResponse, JsonValue.class));

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

    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, firstResponseCallback);

    ResponseCallback<Object> secondResponseCallback = mock(ResponseCallback.class);

    ResponseFuture secondResponseFuture = new ResponseFuture(TEST_ID + 2, int.class, TEST_REAL_JSONB,
      secondResponseCallback);
    idPerResponseListener.put(TEST_ID, responseFuture);
    idPerResponseListener.put(TEST_ID + 2, secondResponseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String arrayResponse = "[{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}," +
      "{\"jsonrpc\":\"2.0\",\"id\":\"test-id2\",\"result\":2}]";
    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(arrayResponse, JsonValue.class));


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
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, firstResponseCallback);
    ResponseCallback<?> secondResponseCallback = mock(ResponseCallback.class);
    ResponseFuture secondResponseFuture = new ResponseFuture(TEST_ID + 2, int.class, TEST_REAL_JSONB, secondResponseCallback);
    idPerResponseListener.put(TEST_ID, responseFuture);
    idPerResponseListener.put(TEST_ID + 2, secondResponseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener, logger);
    String arrayResponse = "[{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}," +
      "{\"jsonrpc\":\"2.0\",\"id\":\"test-id2\", \"error\": {\"code\": -32700, \"message\": \"Parse error\"}}]";
    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(arrayResponse, JsonValue.class));

    ArgumentCaptor<Integer> firstArgumentCaptor = ArgumentCaptor.forClass(int.class);
    verify(firstResponseCallback, times(1)).onResponse(firstArgumentCaptor.capture(), any());
    assertEquals(1, firstArgumentCaptor.getValue());

    ArgumentCaptor<Throwable> secondArgumentCaptor = ArgumentCaptor.forClass(Throwable.class);
    verify(secondResponseCallback, times(1)).onResponse(any(), secondArgumentCaptor.capture());
    Throwable throwable = secondArgumentCaptor.getValue();
    assertTrue(throwable instanceof JsonRpcProcessingException);
    assertTrue(throwable.getMessage().contains(TEST_ID + 2));
  }
}
