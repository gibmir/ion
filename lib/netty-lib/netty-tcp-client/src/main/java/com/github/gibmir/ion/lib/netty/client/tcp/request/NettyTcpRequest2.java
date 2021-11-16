package com.github.gibmir.ion.lib.netty.client.tcp.request;

import com.github.gibmir.ion.api.client.request.Request2;
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

public class NettyTcpRequest2<T1, T2, R> extends AbstractNettyTcpRequest<NettyTcpRequest2<T1, T2, R>>
  implements Request2<T1, T2, R> {

  public NettyTcpRequest2(final NettyTcpJsonRpcSender defaultJsonRpcNettySender, final SocketAddress defaultSocketAddress,
                          final Jsonb defaultJsonb, final Charset defaultCharset,
                          final JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyTcpRequest2<T1, T2, R> socketAddress(final SocketAddress socketAddress) {
    return new NettyTcpRequest2<>(defaultJsonRpcSender, socketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyTcpRequest2<T1, T2, R> jsonb(final Jsonb jsonb) {
    return new NettyTcpRequest2<>(defaultJsonRpcSender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyTcpRequest2<T1, T2, R> charset(final Charset charset) {
    return new NettyTcpRequest2<>(defaultJsonRpcSender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<R> positionalCall(final String id, final T1 arg1, final T2 arg2) {
    return defaultJsonRpcSender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2}), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<R> namedCall(final String id, final T1 arg1, final T2 arg2) {
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
  public void positionalNotificationCall(final T1 arg1, final T2 arg2) {
    defaultJsonRpcSender.send(NotificationDto.positional(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg1, arg2}), jsonb, charset, defaultSocketAddress);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void namedNotificationCall(final T1 arg1, final T2 arg2) {
    Map<String, Object> argsMap = new WeakHashMap<>(2);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg1);
    argsMap.put(parameterNames[ProcedureScanner.SECOND_PROCEDURE_PARAMETER], arg2);
    defaultJsonRpcSender.send(NotificationDto.named(jsonRemoteProcedureSignature.getProcedureName(), argsMap),
      jsonb, charset, defaultSocketAddress);
  }
}
