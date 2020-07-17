package com.github.gibmir.ion.api.server.cache.processor.factory;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.request.transfer.NotificationDto;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.error.JsonRpcError;
import com.github.gibmir.ion.api.dto.response.transfer.notification.NotificationResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.function.Consumer;

public class JsonRpcRequestProcessorFactory {
  //todo type-safe generic type
  public static <T> JsonRpcRequestProcessor createProcessor(Class<? extends T> serviceInterface,
                                                            T service) {
    try {
      final NamedMethodHandle[] methodHandles = resolveMethodHandlesFor(serviceInterface);
      NamedMethodHandle caller = findCaller(methodHandles);
      return new MethodHandleJsonRpcRequestProcessor<>(caller, service);
    } catch (Exception e) {
      String message = "Exception occurred while preparing request processor for [" + serviceInterface + ']';
      throw new IllegalArgumentException(message, e);
    }
  }

  public static NamedMethodHandle findCaller(NamedMethodHandle[] namedMethodHandles) {
    for (NamedMethodHandle namedMethodHandle : namedMethodHandles) {
      //todo procedure implementation check
      if (namedMethodHandle.methodName.equals("call")) {
        return namedMethodHandle;
      }
    }
    throw new IllegalArgumentException("There is no caller in " + Arrays.toString(namedMethodHandles));
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
        name, method.getParameterTypes());
    }
    return methodHandles;
  }

  public static class NamedMethodHandle {
    private final MethodHandle methodHandle;
    private final String methodName;
    private final Class<?>[] argumentTypes;

    public NamedMethodHandle(MethodHandle methodHandle, String methodName, Class<?>... argumentTypes) {
      this.methodHandle = methodHandle;
      this.methodName = methodName;
      this.argumentTypes = argumentTypes;
    }

    public Object invokeWithArguments(Object... args) throws Throwable {
      return methodHandle.invokeWithArguments(args);
    }
  }

  private static class MethodHandleJsonRpcRequestProcessor<S> implements JsonRpcRequestProcessor {
    public static final Logger LOGGER = LoggerFactory.getLogger(MethodHandleJsonRpcRequestProcessor.class);
    private final NamedMethodHandle namedMethodHandle;
    private final S service;

    public MethodHandleJsonRpcRequestProcessor(NamedMethodHandle namedMethodHandle, S service) {
      this.namedMethodHandle = namedMethodHandle;
      this.service = service;
    }

    @Override
    public void process(String id, String procedureName, JsonObject jsonObject, Jsonb jsonb,
                        Consumer<JsonRpcResponse> responseConsumer) {
      JsonValue paramsValue = jsonObject.get(SerializationProperties.PARAMS_KEY);
      int length = namedMethodHandle.argumentTypes.length;
      switch (paramsValue.getValueType()) {
        case ARRAY:
          Object[] arguments = new Object[length];
          JsonArray jsonParamsArray = paramsValue.asJsonArray();
          for (int i = 0; i < length; i++) {
            arguments[i] = jsonb.fromJson(jsonParamsArray.get(i).toString(), namedMethodHandle.argumentTypes[i]);
          }
          responseConsumer.accept(process(RequestDto.positional(id, procedureName, arguments)));
          return;
        case OBJECT:
          //todo named params
        default:
          JsonRpcError jsonRpcError = Errors.INVALID_METHOD_PARAMETERS.getError()
            .appendMessage("Named parameters is unsupported");
          responseConsumer.accept(ErrorResponse.fromJsonRpcError(id, jsonRpcError));
      }
    }

    public JsonRpcResponse process(RequestDto positionalRequest) {
      try {
        return invokeMethod(positionalRequest);
      } catch (Throwable throwable) {
        final String message = String.format("Exception [%s] occurred while invoking method [%s]. Message is:%s",
          throwable, positionalRequest.getProcedureName(), throwable.getMessage());
        return ErrorResponse.fromJsonRpcError(positionalRequest.getId(), new JsonRpcError(-32000, message));
      }
    }

    private JsonRpcResponse invokeMethod(RequestDto jsonRpcRequest) throws Throwable {
      final Object result = namedMethodHandle.invokeWithArguments(service, jsonRpcRequest.getArgs());
      return SuccessResponse.createWithStringId(jsonRpcRequest.getId(), result);
    }


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
      final Object result = namedMethodHandle.invokeWithArguments(service, notificationRequest.getArgs());
      if (result != null) {
        LOGGER.warn("Result isn't null for notification request:[{}]", notificationRequest);
      }
      return NotificationResponse.INSTANCE;
    }
  }
}
