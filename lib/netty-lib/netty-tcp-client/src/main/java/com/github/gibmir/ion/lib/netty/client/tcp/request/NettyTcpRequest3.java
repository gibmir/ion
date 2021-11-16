package com.github.gibmir.ion.lib.netty.client.tcp.request;

import com.github.gibmir.ion.api.client.request.Request3;
import com.github.gibmir.ion.lib.netty.client.tcp.sender.NettyTcpJsonRpcSender;
import com.github.gibmir.ion.scanner.ProcedureScanner;
import com.github.gibmir.ion.scanner.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CompletableFuture;

public class NettyTcpRequest3<T1, T2, T3, R> extends AbstractNettyTcpRequest<NettyTcpRequest3<T1, T2, T3, R>>
  implements Request3<T1, T2, T3, R> {

  public NettyTcpRequest3(final NettyTcpJsonRpcSender defaultJsonRpcNettySender, final SocketAddress defaultSocketAddress,
                          final Jsonb defaultJsonb, final Charset defaultCharset,
                          final JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyTcpRequest3<T1, T2, T3, R> socketAddress(final SocketAddress socketAddress) {
    return new NettyTcpRequest3<>(defaultJsonRpcSender, socketAddress, jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyTcpRequest3<T1, T2, T3, R> jsonb(final Jsonb jsonb) {
    return new NettyTcpRequest3<>(defaultJsonRpcSender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyTcpRequest3<T1, T2, T3, R> charset(final Charset charset) {
    return new NettyTcpRequest3<>(defaultJsonRpcSender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<R> positionalCall(final String id, final T1 arg1, final T2 arg2, final T3 arg3) {
    return defaultJsonRpcSender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2, arg3}), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<R> namedCall(final String id, final T1 arg1, final T2 arg2, final T3 arg3) {
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    argsMap.put(parameterNames[ProcedureScanner.THIRD_PROCEDURE_PARAMETER], arg3);
    return defaultJsonRpcSender.send(id, RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(),
      argsMap), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void positionalNotificationCall(final T1 arg1, final T2 arg2, final T3 arg3) {
    defaultJsonRpcSender.send(NotificationDto.positional(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2, arg3}), jsonb, charset, defaultSocketAddress);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void namedNotificationCall(final T1 arg1, final T2 arg2, final T3 arg3) {
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    argsMap.put(parameterNames[ProcedureScanner.THIRD_PROCEDURE_PARAMETER], arg3);
    defaultJsonRpcSender.send(NotificationDto.named(jsonRemoteProcedureSignature.getProcedureName(), argsMap),
      jsonb, charset, defaultSocketAddress);
  }
}
