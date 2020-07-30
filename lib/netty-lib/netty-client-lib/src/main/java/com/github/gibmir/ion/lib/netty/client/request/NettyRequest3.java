package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request3;
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

public class NettyRequest3<T1, T2, T3, R> extends AbstractNettyRequest<R, NettyRequest3<T1, T2, T3, R>>
  implements Request3<T1, T2, T3, R> {
  public NettyRequest3(JsonRpcNettySender defaultJsonRpcNettySender, SocketAddress defaultSocketAddress,
                       Jsonb defaultJsonb, Charset defaultCharset,
                       JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyRequest3<T1, T2, T3, R> socketAddress(SocketAddress socketAddress) {
    return new NettyRequest3<>(defaultJsonRpcNettySender, socketAddress, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyRequest3<T1, T2, T3, R> jsonb(Jsonb jsonb) {
    return new NettyRequest3<>(defaultJsonRpcNettySender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyRequest3<T1, T2, T3, R> charset(Charset charset) {
    return new NettyRequest3<>(defaultJsonRpcNettySender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2, T3 arg3) {
    return defaultJsonRpcNettySender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2, arg3}), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  @Override
  public CompletableFuture<R> namedCall(String id, T1 arg1, T2 arg2, T3 arg3) {
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    argsMap.put(parameterNames[ProcedureScanner.THIRD_PROCEDURE_PARAMETER], arg3);
    return defaultJsonRpcNettySender.send(id, RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(),
      argsMap), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  @Override
  public void notificationCall(T1 arg1, T2 arg2, T3 arg3) {
    defaultJsonRpcNettySender.send(new NotificationDto(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2, arg3}), jsonb, charset, defaultSocketAddress);
  }
}
