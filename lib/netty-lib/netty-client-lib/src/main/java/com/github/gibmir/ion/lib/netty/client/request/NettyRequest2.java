package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request2;
import com.github.gibmir.ion.api.dto.request.transfer.NotificationDto;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public class NettyRequest2<T1, T2, R> extends AbstractNettyRequest<R, NettyRequest2<T1, T2, R>>
  implements Request2<T1, T2, R> {

  public NettyRequest2(Class<R> returnType, String methodName, JsonRpcNettySender defaultJsonRpcNettySender,
                       SocketAddress defaultSocketAddress, Jsonb defaultJsonb, Charset defaultCharset) {
    super(returnType, methodName, defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset);
  }

  @Override
  public NettyRequest2<T1, T2, R> socketAddress(SocketAddress socketAddress) {
    return new NettyRequest2<>(returnType, procedureName, defaultJsonRpcNettySender, socketAddress, jsonb,
      charset);
  }

  @Override
  public NettyRequest2<T1, T2, R> jsonb(Jsonb jsonb) {
    return new NettyRequest2<>(returnType, procedureName, defaultJsonRpcNettySender, defaultSocketAddress, jsonb,
      charset);
  }

  @Override
  public NettyRequest2<T1, T2, R> charset(Charset charset) {
    return new NettyRequest2<>(returnType, procedureName, defaultJsonRpcNettySender, defaultSocketAddress, jsonb,
      charset);
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2) {
    return defaultJsonRpcNettySender.send(id, RequestDto.positional(id, procedureName, new Object[]{arg1, arg2}),
      jsonb, charset, returnType, defaultSocketAddress);
  }

  @Override
  public void notificationCall(T1 arg1, T2 arg2) {
    defaultJsonRpcNettySender.sendNotification(new NotificationDto(procedureName, new Object[]{arg1, arg2}), jsonb,
      charset, defaultSocketAddress);
  }
}
