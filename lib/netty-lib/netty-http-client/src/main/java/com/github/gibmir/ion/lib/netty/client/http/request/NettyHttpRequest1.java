package com.github.gibmir.ion.lib.netty.client.http.request;

import com.github.gibmir.ion.api.client.request.Request1;
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

public class NettyHttpRequest1<T, R> extends AbstractNettyHttpRequest<NettyHttpRequest1<T, R>>
  implements Request1<T, R> {

  public NettyHttpRequest1(NettyHttpJsonRpcSender defaultJsonRpcSender, URI defaultUri, Jsonb jsonb,
                              Charset charset, JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcSender, defaultUri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyHttpRequest1<T, R> jsonb(Jsonb jsonb) {
    return new NettyHttpRequest1<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyHttpRequest1<T, R> charset(Charset charset) {
    return new NettyHttpRequest1<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T arg) {
    return defaultJsonRpcSender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg}), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), uri);
  }

  @Override
  public CompletableFuture<R> namedCall(String id, T arg) {
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg);
    return defaultJsonRpcSender.send(id, RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(), argsMap),
      jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), uri);
  }

  @Override
  public void notificationCall(T arg) {
    defaultJsonRpcSender.send(new NotificationDto(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg}), jsonb, charset, uri);
  }

  @Override
  public NettyHttpRequest1<T, R> uri(URI uri) {
    return new NettyHttpRequest1<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }
}
