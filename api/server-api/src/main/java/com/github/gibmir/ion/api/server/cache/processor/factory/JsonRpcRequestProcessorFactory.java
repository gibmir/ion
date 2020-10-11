package com.github.gibmir.ion.api.server.cache.processor.factory;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.properties.SerializationProperties;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.error.JsonRpcError;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import com.github.gibmir.ion.api.server.cache.processor.JsonRpcRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;
import java.util.function.Consumer;

public class JsonRpcRequestProcessorFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonRpcRequestProcessorFactory.class);
  public static final String CALL_METHOD_NAME = "call";

  public static <R, P extends JsonRemoteProcedure0<R>> JsonRpcRequestProcessor createProcessor0(Class<P> procedure,
                                                                                                P service) {
    try {
      JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature0(procedure);
      return createProcessor(procedure, service, jsonRemoteProcedureSignature);
    } catch (Exception e) {
      throw new IllegalArgumentException("Exception occurred while preparing request processor for [" + procedure + ']',
        e);
    }
  }

  public static <T, R, P extends JsonRemoteProcedure1<T, R>> JsonRpcRequestProcessor createProcessor1(
    Class<P> procedure, P service) {
    try {
      JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature1(procedure);
      return createProcessor(procedure, service, jsonRemoteProcedureSignature);
    } catch (Exception e) {
      throw new IllegalArgumentException("Exception occurred while preparing request processor for [" + procedure + ']',
        e);
    }
  }

  public static <T1, T2, R, P extends JsonRemoteProcedure2<T1, T2, R>> JsonRpcRequestProcessor createProcessor2(
    Class<P> procedure, P service) {
    try {
      JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature2(procedure);
      return createProcessor(procedure, service, jsonRemoteProcedureSignature);
    } catch (Exception e) {
      throw new IllegalArgumentException("Exception occurred while preparing request processor for [" + procedure + ']',
        e);
    }
  }

  public static <T1, T2, T3, R, P extends JsonRemoteProcedure3<T1, T2, T3, R>> JsonRpcRequestProcessor createProcessor3(
    Class<P> procedure, P service) {
    try {
      JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature3(procedure);
      return createProcessor(procedure, service, jsonRemoteProcedureSignature);
    } catch (Exception e) {
      throw new IllegalArgumentException("Exception occurred while preparing request processor for [" + procedure + ']',
        e);
    }
  }

  private static <T> JsonRpcRequestProcessor createProcessor(
    Class<T> procedure, T service, JsonRemoteProcedureSignature jsonRemoteProcedureSignature)
    throws NoSuchMethodException, IllegalAccessException {
    MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();
    final MethodHandle methodHandle = publicLookup.findVirtual(procedure, CALL_METHOD_NAME,
      jsonRemoteProcedureSignature.getMethodType());
    NamedMethodHandle namedMethodHandle = new NamedMethodHandle(methodHandle.asSpreader(Object[].class,
      jsonRemoteProcedureSignature.getParametersCount()), jsonRemoteProcedureSignature.getParameterNames(),
      jsonRemoteProcedureSignature.getGenericTypes());
    return new MethodHandleJsonRpcRequestProcessor<>(namedMethodHandle, service);
  }

  public static class NamedMethodHandle {
    private final MethodHandle methodHandle;
    private final String[] parameterNames;
    private final Type[] argumentTypes;

    public NamedMethodHandle(MethodHandle methodHandle, String[] parameterNames, Type[] argumentTypes) {
      this.methodHandle = methodHandle;
      this.parameterNames = parameterNames;
      this.argumentTypes = argumentTypes;
    }

    public Object invokeWithArguments(Object... args) throws Throwable {
      return methodHandle.invokeWithArguments(args);
    }
  }

  private static class MethodHandleJsonRpcRequestProcessor<S> implements JsonRpcRequestProcessor {
    private static final Object[] EMPTY_ARGS = new Object[0];
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
      if (paramsValue == null) {
        responseConsumer.accept(process(RequestDto.positional(id, procedureName, EMPTY_ARGS)));
        return;
      }
      switch (paramsValue.getValueType()) {
        case ARRAY:
          responseConsumer.accept(process(RequestDto.positional(id, procedureName,
            getArgumentsFromArray(jsonb, paramsValue, namedMethodHandle.argumentTypes.length))));
          return;
        case OBJECT:
          responseConsumer.accept(process(RequestDto.positional(id, procedureName,
            getArgumentsFromMap(jsonb, paramsValue))));
          return;
        default:
          responseConsumer.accept(ErrorResponse.fromJsonRpcError(id, Errors.INVALID_METHOD_PARAMETERS.getError()
            .appendMessage("Unsupported request Type")));
      }
    }

    @Override
    public void process(String procedureName, JsonObject jsonObject, Jsonb jsonb) {
      JsonValue paramsValue = jsonObject.get(SerializationProperties.PARAMS_KEY);
      if (paramsValue == null) {
        process(new NotificationDto(procedureName, EMPTY_ARGS));
        return;
      }
      int argumentsCount = namedMethodHandle.argumentTypes.length;
      switch (paramsValue.getValueType()) {
        case ARRAY:
          process(new NotificationDto(procedureName, getArgumentsFromArray(jsonb, paramsValue, argumentsCount)));
          return;
        case OBJECT:
          process(new NotificationDto(procedureName, getArgumentsFromMap(jsonb, paramsValue)));
          return;
        default:
          LOGGER.error("Exception [{}] occurred while processing notification",
            Errors.INVALID_METHOD_PARAMETERS.getError().appendMessage("Unsupported request Type"));
      }
    }

    private Object[] getArgumentsFromArray(Jsonb jsonb, JsonValue paramsValue, int argumentsCount) {
      Object[] arguments = new Object[argumentsCount];
      JsonArray jsonParamsArray = paramsValue.asJsonArray();
      for (int i = 0; i < argumentsCount; i++) {
        arguments[i] = jsonb.fromJson(jsonParamsArray.get(i).toString(), namedMethodHandle.argumentTypes[i]);
      }
      return arguments;
    }

    private Object[] getArgumentsFromMap(Jsonb jsonb, JsonValue paramsValue) {
      JsonObject jsonObject = paramsValue.asJsonObject();
      int argumentsCount = namedMethodHandle.parameterNames.length;
      Object[] arguments = new Object[argumentsCount];
      for (int i = 0; i < argumentsCount; i++) {
        arguments[i] = jsonb.fromJson(jsonObject.get(namedMethodHandle.parameterNames[i]).toString(),
          namedMethodHandle.argumentTypes[i]);
      }
      return arguments;
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


    public void process(NotificationDto notificationRequest) {
      try {
        invokeMethod(notificationRequest);
      } catch (Throwable throwable) {
        String procedureName = notificationRequest.getProcedureName();
        LOGGER.error("Exception occurred while invoking method [{}]. Message is:{}",
          procedureName, throwable.getMessage());
      }
    }

    private void invokeMethod(NotificationDto notificationRequest) throws Throwable {
      final Object result = namedMethodHandle.invokeWithArguments(service, notificationRequest.getArgs());
      if (result != null) {
        LOGGER.warn("Result isn't null for notification request:[{}]", notificationRequest);
      }
    }
  }
}
