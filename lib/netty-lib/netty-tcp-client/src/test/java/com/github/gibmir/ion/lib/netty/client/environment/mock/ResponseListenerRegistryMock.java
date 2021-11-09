package com.github.gibmir.ion.lib.netty.client.environment.mock;

import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.SimpleResponseListenerRegistry;
import org.slf4j.Logger;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class ResponseListenerRegistryMock {

  private ResponseListenerRegistryMock() {
  }

  public static ResponseListenerRegistry emptyMock() {
    return mock(ResponseListenerRegistry.class);
  }

  /**
   * @param map    response cache
   * @param logger logger
   * @return listener registry
   */
  public static ResponseListenerRegistry newStub(Map<String, ResponseFuture> map, Logger logger) {
    return new SimpleResponseListenerRegistry(map, logger);
  }

  public static ResponseListenerRegistry newMock(Class<? extends RuntimeException> throwableClass) {
    ResponseListenerRegistry responseListenerRegistry = mock(ResponseListenerRegistry.class);
    doThrow(throwableClass).when(responseListenerRegistry).register(any(ResponseFuture.class));
    return responseListenerRegistry;
  }
}
