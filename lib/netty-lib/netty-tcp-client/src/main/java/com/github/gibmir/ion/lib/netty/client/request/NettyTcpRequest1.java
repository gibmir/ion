package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request1;
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

public final class NettyTcpRequest1<T, R> extends AbstractNettyTcpRequest<NettyTcpRequest1<T, R>>
  implements Request1<T, R> {

  public NettyTcpRequest1(final JsonRpcSender jsonRpcSender, final SocketAddress defaultSocketAddress,
                          final Jsonb defaultJsonb, final Charset defaultCharset,
                          final JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(jsonRpcSender, defaultSocketAddress, defaultJsonb, defaultCharset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyTcpRequest1<T, R> socketAddress(final SocketAddress socketAddress) {
    return new NettyTcpRequest1<>(defaultJsonRpcSender, socketAddress, jsonb,
      charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyTcpRequest1<T, R> jsonb(final Jsonb jsonb) {
    return new NettyTcpRequest1<>(defaultJsonRpcSender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NettyTcpRequest1<T, R> charset(final Charset charset) {
    return new NettyTcpRequest1<>(defaultJsonRpcSender, defaultSocketAddress,
      jsonb, charset, jsonRemoteProcedureSignature);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<R> positionalCall(final String id, final T arg) {
    return defaultJsonRpcSender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg}), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CompletableFuture<R> namedCall(final String id, final T arg) {
    Map<String, Object> argsMap = new WeakHashMap<>(3);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg);
    return defaultJsonRpcSender.send(id, RequestDto.named(id, jsonRemoteProcedureSignature.getProcedureName(), argsMap),
      jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void positionalNotificationCall(final T arg) {
    defaultJsonRpcSender.send(NotificationDto.positional(jsonRemoteProcedureSignature.getProcedureName(),
      new Object[]{arg}), jsonb, charset, defaultSocketAddress);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void namedNotificationCall(final T arg) {
    Map<String, Object> argsMap = new WeakHashMap<>(1);
    String[] parameterNames = jsonRemoteProcedureSignature.getParameterNames();
    argsMap.put(parameterNames[ProcedureScanner.FIRST_PROCEDURE_PARAMETER], arg);
    defaultJsonRpcSender.send(NotificationDto.named(jsonRemoteProcedureSignature.getProcedureName(),
      argsMap), jsonb, charset, defaultSocketAddress);
  }
}
