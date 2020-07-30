package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request2;
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

public class NettyRequest2<T1, T2, R> extends AbstractNettyRequest<R, NettyRequest2<T1, T2, R>>
  implements Request2<T1, T2, R> {

  public NettyRequest2(JsonRpcNettySender defaultJsonRpcNettySender, SocketAddress defaultSocketAddress,
                       Jsonb defaultJsonb, Charset defaultCharset, JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyRequest2<T1, T2, R> socketAddress(SocketAddress socketAddress) {
    return new NettyRequest2<>(defaultJsonRpcNettySender, socketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyRequest2<T1, T2, R> jsonb(Jsonb jsonb) {
    return new NettyRequest2<>(defaultJsonRpcNettySender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyRequest2<T1, T2, R> charset(Charset charset) {
    return new NettyRequest2<>(defaultJsonRpcNettySender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2) {
    return defaultJsonRpcNettySender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2}), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  @Override
  public CompletableFuture<R> namedCall(String id, T1 arg1, T2 arg2) {
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    return defaultJsonRpcNettySender.send(id, RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(),
      argsMap), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  @Override
  public void notificationCall(T1 arg1, T2 arg2) {
    defaultJsonRpcNettySender.send(new NotificationDto(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2}), jsonb, charset, defaultSocketAddress);
  }
}
