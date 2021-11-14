package com.github.gibmir.ion.lib.netty.client.tcp.sender;

import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.NettyBatch;
import com.github.gibmir.ion.lib.netty.client.common.sender.JsonRpcSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.util.concurrent.FutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import java.io.Closeable;
import java.lang.reflect.Type;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public class NettyTcpJsonRpcSender implements JsonRpcSender, Closeable {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyTcpJsonRpcSender.class);
  private final ChannelPoolMap<SocketAddress, ? extends ChannelPool> nettyChannelPool;
  private final ResponseListenerRegistry responseListenerRegistry;

  public NettyTcpJsonRpcSender(final ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap,
                               final ResponseListenerRegistry responseListenerRegistry) {
    this.nettyChannelPool = channelPoolMap;
    this.responseListenerRegistry = responseListenerRegistry;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public <R> CompletableFuture<R> send(final String id, final RequestDto request, final Jsonb jsonb,
                                       final Charset charset, final Type returnType, final SocketAddress socketAddress) {
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
      sendTo(socketAddress, jsonb.toJson(request).getBytes(charset));
    } catch (Exception e) {
      responseFuture.completeExceptionally(e);
    }
    return responseFuture.thenApply(response -> (R) response);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void send(final NotificationDto request, final Jsonb jsonb, final Charset charset,
                   final SocketAddress socketAddress) {
    try {
      sendTo(socketAddress, jsonb.toJson(request).getBytes(charset));
    } catch (Exception e) {
      LOGGER.error("Exception occurred while sending notification ", e);
    }
  }

  /**
   * @throws ChannelException if exception occurred while sending request(on client side)
   * @implNote <ol>
   * <li>create future to await batch response</li>
   * <li>register futures to await result</li>
   * <li>send request dto</li>
   * <li>process response</li>
   * </ol>
   */
  @Override
  public void send(final NettyBatch nettyBatch, final Jsonb jsonb, final Charset charset,
                   final SocketAddress socketAddress) {
    for (NettyBatch.BatchPart<?> batchPart : nettyBatch.getBatchParts()) {
      ResponseFuture responseFuture = new ResponseFuture(batchPart.getId(), batchPart.getReturnType(),
        jsonb, batchPart.getResponseCallback());
      responseListenerRegistry.register(responseFuture);
    }
    sendTo(socketAddress, jsonb.toJson(nettyBatch.getBatchRequestDto()).getBytes(charset));
  }

  private void sendTo(final SocketAddress socketAddress, final byte[] payload) {
    ChannelPool simpleChannelPool = nettyChannelPool.get(socketAddress);
    simpleChannelPool.acquire().addListener((FutureListener<Channel>) channelAcquired -> {
      if (channelAcquired.isSuccess()) {
        Channel channel = channelAcquired.getNow();
        channel.writeAndFlush(payload).addListener(writeFinished -> simpleChannelPool.release(channel));
      } else {
        throw new ChannelException("Can't acquire the channel for address " + socketAddress);
      }
    });
  }

  @Override
  public void close() {

  }
}
