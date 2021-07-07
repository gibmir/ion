package com.github.gibmir.ion.lib.netty.client.common.request.batch;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.scanner.ProcedureScanner;
import com.github.gibmir.ion.scanner.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class BatchRequestAggregator {
  public static final Object[] EMPTY_PAYLOAD = new Object[0];
  private final List<JsonRpcRequest> requests = new ArrayList<>();
  private final List<NettyBatch.BatchPart<?>> batchParts = new ArrayList<>();

  /**
   * Appends request to batch.
   *
   * @param id                   request id
   * @param jsonRemoteProcedure0 remote procedure
   * @param responseCallback     callback
   * @param <R>                  return type
   */
  public <R> void addRequest(final String id, final Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0,
                             final ResponseCallback<R> responseCallback) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature0(jsonRemoteProcedure0);
    RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(), EMPTY_PAYLOAD);
    requests.add(requestDto);
    batchParts.add(new NettyBatch.BatchPart<>(id, responseCallback, jsonRemoteProcedureSignature.getReturnType()));
  }

  /**
   * Appends positional request to batch.
   *
   * @param id                   request id
   * @param jsonRemoteProcedure1 remote procedure
   * @param responseCallback     callback
   * @param arg                  first argument
   * @param <T1>                 first argument type
   * @param <R>                  return type
   */
  public <T1, R> void addPositionalRequest(final String id,
                                           final Class<? extends JsonRemoteProcedure1<T1, R>> jsonRemoteProcedure1,
                                           final T1 arg, final ResponseCallback<R> responseCallback) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature1(jsonRemoteProcedure1);
    RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg});
    requests.add(requestDto);
    batchParts.add(new NettyBatch.BatchPart<>(id, responseCallback, jsonRemoteProcedureSignature.getReturnType()));
  }

  /**
   * Appends positional request to batch.
   *
   * @param id                   request id
   * @param jsonRemoteProcedure2 remote procedure
   * @param responseCallback     callback
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <R>                  return type
   */
  public <T1, T2, R> void addPositionalRequest(final String id,
                                               final Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                               final T1 arg1, final T2 arg2, final ResponseCallback<R> responseCallback) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature2(jsonRemoteProcedure2);
    RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2});
    requests.add(requestDto);
    batchParts.add(new NettyBatch.BatchPart<>(id, responseCallback, jsonRemoteProcedureSignature.getReturnType()));
  }

  /**
   * Appends positional request to batch.
   *
   * @param id                   request id
   * @param jsonRemoteProcedure3 remote procedure
   * @param responseCallback     callback
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param arg3                 third argument
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <T3>                 third argument type
   * @param <R>                  return type
   */
  public <T1, T2, T3, R> void addPositionalRequest(final String id,
                                                   final Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                   final T1 arg1, final T2 arg2, final T3 arg3,
                                                   final ResponseCallback<R> responseCallback) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature3(jsonRemoteProcedure3);
    RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2, arg3});
    requests.add(requestDto);
    batchParts.add(new NettyBatch.BatchPart<>(id, responseCallback, jsonRemoteProcedureSignature.getReturnType()));
  }

  /**
   * Appends named request to batch.
   *
   * @param id                   request id
   * @param jsonRemoteProcedure1 remote procedure
   * @param responseCallback     callback
   * @param arg                  first argument
   * @param <T1>                 first argument type
   * @param <R>                  return type
   */
  public <T1, R> void addNamedRequest(final String id,
                                      final Class<? extends JsonRemoteProcedure1<T1, R>> jsonRemoteProcedure1,
                                      final T1 arg, final ResponseCallback<R> responseCallback) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature1(jsonRemoteProcedure1);
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg);
    RequestDto requestDto = RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(), argsMap);
    requests.add(requestDto);
    batchParts.add(new NettyBatch.BatchPart<>(id, responseCallback, jsonRemoteProcedureSignature.getReturnType()));
  }

  /**
   * Appends named request to batch.
   *
   * @param id                   request id
   * @param jsonRemoteProcedure2 remote procedure
   * @param responseCallback     callback
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <R>                  return type
   */
  public <T1, T2, R> void addNamedRequest(final String id,
                                          final Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                          final T1 arg1, final T2 arg2, final ResponseCallback<R> responseCallback) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature2(jsonRemoteProcedure2);
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    RequestDto requestDto = RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(), argsMap);
    requests.add(requestDto);
    batchParts.add(new NettyBatch.BatchPart<>(id, responseCallback, jsonRemoteProcedureSignature.getReturnType()));
  }

  /**
   * Appends named request to batch.
   *
   * @param id                   request id
   * @param jsonRemoteProcedure3 remote procedure
   * @param responseCallback     callback
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param arg3                 third argument
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <T3>                 third argument type
   * @param <R>                  return type
   */
  public <T1, T2, T3, R> void addNamedRequest(final String id,
                                              final Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                              final T1 arg1, final T2 arg2, final T3 arg3,
                                              final ResponseCallback<R> responseCallback) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature3(jsonRemoteProcedure3);
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    argsMap.put(parameterNames[ProcedureScanner.THIRD_PROCEDURE_PARAMETER], arg3);
    RequestDto requestDto = RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(), argsMap);
    requests.add(requestDto);
    batchParts.add(new NettyBatch.BatchPart<>(id, responseCallback, jsonRemoteProcedureSignature.getReturnType()));
  }

  /**
   * Appends notification to batch.
   *
   * @param jsonRemoteProcedure0 remote procedure
   * @param <R>                  return type
   */
  public <R> void addNotification(final Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature0(jsonRemoteProcedure0);
    requests.add(NotificationDto.empty(jsonRemoteProcedureSignature.getProcedureName()));

  }

  /**
   * Appends positional notification to batch.
   *
   * @param jsonRemoteProcedure1 remote procedure
   * @param arg                  first argument
   * @param <T1>                 first argument type
   * @param <R>                  return type
   */
  public <T1, R> void addPositionalNotification(final Class<? extends JsonRemoteProcedure1<T1, R>> jsonRemoteProcedure1,
                                                final T1 arg) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature1(jsonRemoteProcedure1);
    requests.add(NotificationDto.positional(jsonRemoteProcedureSignature.getProcedureName(), new Object[]{arg}));
  }

  /**
   * Appends positional notification to batch.
   *
   * @param jsonRemoteProcedure2 remote procedure
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <R>                  return type
   */
  public <T1, T2, R> void addPositionalNotification(final Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                                    final T1 arg1, final T2 arg2) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature2(jsonRemoteProcedure2);
    requests.add(NotificationDto.positional(jsonRemoteProcedureSignature.getProcedureName(), new Object[]{arg1, arg2}));
  }

  /**
   * Appends positional notification to batch.
   *
   * @param jsonRemoteProcedure3 remote procedure
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param arg3                 third argument
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <T3>                 third argument type
   * @param <R>                  return type
   */
  public <T1, T2, T3, R> void addPositionalNotification(final Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                        final T1 arg1, final T2 arg2, final T3 arg3) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature3(jsonRemoteProcedure3);
    requests.add(NotificationDto.positional(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2, arg3}));
  }

  /**
   * Appends named notification to batch.
   *
   * @param jsonRemoteProcedure1 remote procedure
   * @param arg                  first argument
   * @param <T1>                 first argument type
   * @param <R>                  return type
   */
  public <T1, R> void addNamedNotification(final Class<? extends JsonRemoteProcedure1<T1, R>> jsonRemoteProcedure1,
                                           final T1 arg) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature1(jsonRemoteProcedure1);
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg);
    requests.add(NotificationDto.named(jsonRemoteProcedureSignature.getProcedureName(), argsMap));
  }

  /**
   * Appends named notification to batch.
   *
   * @param jsonRemoteProcedure2 remote procedure
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <R>                  return type
   */
  public <T1, T2, R> void addNamedNotification(final Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                               final T1 arg1, final T2 arg2) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature2(jsonRemoteProcedure2);
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    requests.add(NotificationDto.named(jsonRemoteProcedureSignature.getProcedureName(), argsMap));
  }

  /**
   * Appends named notification to batch.
   *
   * @param jsonRemoteProcedure3 remote procedure
   * @param arg1                 first argument
   * @param arg2                 second argument
   * @param arg3                 third argument
   * @param <T1>                 first argument type
   * @param <T2>                 second argument type
   * @param <T3>                 third argument type
   * @param <R>                  return type
   */
  public <T1, T2, T3, R> void addNamedNotification(final Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                   final T1 arg1, final T2 arg2, final T3 arg3) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature3(jsonRemoteProcedure3);
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    argsMap.put(parameterNames[ProcedureScanner.THIRD_PROCEDURE_PARAMETER], arg3);
    requests.add(NotificationDto.named(jsonRemoteProcedureSignature.getProcedureName(), argsMap));
  }

  /**
   * @return batch requests
   */
  public List<JsonRpcRequest> getRequests() {
    return requests;
  }

  /**
   * @return provides listeners
   */
  public List<NettyBatch.BatchPart<?>> getAwaitBatchParts() {
    return batchParts;
  }
}
