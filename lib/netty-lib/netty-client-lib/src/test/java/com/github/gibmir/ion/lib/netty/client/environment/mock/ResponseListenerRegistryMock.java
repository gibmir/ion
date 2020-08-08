package com.github.gibmir.ion.lib.netty.client.environment.mock;

import com.github.gibmir.ion.lib.netty.client.sender.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry.ResponseListenerRegistry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class ResponseListenerRegistryMock {
  public static ResponseListenerRegistry emptyMock() {
    return mock(ResponseListenerRegistry.class);
  }

  public static ResponseListenerRegistry newMock(Class<? extends RuntimeException> throwableClass) {
    ResponseListenerRegistry responseListenerRegistry = mock(ResponseListenerRegistry.class);
    doThrow(throwableClass).when(responseListenerRegistry).register(any(ResponseFuture.class));
    return responseListenerRegistry;
  }
}
