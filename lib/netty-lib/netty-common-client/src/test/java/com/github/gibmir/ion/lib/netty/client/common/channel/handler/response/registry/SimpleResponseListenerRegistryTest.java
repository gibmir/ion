package com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.json.bind.Jsonb;
import java.util.Map;
import java.util.function.BiFunction;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class SimpleResponseListenerRegistryTest {

  @Test
  @SuppressWarnings("unchecked")
  void testRegisterSuccessfully() {
    Map<String, ResponseFuture> registryMap = mock(Map.class);
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(registryMap);
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
    ResponseListenerRegistry registry = new SimpleResponseListenerRegistry(registryMap);
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
}
