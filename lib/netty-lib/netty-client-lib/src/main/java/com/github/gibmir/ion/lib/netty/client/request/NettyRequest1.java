package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public class NettyRequest1<T, R> extends AbstractNettyRequest<R, NettyRequest1<T, R>>
  implements Request1<T, R> {

  public NettyRequest1(Class<R> returnType, String methodName, JsonRpcNettySender defaultJsonRpcNettySender,
                       SocketAddress defaultSocketAddress, Jsonb defaultJsonb, Charset defaultCharset) {
    super(returnType, methodName, defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset);
  }

  @Override
  public NettyRequest1<T, R> socketAddress(SocketAddress socketAddress) {
    this.defaultSocketAddress = socketAddress;
    return this;
  }

  @Override
  public NettyRequest1<T, R> jsonb(Jsonb jsonb) {
    this.jsonb = jsonb;
    return this;
  }

  @Override
  public NettyRequest1<T, R> charset(Charset charset) {
    this.charset = charset;
    return this;
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T arg) {
    RequestDto positional = RequestDto.positional(id, methodName, new Object[]{arg});
    return defaultJsonRpcNettySender.send(positional, jsonb, charset, returnType, defaultSocketAddress);
  }

  @Override
  public void notificationCall(T arg) {

  }
}
