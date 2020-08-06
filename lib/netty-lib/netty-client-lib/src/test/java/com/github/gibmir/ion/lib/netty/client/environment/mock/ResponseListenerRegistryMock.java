package com.github.gibmir.ion.lib.netty.client.environment.mock;

import com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry.ResponseListenerRegistry;

import static org.mockito.Mockito.mock;

public class ResponseListenerRegistryMock {
  public static ResponseListenerRegistry newMock() {
    return mock(ResponseListenerRegistry.class);
  }
}
