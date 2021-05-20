package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request2;
import com.github.gibmir.ion.scanner.ProcedureScanner;
import com.github.gibmir.ion.scanner.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.common.sender.JsonRpcSender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;

public class NettyTcpRequest2<T1, T2, R> extends AbstractNettyTcpRequest<NettyTcpRequest2<T1, T2, R>>
  implements Request2<T1, T2, R> {

  public NettyTcpRequest2(JsonRpcSender defaultJsonRpcNettySender, SocketAddress defaultSocketAddress,
                          Jsonb defaultJsonb, Charset defaultCharset, JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyTcpRequest2<T1, T2, R> socketAddress(SocketAddress socketAddress) {
    return new NettyTcpRequest2<>(defaultJsonRpcSender, socketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyTcpRequest2<T1, T2, R> jsonb(Jsonb jsonb) {
    return new NettyTcpRequest2<>(defaultJsonRpcSender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyTcpRequest2<T1, T2, R> charset(Charset charset) {
    return new NettyTcpRequest2<>(defaultJsonRpcSender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2) {
    return defaultJsonRpcSender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2}), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<R> namedCall(String id, T1 arg1, T2 arg2) {
    Map<String, Object> argsMap = new WeakHashMap<>(2);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    return defaultJsonRpcSender.send(id, RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(),
      argsMap), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void positionalNotificationCall(T1 arg1, T2 arg2) {
    defaultJsonRpcSender.send(NotificationDto.positional(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2}), jsonb, charset, defaultSocketAddress);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void namedNotificationCall(T1 arg1, T2 arg2) {
    Map<String, Object> argsMap = new WeakHashMap<>(2);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    defaultJsonRpcSender.send(NotificationDto.named(jsonRemoteProcedureSignature.getProcedureName(), argsMap),
      jsonb, charset, defaultSocketAddress);
  }
}
