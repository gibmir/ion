package com.github.gibmir.ion.lib.netty.client.http.request;

import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gibmir.ion.scanner.ProcedureScanner;
import com.github.gibmir.ion.scanner.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;

public final class NettyHttpRequest1<T, R> extends AbstractNettyHttpRequest<NettyHttpRequest1<T, R>>
  implements Request1<T, R> {

  public NettyHttpRequest1(final NettyHttpJsonRpcSender defaultJsonRpcSender, final URI defaultUri, final Jsonb jsonb,
                           final Charset charset, final JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcSender, defaultUri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyHttpRequest1<T, R> jsonb(final Jsonb jsonb) {
    return new NettyHttpRequest1<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyHttpRequest1<T, R> charset(final Charset charset) {
    return new NettyHttpRequest1<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public CompletableFuture<R> positionalCall(final String id, final T arg) {
    return defaultJsonRpcSender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg}), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), uri);
  }

  @Override
  public CompletableFuture<R> namedCall(final String id, final T arg) {
    Map<String, Object> argsMap = new WeakHashMap<>(1);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg);
    return defaultJsonRpcSender.send(id, RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(), argsMap),
      jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), uri);
  }

  @Override
  public void positionalNotificationCall(final T arg) {
    defaultJsonRpcSender.send(NotificationDto.positional(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg}), jsonb, charset, uri);
  }

  @Override
  public void namedNotificationCall(final T arg) {
    Map<String, Object> argsMap = new WeakHashMap<>(1);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg);
    defaultJsonRpcSender.send(NotificationDto.named(jsonRemoteProcedureSignature.getProcedureName(), argsMap),
      jsonb, charset, uri);
  }

  @Override
  public NettyHttpRequest1<T, R> uri(final URI uri) {
    return new NettyHttpRequest1<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }
}
