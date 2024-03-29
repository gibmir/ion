package com.github.gibmir.ion.lib.netty.server.common.processor.factory;

import com.github.gibmir.ion.scanner.ProcedureScanner;
import com.github.gibmir.ion.scanner.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import com.github.gibmir.ion.api.server.processor.request.JsonRpcRequestProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Type;

public final class JsonRpcRequestProcessorFactory {
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonRpcRequestProcessorFactory.class);
  public static final String CALL_METHOD_NAME = "call";

  private JsonRpcRequestProcessorFactory() {
  }

  /**
   * Creates processor for procedure without args.
   *
   * @param procedure procedure API class
   * @param service   procedure impl
   * @param jsonb     serializer
   * @return request processor
   */
  public static JsonRpcRequestProcessor createProcessor0(final Class<?> procedure, final Object service,
                                                         final Jsonb jsonb) {
    try {
      JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature0(procedure);
      return createProcessor(procedure, service, jsonRemoteProcedureSignature, jsonb);
    } catch (Exception e) {
      throw new IllegalArgumentException("Exception occurred while preparing request processor for [" + procedure + ']',
        e);
    }
  }

  /**
   * Creates processor for procedure with one arg.
   *
   * @param procedure procedure API class
   * @param service   procedure impl
   * @param jsonb     serializer
   * @return request processor
   */
  public static JsonRpcRequestProcessor createProcessor1(final Class<?> procedure, final Object service,
                                                         final Jsonb jsonb) {
    try {
      JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature1(procedure);
      return createProcessor(procedure, service, jsonRemoteProcedureSignature, jsonb);
    } catch (Exception e) {
      throw new IllegalArgumentException("Exception occurred while preparing request processor for [" + procedure + ']',
        e);
    }
  }

  /**
   * Creates processor for procedure with two arg.
   *
   * @param procedure procedure API class
   * @param service   procedure impl
   * @param jsonb     serializer
   * @return request processor
   */
  public static JsonRpcRequestProcessor createProcessor2(final Class<?> procedure, final Object service,
                                                         final Jsonb jsonb) {
    try {
      JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature2(procedure);
      return createProcessor(procedure, service, jsonRemoteProcedureSignature, jsonb);
    } catch (Exception e) {
      throw new IllegalArgumentException("Exception occurred while preparing request processor for [" + procedure + ']',
        e);
    }
  }

  /**
   * Creates processor for procedure with three arg.
   *
   * @param procedure procedure API class
   * @param service   procedure impl
   * @param jsonb     serializer
   * @return request processor
   */
  public static JsonRpcRequestProcessor createProcessor3(final Class<?> procedure, final Object service,
                                                         final Jsonb jsonb) {
    try {
      JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature3(procedure);
      return createProcessor(procedure, service, jsonRemoteProcedureSignature, jsonb);
    } catch (Exception e) {
      throw new IllegalArgumentException("Exception occurred while preparing request processor for [" + procedure + ']',
        e);
    }
  }

  private static JsonRpcRequestProcessor createProcessor(final Class<?> procedure, final Object service,
                                                         final JsonRemoteProcedureSignature jsonRemoteProcedureSignature,
                                                         final Jsonb jsonb)
    throws NoSuchMethodException, IllegalAccessException {
    LOGGER.trace("Procedure signature was resolved {}. Starting method handle creation", jsonRemoteProcedureSignature);
    MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();
    final MethodHandle methodHandle = publicLookup.findVirtual(procedure, CALL_METHOD_NAME,
      jsonRemoteProcedureSignature.getMethodType());
    NamedMethodHandle namedMethodHandle = new NamedMethodHandle(methodHandle.asSpreader(Object[].class,
      jsonRemoteProcedureSignature.getParametersCount()), jsonRemoteProcedureSignature.getParameterNames(),
      jsonRemoteProcedureSignature.getGenericTypes());
    return new MethodHandleJsonRpcRequestProcessor<>(namedMethodHandle, service, jsonb,
      LoggerFactory.getLogger(MethodHandleJsonRpcRequestProcessor.class));
  }

  public static class NamedMethodHandle {
    private final MethodHandle methodHandle;
    private final String[] parameterNames;
    private final Type[] argumentTypes;

    public NamedMethodHandle(final MethodHandle methodHandle, final String[] parameterNames,
                             final Type[] argumentTypes) {
      this.methodHandle = methodHandle;
      this.parameterNames = parameterNames;
      this.argumentTypes = argumentTypes;
    }

    /**
     * Invokes method with {@link MethodHandle}.
     *
     * @param args method arguments
     * @return invocation result
     * @throws Throwable if method throws exception
     */
    public Object invokeWithArguments(final Object... args) throws Throwable {
      return methodHandle.invokeWithArguments(args);
    }

    /**
     * Returns arguments array length.
     *
     * @return arguments array length
     */
    public int getArgumentsCount() {
      return argumentTypes.length;
    }

    /**
     * Returns names array length.
     *
     * @return names array length
     */
    public int getParameterNamesCount() {
      return parameterNames.length;
    }

    /**
     * Returns parameter name by specified index.
     *
     * @param i parameter name index
     * @return parameter name
     */
    public String getParameterName(final int i) {
      return parameterNames[i];
    }

    /**
     * Returns argument type by specified index.
     *
     * @param i argument type index
     * @return argument type
     */
    public Type getArgumentType(final int i) {
      return argumentTypes[i];
    }
  }

  public static class MethodHandleJsonRpcRequestProcessor<S> implements JsonRpcRequestProcessor {
    private static final Object[] EMPTY_ARGS = new Object[0];
    private final Logger logger;
    private final NamedMethodHandle namedMethodHandle;
    private final S service;
    private final Jsonb jsonb;

    public MethodHandleJsonRpcRequestProcessor(final NamedMethodHandle namedMethodHandle, final S service,
                                               final Jsonb jsonb, final Logger logger) {
      this.namedMethodHandle = namedMethodHandle;
      this.service = service;
      this.jsonb = jsonb;
      this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JsonRpcResponse processRequest(final String id, final String procedureName, final String argumentsJson) {
      JsonValue paramsValue = jsonb.fromJson(argumentsJson, JsonValue.class);
      if (paramsValue == null) {
        return process(RequestDto.positional(id, procedureName, EMPTY_ARGS));
      }
      switch (paramsValue.getValueType()) {
        case ARRAY:
          return process(RequestDto.positional(id, procedureName,
            getArgumentsFromArray(this.jsonb, paramsValue.asJsonArray(), namedMethodHandle.getArgumentsCount())));
        case OBJECT:
          return process(RequestDto.positional(id, procedureName,
            getArgumentsFromMap(this.jsonb, paramsValue.asJsonObject())));
        default:
          return ErrorResponse.fromJsonRpcError(id, Errors.INVALID_METHOD_PARAMETERS.getError()
            .appendMessage(String.format("Request with id [%s] has unsupported type", id)));
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processNotification(final String procedureName, final String argumentsJson) {
      JsonValue paramsValue = jsonb.fromJson(argumentsJson, JsonValue.class);
      if (paramsValue == null) {
        process(NotificationDto.empty(procedureName));
        return;
      }
      switch (paramsValue.getValueType()) {
        case ARRAY:
          process(NotificationDto.positional(procedureName, getArgumentsFromArray(jsonb, paramsValue.asJsonArray(),
            namedMethodHandle.getArgumentsCount())));
          return;
        case OBJECT:
          //named processing
          process(NotificationDto.positional(procedureName, getArgumentsFromMap(jsonb, paramsValue.asJsonObject())));
          return;
        default:
          String message = String.format("Exception occurred while processing notification for procedure [%s]. %s",
            procedureName, Errors.INVALID_METHOD_PARAMETERS.getError().appendMessage("Unsupported request Type"));
          logger.error(message);
      }
    }

    private Object[] getArgumentsFromArray(final Jsonb jsonb, final JsonArray jsonParamsArray, final int argumentsCount) {
      Object[] arguments = new Object[argumentsCount];
      for (int i = 0; i < argumentsCount; i++) {
        arguments[i] = jsonb.fromJson(jsonParamsArray.get(i).toString(), namedMethodHandle.getArgumentType(i));
      }
      return arguments;
    }

    private Object[] getArgumentsFromMap(final Jsonb jsonb, final JsonObject jsonObject) {
      int argumentsCount = namedMethodHandle.getParameterNamesCount();
      Object[] arguments = new Object[argumentsCount];
      for (int i = 0; i < argumentsCount; i++) {
        arguments[i] = jsonb.fromJson(jsonObject.get(namedMethodHandle.getParameterName(i)).toString(),
          namedMethodHandle.getArgumentType(i));
      }
      return arguments;
    }


    private JsonRpcResponse process(final RequestDto positionalRequest) {
      try {
        return invokeMethod(positionalRequest);
      } catch (Throwable throwable) {
        logger.error("Exception occurred while executing request with id {}. ", positionalRequest.getId(),
          throwable);
        return ErrorResponse.fromJsonRpcError(positionalRequest.getId(),
          Errors.APPLICATION_ERROR.getError().appendMessage(
            String.format("Exception occurred while invoking request with id [%s] and method [%s]. %s",
              positionalRequest.getId(), positionalRequest.getProcedureName(), throwable)));
      }
    }

    private JsonRpcResponse invokeMethod(final RequestDto jsonRpcRequest) throws Throwable {
      final Object result = namedMethodHandle.invokeWithArguments(service, jsonRpcRequest.getArgs());
      return SuccessResponse.createWithStringId(jsonRpcRequest.getId(), result);
    }


    private void process(final NotificationDto notificationRequest) {
      try {
        invokeMethod(notificationRequest);
      } catch (Throwable throwable) {
        String procedureName = notificationRequest.getProcedureName();
        logger.error("Exception occurred while invoking notification request to procedure [{}].", procedureName, throwable);
      }
    }

    private void invokeMethod(final NotificationDto notificationRequest) throws Throwable {
      final Object result = namedMethodHandle.invokeWithArguments(service, notificationRequest.getArgs());
      if (result != null) {
        logger.warn("Result isn't null for notification request to procedure [{}]", notificationRequest.getProcedureName());
      } else {
        logger.debug("Notification request for procedure [{}] was successfully processed",
          notificationRequest.getProcedureName());
      }
    }
  }
}
