package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.Request0;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;
//todo write other requests. write factory. write provider
public class NettyRequest0<R> extends AbstractNettyRequest<R, NettyRequest0<R>>
  implements Request0<R> {
  public static final Object[] EMPTY_PAYLOAD = new Object[0];


  public NettyRequest0(Class<R> returnType, String methodName, JsonRpcNettySender defaultJsonRpcNettySender,
                       SocketAddress defaultSocketAddress, Jsonb defaultJsonb, Charset defaultCharset) {
    super(returnType, methodName, defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset);
  }

  @Override
  public CompletableFuture<R> call(String id) {
    RequestDto positional = RequestDto.positional(id, methodName, EMPTY_PAYLOAD);
    return defaultJsonRpcNettySender.send(positional, jsonb, charset, returnType, defaultSocketAddress);
  }

  @Override
  public void notificationCall() {

  }

  @Override
  public NettyRequest0<R> socketAddress(SocketAddress socketAddress) {
    this.defaultSocketAddress = socketAddress;
    return this;
  }

  @Override
  public NettyRequest0<R> jsonb(Jsonb jsonb) {
    this.jsonb = jsonb;
    return this;
  }

  @Override
  public NettyRequest0<R> charset(Charset charset) {
    this.charset = charset;
    return this;
  }
}