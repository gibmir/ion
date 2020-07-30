package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request0;
import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public class NettyRequest0<R> extends AbstractNettyRequest<R, NettyRequest0<R>>
  implements Request0<R> {
  public static final Object[] EMPTY_PAYLOAD = new Object[0];


  public NettyRequest0(JsonRpcNettySender defaultJsonRpcNettySender, SocketAddress defaultSocketAddress,
                       Jsonb defaultJsonb, Charset defaultCharset,
                       JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset, jsonRemoteProcedureSignature);
  }

  @Override
  public CompletableFuture<R> call(String id) {
    return defaultJsonRpcNettySender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      EMPTY_PAYLOAD), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), defaultSocketAddress);
  }

  @Override
  public void notificationCall() {
    defaultJsonRpcNettySender.send(new NotificationDto(jsonRemoteProcedureSignature.getProcedureName(), EMPTY_PAYLOAD),
      jsonb, charset, defaultSocketAddress);
  }

  @Override
  public NettyRequest0<R> socketAddress(SocketAddress socketAddress) {
    return new NettyRequest0<>(defaultJsonRpcNettySender, socketAddress, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyRequest0<R> jsonb(Jsonb jsonb) {
    return new NettyRequest0<>(defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyRequest0<R> charset(Charset charset) {
    return new NettyRequest0<>(defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset, jsonRemoteProcedureSignature);
  }
}
