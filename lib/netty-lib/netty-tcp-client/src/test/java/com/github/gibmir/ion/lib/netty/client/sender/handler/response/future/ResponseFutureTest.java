package com.github.gibmir.ion.lib.netty.client.sender.handler.response.future;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TestException;
import org.junit.jupiter.api.Test;

import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_ID;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_REAL_JSONB;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ResponseFutureTest {

  @Test
  void testGetReturnType() {
    Class<Integer> expectedType = int.class;
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, expectedType, TEST_REAL_JSONB,
      mock(ResponseCallback.class));

    assertEquals(expectedType, responseFuture.getReturnType());
  }

  @Test
  void testCompleteExceptionally() {
    ResponseCallback<?> responseCallbackMock = mock(ResponseCallback.class);
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, responseCallbackMock);
    TestException expectedException = new TestException();
    responseFuture.completeExceptionally(expectedException);
    verify(responseCallbackMock, times(1)).onResponse(any(), eq(expectedException));
  }

  @Test
  void testGetId() {
    ResponseFuture responseFuture = new ResponseFuture(TEST_ID, int.class, TEST_REAL_JSONB, mock(ResponseCallback.class));

    assertEquals(TEST_ID, responseFuture.getId());
  }
}
