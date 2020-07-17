package com.github.gibmir.ion.lib.netty.client.sender;

import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.sender.pool.ChannelPool;
import io.netty.channel.Channel;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public class JsonRpcNettySender {
  private final ChannelPool channelPool;
  private final ResponseListenerRegistry responseListenerRegistry;

  public JsonRpcNettySender(ChannelPool channelPool, ResponseListenerRegistry responseListenerRegistry) {
    this.channelPool = channelPool;
    this.responseListenerRegistry = responseListenerRegistry;
  }

  public <R> CompletableFuture<R> send(String id, JsonRpcRequest request, Jsonb jsonb, Charset charset,
                                       Class<R> returnType, SocketAddress socketAddress) {
    CompletableFuture<Object> responseFuture = new CompletableFuture<>();
    responseListenerRegistry.register(id, new ResponseFuture(returnType, responseFuture));
    Channel channel = channelPool.getOrCreate(jsonb, charset, socketAddress);
    try {
      channel.writeAndFlush(request).sync();
    } catch (InterruptedException e) {
      responseFuture.completeExceptionally(e);
      Thread.currentThread().interrupt();
    }
    return responseFuture.thenApply(returnType::cast);
  }

  public void sendNotification(JsonRpcRequest request, Jsonb jsonb, Charset charset, SocketAddress socketAddress) {

  }
}
