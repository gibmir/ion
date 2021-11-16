package com.github.gibmir.ion.lib.netty.client.tcp.request;

import com.github.gibmir.ion.api.client.request.Request0;
import com.github.gibmir.ion.lib.netty.client.tcp.sender.NettyTcpJsonRpcSender;
import com.github.gibmir.ion.scanner.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public final class NettyTcpRequest0<R> extends AbstractNettyTcpRequest<NettyTcpRequest0<R>>
  implements Request0<R> {
  public static final Object[] EMPTY_PAYLOAD = new Object[0];


  public NettyTcpRequest0(final NettyTcpJsonRpcSender defaultJsonRpcNettySender, final SocketAddress defaultSocketAddress,
                          final Jsonb defaultJsonb, final Charset defaultCharset,
                          final JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset, jsonRemoteProcedureSignature);
  }

  @Override
  public CompletableFuture<R> call(final String id) {
    return defaultJsonRpcSender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      EMPTY_PAYLOAD), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  @Override
  public void notificationCall() {
    defaultJsonRpcSender.send(NotificationDto.empty(jsonRemoteProcedureSignature.getProcedureName()),
      jsonb, charset, defaultSocketAddress);
  }

  @Override
  public NettyTcpRequest0<R> socketAddress(final SocketAddress socketAddress) {
    return new NettyTcpRequest0<>(defaultJsonRpcSender, socketAddress, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyTcpRequest0<R> jsonb(final Jsonb jsonb) {
    return new NettyTcpRequest0<>(defaultJsonRpcSender, defaultSocketAddress, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyTcpRequest0<R> charset(final Charset charset) {
    return new NettyTcpRequest0<>(defaultJsonRpcSender, defaultSocketAddress, jsonb, charset, jsonRemoteProcedureSignature);
  }
}
