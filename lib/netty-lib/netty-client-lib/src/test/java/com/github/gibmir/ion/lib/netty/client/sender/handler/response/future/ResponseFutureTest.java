package com.github.gibmir.ion.lib.netty.client.sender.handler.response.future;

import com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TestException;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResponseFutureTest {

  @Test
  void testGetReturnType() {
    Class<Integer> expectedType = int.class;
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, expectedType, new CompletableFuture<>());

    assertEquals(expectedType, responseFuture.getReturnType());
  }

  @Test
  void testGetFuture() {
    CompletableFuture<Object> future = new CompletableFuture<>();
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, future);

    assertEquals(future, responseFuture.getFuture());
  }

  @Test
  void testCompleteExceptionally() {
    CompletableFuture<Object> future = new CompletableFuture<>();
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, future);
    responseFuture.completeExceptionally(new TestException());
    assertEquals(future, responseFuture.getFuture());
    assertTrue(responseFuture.getFuture().isCompletedExceptionally());
  }

  @Test
  void testGetId() {
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, new CompletableFuture<>());

    assertEquals(TEST_ID, responseFuture.getId());
  }
}
