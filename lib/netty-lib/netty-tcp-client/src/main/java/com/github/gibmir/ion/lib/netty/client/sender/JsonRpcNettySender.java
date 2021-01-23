package com.github.gibmir.ion.lib.netty.client.sender;

import com.github.gibmir.ion.api.client.batch.response.BatchResponse;
import com.github.gibmir.ion.api.client.batch.response.element.BatchElement;
import com.github.gibmir.ion.api.client.batch.response.element.error.ErrorBatchElement;
import com.github.gibmir.ion.api.client.batch.response.element.success.SuccessBatchElement;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.request.batch.NettyBatch;
import com.github.gibmir.ion.lib.netty.client.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.channel.handler.response.registry.ResponseListenerRegistry;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JsonRpcNettySender implements Closeable {
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonRpcNettySender.class);
  private final ChannelPoolMap<SocketAddress, ? extends ChannelPool> nettyChannelPool;
  private final ResponseListenerRegistry responseListenerRegistry;

  public JsonRpcNettySender(ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap,
                            ResponseListenerRegistry responseListenerRegistry) {
    this.nettyChannelPool = channelPoolMap;
    this.responseListenerRegistry = responseListenerRegistry;
  }

  @SuppressWarnings("unchecked")
  public <R> CompletableFuture<R> send(String id, RequestDto request, Jsonb jsonb, Charset charset,
                                       Type returnType, SocketAddress socketAddress) {
    CompletableFuture<Object> responseFuture = new CompletableFuture<>();
    try {
      responseListenerRegistry.register(new ResponseFuture(id, returnType, responseFuture, jsonb));
      sendTo(socketAddress, jsonb.toJson(request).getBytes(charset));
    } catch (Exception e) {
      responseFuture.completeExceptionally(e);
    }
    return responseFuture.thenApply(response -> (R) response);
  }

  /**
   * @throws JsonRpcProcessingException if exception occurred while sending request(on client side)
   * @implNote <ol>
   * <li>create future to await batch response</li>
   * <li>register futures to await result</li>
   * <li>send request dto</li>
   * <li>process response</li>
   * </ol>
   */
  @SuppressWarnings("unchecked")
  public CompletableFuture<BatchResponse> send(NettyBatch nettyBatch, Jsonb jsonb, Charset charset,
                                               SocketAddress socketAddress) {
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = nettyBatch.getAwaitBatchParts();
    int size = awaitBatchParts.size();
    CompletableFuture<BatchElement>[] futureBatchElements = new CompletableFuture[size];
    for (int i = 0; i < size; i++) {
      CompletableFuture<Object> futureBatchElement = new CompletableFuture<>();
      NettyBatch.AwaitBatchPart awaitBatchPart = awaitBatchParts.get(i);
      ResponseFuture responseFuture = new ResponseFuture(awaitBatchPart.getId(), awaitBatchPart.getReturnType(),
        futureBatchElement, jsonb);
      responseListenerRegistry.register(responseFuture);
      futureBatchElements[i] = futureBatchElement
        .handle((response, throwable) -> JsonRpcNettySender.toBatchElement(responseFuture, response, throwable));
    }

    CompletableFuture<Void> batchAwaitFuture = CompletableFuture.allOf(futureBatchElements);
    try {
      sendTo(socketAddress, jsonb.toJson(nettyBatch.getBatchRequestDto()).getBytes(charset));
    } catch (Exception e) {
      LOGGER.error("Exception occurred white sending a request", e);
      batchAwaitFuture.completeExceptionally(e);
    }
    return batchAwaitFuture.thenApply(whenResponsesReceived -> handleResult(futureBatchElements));
  }

  private static BatchResponse handleResult(CompletableFuture<BatchElement>[] futureBatchElements) {
    List<BatchElement> batchElements = new ArrayList<>(futureBatchElements.length);
    for (CompletableFuture<BatchElement> futureBatchElement : futureBatchElements) {
      batchElements.add(/*already completed*/futureBatchElement.join());
    }
    return new BatchResponse(batchElements);
  }

  private static BatchElement toBatchElement(ResponseFuture responseFuture, Object response, Throwable throwable) {
    String id = responseFuture.getId();
    if (throwable != null) {
      return new ErrorBatchElement(id, throwable);
    }
    return new SuccessBatchElement(id, response);
  }

  public void send(NotificationDto request, Jsonb jsonb, Charset charset,
                   SocketAddress socketAddress) {
    try {
      sendTo(socketAddress, jsonb.toJson(request).getBytes(charset));
    } catch (Exception e) {
      LOGGER.error("Exception occurred while sending notification");
    }
  }

  private void sendTo(SocketAddress socketAddress, byte[] payload) {
    ChannelPool simpleChannelPool = nettyChannelPool.get(socketAddress);
    simpleChannelPool.acquire().addListener((FutureListener<Channel>) acquiredFuture -> {
      if (acquiredFuture.isSuccess()) {
        Channel channel = acquiredFuture.getNow();
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
