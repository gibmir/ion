package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.common.sender.JsonRpcSender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;

public class NettyTcpRequest1<T, R> extends AbstractNettyTcpRequest<NettyTcpRequest1<T, R>>
  implements Request1<T, R> {

  public NettyTcpRequest1(JsonRpcSender jsonRpcSender, SocketAddress defaultSocketAddress,
                          Jsonb defaultJsonb, Charset defaultCharset, JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(jsonRpcSender, defaultSocketAddress, defaultJsonb, defaultCharset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyTcpRequest1<T, R> socketAddress(SocketAddress socketAddress) {
    return new NettyTcpRequest1<>(defaultJsonRpcSender, socketAddress, jsonb,
      charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyTcpRequest1<T, R> jsonb(Jsonb jsonb) {
    return new NettyTcpRequest1<>(defaultJsonRpcSender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyTcpRequest1<T, R> charset(Charset charset) {
    return new NettyTcpRequest1<>(defaultJsonRpcSender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T arg) {
    return defaultJsonRpcSender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg}), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  @Override
  public CompletableFuture<R> namedCall(String id, T arg) {
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg);
    return defaultJsonRpcSender.send(id, RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(), argsMap),
      jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  @Override
  public void notificationCall(T arg) {
    defaultJsonRpcSender.send(new NotificationDto(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg}), jsonb, charset, defaultSocketAddress);
  }
}
