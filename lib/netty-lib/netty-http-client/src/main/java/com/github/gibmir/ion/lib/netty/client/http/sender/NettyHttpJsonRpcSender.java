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
import io.netty.util.concurrent.Future;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NettyHttpJsonRpcSender {
  private final ChannelPoolMap<SocketAddress, SimpleChannelPool> nettyChannelPool;
  private final ResponseListenerRegistry responseListenerRegistry;
  private final Logger logger;

  public NettyHttpJsonRpcSender(final ChannelPoolMap<SocketAddress, SimpleChannelPool> nettyChannelPool,
                                final ResponseListenerRegistry responseListenerRegistry,
                                final Logger logger) {
    this.responseListenerRegistry = responseListenerRegistry;
    this.nettyChannelPool = nettyChannelPool;
    this.logger = logger;
  }

  /**
   * Sends request.
   *
   * @param id         request id
   * @param request    request dto
   * @param jsonb      serializer
   * @param charset    encoding
   * @param returnType response type
   * @param uri        server uri
   * @param <R>        return type
   * @return future result
   */
  @SuppressWarnings("unchecked")
  public <R> CompletableFuture<R> send(final String id, final RequestDto request, final Jsonb jsonb,
                                       final Charset charset, final Type returnType, final URI uri) {
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

  /**
   * Sends notification.
   *
   * @param request notification dto
   * @param jsonb   serializer
   * @param charset encoding
   * @param uri     server uri
   */
  public void send(final NotificationDto request, final Jsonb jsonb, final Charset charset, final URI uri) {
    try {
      sendTo(uri, jsonb.toJson(request).getBytes(charset));
    } catch (Exception e) {
      logger.error("Exception occurred while sending notification ", e);
    }
  }

  /**
   * Sends batch request.
   *
   * @param nettyBatch batch
   * @param jsonb      serializer
   * @param charset    encoding
   * @param uri        server uri
   */
  public void send(final NettyBatch nettyBatch, final Jsonb jsonb, final Charset charset, final URI uri) {
    List<NettyBatch.BatchPart<?>> batchParts = nettyBatch.getBatchParts();
    for (NettyBatch.BatchPart<?> batchPart : batchParts) {
      ResponseFuture responseFuture = new ResponseFuture(batchPart.getId(), batchPart.getReturnType(),
        jsonb, batchPart.getResponseCallback());
      responseListenerRegistry.register(responseFuture);
    }
    sendTo(uri, jsonb.toJson(nettyBatch.getBatchRequestDto()).getBytes(charset));
  }

  private void sendTo(final URI uri, final byte[] payload) {
    ChannelPool pool = nettyChannelPool.get(new InetSocketAddress(uri.getHost(), uri.getPort()));
    pool.acquire().addListener(channelFuture -> write(uri, payload, pool, channelFuture));
  }

  private void write(final URI uri, final byte[] payload, final ChannelPool pool, final Future<? super Channel> channelFuture) {
    if (channelFuture.isSuccess()) {
      Channel channel = (Channel) channelFuture.getNow();
      FullHttpRequest httpRequest = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST,
        uri.getRawPath());
      httpRequest.headers().set(HttpHeaderNames.HOST, uri.getHost());
      httpRequest.headers().set(HttpHeaderNames.CONTENT_LENGTH, payload.length);
      httpRequest.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
      httpRequest.content().clear().writeBytes(payload);
      channel.writeAndFlush(httpRequest).addListener(writeFinished -> pool.release(channel));
    } else {
      throw new ChannelException("Can't acquire the channel for uri " + uri);
    }
  }
}
