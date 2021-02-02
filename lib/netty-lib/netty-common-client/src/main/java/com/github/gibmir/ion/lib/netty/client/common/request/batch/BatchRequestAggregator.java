package com.github.gibmir.ion.lib.netty.client.common.request.batch;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
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
  private final List<NettyBatch.AwaitBatchPart> awaitBatchParts = new ArrayList<>();

  public <R> void addRequest(String id, Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature0(jsonRemoteProcedure0);
    RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(), EMPTY_PAYLOAD);
    requests.add(requestDto);
    awaitBatchParts.add(new NettyBatch.AwaitBatchPart(id, jsonRemoteProcedureSignature.getReturnType()));
  }


  public <T1, R> void addPositionalRequest(String id, Class<? extends JsonRemoteProcedure1<T1, R>> jsonRemoteProcedure1, T1 arg) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature1(jsonRemoteProcedure1);
    RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg});
    requests.add(requestDto);
    awaitBatchParts.add(new NettyBatch.AwaitBatchPart(id, jsonRemoteProcedureSignature.getReturnType()));
  }


  public <T1, T2, R> void addPositionalRequest(String id, Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                               T1 arg1, T2 arg2) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature2(jsonRemoteProcedure2);
    RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2});
    requests.add(requestDto);
    awaitBatchParts.add(new NettyBatch.AwaitBatchPart(id, jsonRemoteProcedureSignature.getReturnType()));
  }


  public <T1, T2, T3, R> void addPositionalRequest(String id,
                                                   Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                   T1 arg1, T2 arg2, T3 arg3) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature3(jsonRemoteProcedure3);
    RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2, arg3});
    requests.add(requestDto);
    awaitBatchParts.add(new NettyBatch.AwaitBatchPart(id, jsonRemoteProcedureSignature.getReturnType()));
  }


  public <T1, R> void addNamedRequest(String id, Class<? extends JsonRemoteProcedure1<T1, R>> jsonRemoteProcedure1, T1 arg) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature1(jsonRemoteProcedure1);
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg);
    RequestDto requestDto = RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(), argsMap);
    requests.add(requestDto);
    awaitBatchParts.add(new NettyBatch.AwaitBatchPart(id, jsonRemoteProcedureSignature.getReturnType()));
  }

  public <T1, T2, R> void addNamedRequest(String id, Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                          T1 arg1, T2 arg2) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature2(jsonRemoteProcedure2);
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    RequestDto requestDto = RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(), argsMap);
    requests.add(requestDto);
    awaitBatchParts.add(new NettyBatch.AwaitBatchPart(id, jsonRemoteProcedureSignature.getReturnType()));
  }

  public <T1, T2, T3, R> void addNamedRequest(String id, Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                              T1 arg1, T2 arg2, T3 arg3) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature3(jsonRemoteProcedure3);
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    argsMap.put(parameterNames[ProcedureScanner.THIRD_PROCEDURE_PARAMETER], arg3);
    RequestDto requestDto = RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(), argsMap);
    requests.add(requestDto);
    awaitBatchParts.add(new NettyBatch.AwaitBatchPart(id, jsonRemoteProcedureSignature.getReturnType()));
  }

  public <R> void addNotification(Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature0(jsonRemoteProcedure0);
    requests.add(NotificationDto.empty(jsonRemoteProcedureSignature.getProcedureName()));

  }

  public <T1, R> void addPositionalNotification(Class<? extends JsonRemoteProcedure1<T1, R>> jsonRemoteProcedure1, T1 arg) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature1(jsonRemoteProcedure1);
    requests.add(NotificationDto.positional(jsonRemoteProcedureSignature.getProcedureName(), new Object[]{arg}));
  }


  public <T1, T2, R> void addPositionalNotification(Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                                    T1 arg1, T2 arg2) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature2(jsonRemoteProcedure2);
    requests.add(NotificationDto.positional(jsonRemoteProcedureSignature.getProcedureName(), new Object[]{arg1, arg2}));
  }


  public <T1, T2, T3, R> void addPositionalNotification(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                        T1 arg1, T2 arg2, T3 arg3) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature3(jsonRemoteProcedure3);
    requests.add(NotificationDto.positional(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2, arg3}));
  }

  public <T1, R> void addNamedNotification(Class<? extends JsonRemoteProcedure1<T1, R>> jsonRemoteProcedure1, T1 arg) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature1(jsonRemoteProcedure1);
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg);
    requests.add(NotificationDto.named(jsonRemoteProcedureSignature.getProcedureName(), argsMap));
  }


  public <T1, T2, R> void addNamedNotification(Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                               T1 arg1, T2 arg2) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature2(jsonRemoteProcedure2);
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    requests.add(NotificationDto.named(jsonRemoteProcedureSignature.getProcedureName(), argsMap));
  }


  public <T1, T2, T3, R> void addNamedNotification(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                   T1 arg1, T2 arg2, T3 arg3) {
    JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature3(jsonRemoteProcedure3);
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    argsMap.put(parameterNames[ProcedureScanner.THIRD_PROCEDURE_PARAMETER], arg3);
    requests.add(NotificationDto.named(jsonRemoteProcedureSignature.getProcedureName(), argsMap));
  }

  public List<JsonRpcRequest> getRequests() {
    return requests;
  }

  public List<NettyBatch.AwaitBatchPart> getAwaitBatchParts() {
    return awaitBatchParts;
  }
}
