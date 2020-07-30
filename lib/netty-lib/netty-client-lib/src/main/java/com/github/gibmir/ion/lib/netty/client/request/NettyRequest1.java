package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;

public class NettyRequest1<T, R> extends AbstractNettyRequest<R, NettyRequest1<T, R>>
  implements Request1<T, R> {

  public NettyRequest1(JsonRpcNettySender defaultJsonRpcNettySender, SocketAddress defaultSocketAddress,
                       Jsonb defaultJsonb, Charset defaultCharset, JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyRequest1<T, R> socketAddress(SocketAddress socketAddress) {
    return new NettyRequest1<>(defaultJsonRpcNettySender, socketAddress, jsonb,
      charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyRequest1<T, R> jsonb(Jsonb jsonb) {
    return new NettyRequest1<>(defaultJsonRpcNettySender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyRequest1<T, R> charset(Charset charset) {
    return new NettyRequest1<>(defaultJsonRpcNettySender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T arg) {
    return defaultJsonRpcNettySender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg}), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  @Override
  public CompletableFuture<R> namedCall(String id, T arg) {
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg);
    return defaultJsonRpcNettySender.send(id, RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(), argsMap),
      jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  @Override
  public void notificationCall(T arg) {
    defaultJsonRpcNettySender.send(new NotificationDto(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg}), jsonb, charset, defaultSocketAddress);
  }
}
