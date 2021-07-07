package com.github.gibmir.ion.lib.netty.client.http.request;

import com.github.gibmir.ion.api.client.request.Request0;
import com.github.gibmir.ion.scanner.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public final class NettyHttpRequest0<R> extends AbstractNettyHttpRequest<NettyHttpRequest0<R>>
  implements Request0<R> {
  public static final Object[] EMPTY_PAYLOAD = new Object[0];


  public NettyHttpRequest0(final NettyHttpJsonRpcSender defaultJsonRpcNettySender, final URI defaultUri,
                           final Jsonb defaultJsonb, final Charset defaultCharset,
                           final JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    super(defaultJsonRpcNettySender, defaultUri, defaultJsonb, defaultCharset, jsonRemoteProcedureSignature);
  }

  @Override
  public CompletableFuture<R> call(final String id) {
    return defaultJsonRpcSender.send(id, RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
      EMPTY_PAYLOAD), jsonb, charset, jsonRemoteProcedureSignature.getReturnType(), uri);
  }

  @Override
  public void notificationCall() {
    defaultJsonRpcSender.send(NotificationDto.empty(jsonRemoteProcedureSignature.getProcedureName()),
      jsonb, charset, uri);
  }

  @Override
  public NettyHttpRequest0<R> jsonb(final Jsonb jsonb) {
    return new NettyHttpRequest0<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyHttpRequest0<R> charset(final Charset charset) {
    return new NettyHttpRequest0<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }

  @Override
  public NettyHttpRequest0<R> uri(final URI uri) {
    return new NettyHttpRequest0<>(defaultJsonRpcSender, uri, jsonb, charset, jsonRemoteProcedureSignature);
  }
}
