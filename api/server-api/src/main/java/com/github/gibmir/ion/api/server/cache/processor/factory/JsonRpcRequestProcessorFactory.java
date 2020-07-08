package com.github.gibmir.ion.api.server.cache.processor.factory;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.dto.request.transfer.NotificationDto;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.JsonRpcError;
import com.github.gibmir.ion.api.dto.response.transfer.notification.NotificationResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

public class JsonRpcRequestProcessorFactory {

  public static <T> JsonRpcRequestProcessor createProcessor(Class<? extends T> serviceInterface,
                                                            T service) {
    final NamedMethodHandle[] methodHandles;
    try {
      methodHandles = resolveMethodHandlesFor(serviceInterface);
    } catch (NoSuchMethodException | IllegalAccessException e) {
      String message = "Exception occurred while preparing request processor for [" + serviceInterface + ']';
      throw new IllegalArgumentException(message, e);
    }
    return new MethodHandleJsonRpcRequestProcessor<>(methodHandles, service);
  }

  public static NamedMethodHandle[] resolveMethodHandlesFor(Class<?> serviceInterface) throws NoSuchMethodException, IllegalAccessException {
    final MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();

    final Method[] methods = serviceInterface.getMethods();

    NamedMethodHandle[] methodHandles = new NamedMethodHandle[methods.length];
    for (int i = 0; i < methods.length; i++) {
      final Method method = methods[i];
      final String name = method.getName();
      final MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
      final MethodHandle methodHandle = publicLookup.findVirtual(serviceInterface, name, methodType);
      methodHandles[i] = new NamedMethodHandle(methodHandle.asSpreader(Object[].class, method.getParameterCount()),
        name);
    }
    return methodHandles;
  }

  private static class NamedMethodHandle {
    private final MethodHandle methodHandle;
    private final String methodName;

    private NamedMethodHandle(MethodHandle methodHandle, String methodName) {
      this.methodHandle = methodHandle;
      this.methodName = methodName;
    }

    public Object invokeWithArguments(Object... args) throws Throwable {
      return methodHandle.invokeWithArguments(args);
    }
  }

  private static class MethodHandleJsonRpcRequestProcessor<S> implements JsonRpcRequestProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(MethodHandleJsonRpcRequestProcessor.class);
    private final NamedMethodHandle[] namedMethodHandles;
    private final S service;

    public MethodHandleJsonRpcRequestProcessor(NamedMethodHandle[] namedMethodHandles, S service) {
      this.namedMethodHandles = namedMethodHandles;
      this.service = service;
    }

    @Override
    public JsonRpcResponse process(RequestDto positionalRequest) {
      try {
        return invokeMethod(positionalRequest);
      } catch (Throwable throwable) {
        final String message = String.format("Exception occurred while invoking method [%s]. Message is:%s",
          positionalRequest.getProcedureName(), throwable.getMessage());
        return ErrorResponse.withJsonRpcError(positionalRequest.getId(), new JsonRpcError(-32000, message));
      }
    }

    private JsonRpcResponse invokeMethod(RequestDto jsonRpcRequest) throws Throwable {
      for (final NamedMethodHandle methodHandle : namedMethodHandles) {
        if (methodHandle.methodName.equals(jsonRpcRequest.getProcedureName())) {
          final Object result = methodHandle.invokeWithArguments(service, jsonRpcRequest.getArgs());
          return SuccessResponse.createWithStringId(jsonRpcRequest.getId(), result);
        }
      }
      return ErrorResponse.withJsonRpcError(jsonRpcRequest.getId(), new JsonRpcError(-32000,
        "Method " + jsonRpcRequest.getProcedureName() + " is unsupported"));

    }

    @Override
    public JsonRpcResponse process(NotificationDto notificationRequest) {
      try {
        return invokeMethod(notificationRequest);
      } catch (Throwable throwable) {
        String procedureName = notificationRequest.getProcedureName();
        LOGGER.error("Exception occurred while invoking method [{}]. Message is:{}",
          procedureName, throwable.getMessage());
        return NotificationResponse.INSTANCE;
      }
    }

    private JsonRpcResponse invokeMethod(NotificationDto notificationRequest) throws Throwable {
      for (final NamedMethodHandle methodHandle : namedMethodHandles) {
        if (methodHandle.methodName.equals(notificationRequest.getProcedureName())) {
          final Object result = methodHandle.invokeWithArguments(service, notificationRequest.getArgs());
          if (result != null) {
            LOGGER.warn("Result isn't null for notification request:[{}]", notificationRequest);
          }
          return NotificationResponse.INSTANCE;
        }
      }
      LOGGER.error("Method [{}] is unsupported. Exception: ", notificationRequest.getProcedureName(),
        new UnsupportedOperationException());
      return NotificationResponse.INSTANCE;
    }
  }
}
