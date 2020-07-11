package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request0;
import com.github.gibmir.ion.api.dto.request.transfer.NotificationDto;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public class NettyRequest0<R> extends AbstractNettyRequest<R, NettyRequest0<R>>
  implements Request0<R> {
  public static final Object[] EMPTY_PAYLOAD = new Object[0];


  public NettyRequest0(Class<R> returnType, String procedureName, JsonRpcNettySender defaultJsonRpcNettySender,
                       SocketAddress defaultSocketAddress, Jsonb defaultJsonb, Charset defaultCharset) {
    super(returnType, procedureName, defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset);
  }

  @Override
  public CompletableFuture<R> call(String id) {
    return defaultJsonRpcNettySender.send(RequestDto.positional(id, procedureName, EMPTY_PAYLOAD), jsonb, charset,
      returnType, defaultSocketAddress);
  }

  @Override
  public void notificationCall() {
    defaultJsonRpcNettySender.sendNotification(new NotificationDto(procedureName, EMPTY_PAYLOAD), jsonb, charset,
      defaultSocketAddress);
  }

  @Override
  public NettyRequest0<R> socketAddress(SocketAddress socketAddress) {
    return new NettyRequest0<>(returnType, procedureName, defaultJsonRpcNettySender, socketAddress, jsonb, charset);
  }

  @Override
  public NettyRequest0<R> jsonb(Jsonb jsonb) {
    return new NettyRequest0<>(returnType, procedureName, defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset);
  }

  @Override
  public NettyRequest0<R> charset(Charset charset) {
    return new NettyRequest0<>(returnType, procedureName, defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset);
  }
}
