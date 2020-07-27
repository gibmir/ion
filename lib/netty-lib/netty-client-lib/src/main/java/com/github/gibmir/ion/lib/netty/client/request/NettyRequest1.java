package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public class NettyRequest1<T, R> extends AbstractNettyRequest<R, NettyRequest1<T, R>>
  implements Request1<T, R> {

  public NettyRequest1(Class<R> returnType, String procedureName, JsonRpcNettySender defaultJsonRpcNettySender,
                       SocketAddress defaultSocketAddress, Jsonb defaultJsonb, Charset defaultCharset) {
    super(returnType, procedureName, defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset);
  }

  @Override
  public NettyRequest1<T, R> socketAddress(SocketAddress socketAddress) {
    return new NettyRequest1<>(returnType, procedureName, defaultJsonRpcNettySender, socketAddress, jsonb, charset);
  }

  @Override
  public NettyRequest1<T, R> jsonb(Jsonb jsonb) {
    return new NettyRequest1<>(returnType, procedureName, defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset);
  }

  @Override
  public NettyRequest1<T, R> charset(Charset charset) {
    return new NettyRequest1<>(returnType, procedureName, defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset);
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T arg) {
    return defaultJsonRpcNettySender.send(id, RequestDto.positional(id, procedureName, new Object[]{arg}), jsonb,
      charset, returnType, defaultSocketAddress);
  }

  @Override
  public void notificationCall(T arg) {
    defaultJsonRpcNettySender.send(new NotificationDto(procedureName, new Object[]{arg}), jsonb, charset,
      defaultSocketAddress);
  }
}
