package com.github.gibmir.ion.lib.netty.client.http.request;

import com.github.gibmir.ion.api.client.request.Request2;
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

public class NettyHttpRequest2<T1, T2, R> extends AbstractNettyHttpRequest<NettyHttpRequest2<T1, T2, R>>
  implements Request2<T1, T2, R> {
  public NettyHttpRequest2(NettyHttpJsonRpcSender defaultJsonRpcSender, URI defaultUri, Jsonb jsonb,
                              Charset charset, JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcSender, defaultUri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyHttpRequest2<T1, T2, R> jsonb(Jsonb jsonb) {
    return new NettyHttpRequest2<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyHttpRequest2<T1, T2, R> charset(Charset charset) {
    return new NettyHttpRequest2<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2) {
    return defaultJsonRpcSender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2}), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), uri);
  }

  @Override
  public CompletableFuture<R> namedCall(String id, T1 arg1, T2 arg2) {
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    return defaultJsonRpcSender.send(id, RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(),
      argsMap), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), uri);
  }

  @Override
  public void notificationCall(T1 arg1, T2 arg2) {
    defaultJsonRpcSender.send(new NotificationDto(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2}), jsonb, charset, uri);
  }

  @Override
  public NettyHttpRequest2<T1, T2, R> uri(URI uri) {
    return new NettyHttpRequest2<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }
}
