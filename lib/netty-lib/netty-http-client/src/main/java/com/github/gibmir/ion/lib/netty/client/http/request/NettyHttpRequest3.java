package com.github.gibmir.ion.lib.netty.client.http.request;

import com.github.gibmir.ion.api.client.request.Request3;
import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;

public class NettyHttpRequest3<T1, T2, T3, R> extends AbstractNettyHttpRequest<NettyHttpRequest3<T1, T2, T3, R>>
  implements Request3<T1, T2, T3, R> {

  public NettyHttpRequest3(NettyHttpJsonRpcSender defaultJsonRpcSender, URI defaultUri, Jsonb jsonb,
                           Charset charset, JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcSender, defaultUri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyHttpRequest3<T1, T2, T3, R> jsonb(Jsonb jsonb) {
    return new NettyHttpRequest3<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyHttpRequest3<T1, T2, T3, R> charset(Charset charset) {
    return new NettyHttpRequest3<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2, T3 arg3) {
    return defaultJsonRpcSender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2, arg3}), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), uri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<R> namedCall(String id, T1 arg1, T2 arg2, T3 arg3) {
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    argsMap.put(parameterNames[ProcedureScanner.THIRD_PROCEDURE_PARAMETER], arg3);
    return defaultJsonRpcSender.send(id, RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(),
      argsMap), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), uri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void positionalNotificationCall(T1 arg1, T2 arg2, T3 arg3) {
    defaultJsonRpcSender.send(NotificationDto.positional(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2, arg3}), jsonb, charset, uri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void namedNotificationCall(T1 arg1, T2 arg2, T3 arg3) {
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    argsMap.put(parameterNames[ProcedureScanner.THIRD_PROCEDURE_PARAMETER], arg3);
    defaultJsonRpcSender.send(NotificationDto.named(jsonRemoteProcedureSignature.getProcedureName(), argsMap),
      jsonb, charset, uri);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyHttpRequest3<T1, T2, T3, R> uri(URI uri) {
    return new NettyHttpRequest3<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }
}
