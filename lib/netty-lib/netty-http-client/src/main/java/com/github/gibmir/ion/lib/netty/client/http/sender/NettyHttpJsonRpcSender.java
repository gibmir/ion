package com.github.gibmir.ion.lib.netty.client.http.sender;

import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.NettyBatch;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.concurrent.FutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NettyHttpJsonRpcSender {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyHttpJsonRpcSender.class);
  private final ChannelPoolMap<SocketAddress, SimpleChannelPool> nettyChannelPool;
  private final ResponseListenerRegistry responseListenerRegistry;

  public NettyHttpJsonRpcSender(ChannelPoolMap<SocketAddress, SimpleChannelPool> nettyChannelPool,
                                ResponseListenerRegistry responseListenerRegistry) {
    this.responseListenerRegistry = responseListenerRegistry;
    this.nettyChannelPool = nettyChannelPool;
  }

  @SuppressWarnings("unchecked")
  public <R> CompletableFuture<R> send(String id, RequestDto request, Jsonb jsonb, Charset charset,
                                       Type returnType, URI uri) {
    CompletableFuture<Object> responseFuture = new CompletableFuture<>();
    try {
      responseListenerRegistry.register(new ResponseFuture(id, returnType, jsonb,
        /*response callback*/(response, throwable) -> {
        if (throwable != null) {
          responseFuture.completeExceptionally(throwable);
        } else {
          responseFuture.complete(response);
        }
      }));
      sendTo(uri, jsonb.toJson(request).getBytes(charset));
    } catch (Exception e) {
      responseFuture.completeExceptionally(e);
    }
    return responseFuture.thenApply(response -> (R) response);
  }

  public void send(NotificationDto request, Jsonb jsonb, Charset charset, URI uri) {
    try {
      sendTo(uri, jsonb.toJson(request).getBytes(charset));
    } catch (Exception e) {
      LOGGER.error("Exception occurred while sending notification ", e);
    }
  }

  public void send(NettyBatch nettyBatch, Jsonb jsonb, Charset charset, URI uri) {
    List<NettyBatch.BatchPart<?>> batchParts = nettyBatch.getBatchParts();
    for (NettyBatch.BatchPart<?> batchPart : batchParts) {
      ResponseFuture responseFuture = new ResponseFuture(batchPart.getId(), batchPart.getReturnType(),
        jsonb, batchPart.getResponseCallback());
      responseListenerRegistry.register(responseFuture);
    }
    sendTo(uri, jsonb.toJson(nettyBatch.getBatchRequestDto()).getBytes(charset));
  }

  private void sendTo(URI uri, byte[] payload) {
    InetSocketAddress socketAddress = new InetSocketAddress(uri.getHost(), uri.getPort());
    ChannelPool simpleChannelPool = nettyChannelPool.get(socketAddress);
    simpleChannelPool.acquire().addListener((FutureListener<Channel>) acquiredFuture -> {
      if (acquiredFuture.isSuccess()) {
        Channel channel = acquiredFuture.getNow();
        FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
          uri.getRawPath());
        httpRequest.headers().set(HttpHeaderNames.HOST, uri.getHost());
        httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, payload.length);
        httpRequest.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
        httpRequest.content().clear().writeBytes(payload);
        channel.writeAndFlush(httpRequest).addListener(writeFinished -> simpleChannelPool.release(channel));
      } else {
        throw new ChannelException("Can't acquire the channel for uri " + uri);
      }
    });
  }
}
