package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request3;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public class NettyRequest3<T1, T2, T3, R> extends AbstractNettyRequest<R, NettyRequest3<T1, T2, T3, R>>
  implements Request3<T1, T2, T3, R> {
  public NettyRequest3(Class<R> returnType, String methodName, JsonRpcNettySender defaultJsonRpcNettySender,
                       SocketAddress defaultSocketAddress, Jsonb defaultJsonb, Charset defaultCharset) {
    super(returnType, methodName, defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset);
  }

  @Override
  public NettyRequest3<T1, T2, T3, R> socketAddress(SocketAddress socketAddress) {
    this.defaultSocketAddress = socketAddress;
    return this;
  }

  @Override
  public NettyRequest3<T1, T2, T3, R> jsonb(Jsonb jsonb) {
    this.jsonb = jsonb;
    return this;
  }

  @Override
  public NettyRequest3<T1, T2, T3, R> charset(Charset charset) {
    this.charset = charset;
    return this;
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2, T3 arg3) {
    RequestDto positional = RequestDto.positional(id, methodName, new Object[]{arg1, arg2, arg3});
    return defaultJsonRpcNettySender.send(positional, jsonb, charset, returnType, defaultSocketAddress);
  }

  @Override
  public void notificationCall(T1 arg1, T2 arg2, T3 arg3) {

  }
}
