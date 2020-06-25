package com.github.gibmir.ion.api.core.environment.mock;

import com.github.gibmir.ion.api.client.sender.JsonRpcRequestSender;
import com.github.gibmir.ion.api.client.callback.JsonRpcResponseCallback;

import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class JsonRpcChannelMock {

  public static JsonRpcRequestSender newMockWithResponsePayload(byte[] responsePayload) throws Throwable {
    JsonRpcRequestSender jsonRpcRequestSender = mock(JsonRpcRequestSender.class);
    doAnswer(invocation -> {
      JsonRpcResponseCallback argument = invocation.getArgument(1, JsonRpcResponseCallback.class);
      argument.onComplete(responsePayload, null);
      return null;
    }).when(jsonRpcRequestSender).sendAsync(any(), any(JsonRpcResponseCallback.class));
    return jsonRpcRequestSender;
  }

  public static JsonRpcRequestSender newMockWithException(Throwable exception) throws Throwable {
    JsonRpcRequestSender jsonRpcRequestSender = mock(JsonRpcRequestSender.class);
    doAnswer(invocation -> {
      JsonRpcResponseCallback argument = invocation.getArgument(1, JsonRpcResponseCallback.class);
      argument.onComplete(null, exception);
      return null;
    }).when(jsonRpcRequestSender).sendAsync(any(), any(JsonRpcResponseCallback.class));
    return jsonRpcRequestSender;
  }

  public static JsonRpcRequestSender newMockWithTimeout(long timeout, TimeUnit timeUnit) throws Throwable {
    JsonRpcRequestSender jsonRpcRequestSender = mock(JsonRpcRequestSender.class);
    doAnswer(invocation -> {
      timeUnit.sleep(timeout);
      return null;
    }).when(jsonRpcRequestSender).sendAsync(any(), any(JsonRpcResponseCallback.class));
    return jsonRpcRequestSender;
  }
}
