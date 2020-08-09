package com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry;

import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.lib.netty.client.environment.mock.JsonValueMock;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.future.ResponseFuture;
import org.junit.jupiter.api.Test;

import javax.json.JsonValue;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_ID;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_REAL_JSONB;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TestException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SimpleResponseListenerRegistryTest {

  @Test
  void testCorrectRegister() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>());
    assertDoesNotThrow(() -> registry.register(responseFuture));

    assertTrue(idPerResponseListener.containsKey(TEST_ID));
    assertTrue(idPerResponseListener.containsValue(responseFuture));
  }

  @Test
  void testRegisterWithSameId() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);
    ResponseFuture firstResponseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>());
    ResponseFuture secondResponseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>());
    assertDoesNotThrow(() -> registry.register(firstResponseFuture));
    assertDoesNotThrow(() -> registry.register(secondResponseFuture));

    assertTrue(idPerResponseListener.containsKey(TEST_ID));
    assertTrue(idPerResponseListener.containsValue(firstResponseFuture));
    assertFalse(idPerResponseListener.containsValue(secondResponseFuture));
  }


  @Test
  void notifyListenerWithCorrectJsonValue() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>());
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);
    String correctResponse = "{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}";

    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(correctResponse, JsonValue.class), TEST_REAL_JSONB);

    assertTrue(responseFuture.getFuture().isDone());
    assertFalse(responseFuture.getFuture().isCompletedExceptionally());
  }

  /**
   * @implNote return type of {@link ResponseFuture} is broken.
   */
  @Test
  void notifyIncorrectListenerWithCorrectJsonValue() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, null, new CompletableFuture<>());
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);
    String correctResponse = "{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}";

    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(correctResponse, JsonValue.class), TEST_REAL_JSONB);

    assertTrue(responseFuture.getFuture().isDone());
    assertTrue(responseFuture.getFuture().isCompletedExceptionally());
    assertThrows(ExecutionException.class, () -> responseFuture.getFuture().get());
  }

  /**
   * If response future wasn't found - error log will be produced.
   *
   * @see SimpleResponseListenerRegistry
   */
  @Test
  void notifyListenerWithEmptyRegistryCorrectJsonValue() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);
    String correctResponse = "{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}";

    assertDoesNotThrow(
      () -> registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(correctResponse, JsonValue.class), TEST_REAL_JSONB));
  }

  @Test
  void notifyListenerWithJsonValueWithoutProtocol() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>());
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);
    String response = "{\"id\":\"test-id\",\"result\":1}";

    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(response, JsonValue.class), TEST_REAL_JSONB);

    assertTrue(responseFuture.getFuture().isDone());
    assertTrue(responseFuture.getFuture().isCompletedExceptionally());
    ExecutionException executionException = assertThrows(ExecutionException.class, responseFuture.getFuture()::get);
    Throwable cause = executionException.getCause();
    assertTrue(cause instanceof JsonRpcProcessingException);
    assertTrue(cause.getMessage().contains(Errors.INVALID_RPC.getError().getMessage()));
  }

  /**
   * There is no id to notify response listener.
   */
  @Test
  void notifyListenerWithJsonValueWithoutId() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>());
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);
    String response = "{\"jsonrpc\":\"2.0\",\"result\":1}";
    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(response, JsonValue.class), TEST_REAL_JSONB);

    assertFalse(responseFuture.getFuture().isDone());
  }

  @Test
  void notifyListenerWithJsonValueWithoutResult() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>());
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);
    String response = "{\"jsonrpc\":\"2.0\",\"id\":\"test-id\"}";
    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(response, JsonValue.class), TEST_REAL_JSONB);


    assertTrue(responseFuture.getFuture().isDone());
    assertTrue(responseFuture.getFuture().isCompletedExceptionally());
    ExecutionException executionException = assertThrows(ExecutionException.class, responseFuture.getFuture()::get);
    Throwable cause = executionException.getCause();
    assertTrue(cause instanceof JsonRpcProcessingException);
    assertTrue(cause.getMessage().contains(Errors.INVALID_RPC.getError().getMessage()));
  }

  @Test
  void notifyListenerWithJsonValueNotObjectOrArray() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>());
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);

    registry.notifyListenerWith(JsonValueMock.newMock(JsonValue.ValueType.STRING), TEST_REAL_JSONB);

    assertFalse(responseFuture.getFuture().isDone());
    assertFalse(responseFuture.getFuture().isCancelled());
    assertFalse(responseFuture.getFuture().isCompletedExceptionally());
  }

  @Test
  void notifyListenerWithNullJsonValue() {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>());
    idPerResponseListener.put(TEST_ID, responseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);

    registry.notifyListenerWith(null, TEST_REAL_JSONB);

    assertFalse(responseFuture.getFuture().isDone());
    assertFalse(responseFuture.getFuture().isCancelled());
    assertFalse(responseFuture.getFuture().isCompletedExceptionally());
  }

  @Test
  void notifyListenerWithJsonValueArray() throws ExecutionException, InterruptedException {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>());
    ResponseFuture secondResponseFuture = new ResponseFuture(TEST_ID + 2, int.class, new CompletableFuture<>());
    idPerResponseListener.put(TEST_ID, responseFuture);
    idPerResponseListener.put(TEST_ID + 2, secondResponseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);
    String arrayResponse = "[{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}," +
      "{\"jsonrpc\":\"2.0\",\"id\":\"test-id2\",\"result\":2}]";
    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(arrayResponse, JsonValue.class), TEST_REAL_JSONB);


    assertTrue(responseFuture.getFuture().isDone());
    assertTrue(secondResponseFuture.getFuture().isDone());

    assertEquals(1, responseFuture.getFuture().get());
    assertEquals(2, secondResponseFuture.getFuture().get());
  }

  @Test
  void notifyListenerWithJsonValueArrayFutureException() throws ExecutionException, InterruptedException {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>()
      .handle((o, throwable) -> {
        throw new TestException();
      }));


    ResponseFuture secondResponseFuture = new ResponseFuture(TEST_ID + 2, int.class, new CompletableFuture<>());
    idPerResponseListener.put(TEST_ID, responseFuture);
    idPerResponseListener.put(TEST_ID + 2, secondResponseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);
    String arrayResponse = "[{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}," +
      "{\"jsonrpc\":\"2.0\",\"id\":\"test-id2\",\"result\":2}]";
    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(arrayResponse, JsonValue.class), TEST_REAL_JSONB);


    assertTrue(responseFuture.getFuture().isDone());
    assertTrue(secondResponseFuture.getFuture().isDone());

    assertEquals(1, responseFuture.getFuture().get());
    assertEquals(2, secondResponseFuture.getFuture().get());
  }

  @Test
  void notifyListenerWithIncorrectJsonValueArray() throws ExecutionException, InterruptedException {
    HashMap<String, ResponseFuture> idPerResponseListener = new HashMap<>();
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>());
    ResponseFuture secondResponseFuture = new ResponseFuture(TEST_ID + 2, int.class, new CompletableFuture<>());
    idPerResponseListener.put(TEST_ID, responseFuture);
    idPerResponseListener.put(TEST_ID + 2, secondResponseFuture);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(idPerResponseListener);
    String arrayResponse = "[{\"jsonrpc\":\"2.0\",\"id\":\"test-id\",\"result\":1}," +
      "{\"jsonrpc\":\"2.0\",\"id\":\"test-id2\", \"error\": {\"code\": -32700, \"message\": \"Parse error\"}}]";
    registry.notifyListenerWith(TEST_REAL_JSONB.fromJson(arrayResponse, JsonValue.class), TEST_REAL_JSONB);


    assertTrue(responseFuture.getFuture().isDone());
    assertTrue(secondResponseFuture.getFuture().isDone());

    assertEquals(1, responseFuture.getFuture().get());
    ExecutionException executionException = assertThrows(ExecutionException.class,
      () -> secondResponseFuture.getFuture().get());

    Throwable cause = executionException.getCause();
    assertTrue(cause instanceof JsonRpcProcessingException);
    assertTrue(cause.getMessage().contains(TEST_ID + 2));
  }
}
