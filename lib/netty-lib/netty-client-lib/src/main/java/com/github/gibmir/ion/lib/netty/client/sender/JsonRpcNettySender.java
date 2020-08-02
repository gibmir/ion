package com.github.gibmir.ion.lib.netty.client.sender;

import com.github.gibmir.ion.api.client.batch.response.BatchResponse;
import com.github.gibmir.ion.api.client.batch.response.element.BatchElement;
import com.github.gibmir.ion.api.client.batch.response.element.error.ErrorBatchElement;
import com.github.gibmir.ion.api.client.batch.response.element.success.SuccessBatchElement;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.request.batch.NettyBatch;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.sender.pool.ChannelPool;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.bind.Jsonb;
import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JsonRpcNettySender implements Closeable {
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonRpcNettySender.class);
  private final ChannelPool channelPool;
  private final ResponseListenerRegistry responseListenerRegistry;

  public JsonRpcNettySender(ChannelPool channelPool, ResponseListenerRegistry responseListenerRegistry) {
    this.channelPool = channelPool;
    this.responseListenerRegistry = responseListenerRegistry;
  }

  @SuppressWarnings("unchecked")
  public <R> CompletableFuture<R> send(String id, RequestDto request, Jsonb jsonb, Charset charset,
                                       Type returnType, SocketAddress socketAddress) {
    CompletableFuture<Object> responseFuture = new CompletableFuture<>();
    responseListenerRegistry.register(new ResponseFuture(id, returnType, responseFuture));
    Channel channel = channelPool.getOrCreate(jsonb, charset, socketAddress);
    try {
      channel.writeAndFlush(jsonb.toJson(request).getBytes(charset)).sync();
    } catch (InterruptedException e) {
      responseFuture.completeExceptionally(e);
      Thread.currentThread().interrupt();
    }
    return responseFuture.thenApply(response -> (R) response);
  }

  /**
   * @implNote <ol>
   * <li>register futures to await result</li>
   * <li>send request dto</li>
   * <li>write response processing</li>
   * <li>create future to await batch response</li>
   * </ol>
   */
  public CompletableFuture<BatchResponse> send(NettyBatch nettyBatch, Jsonb jsonb, Charset charset,
                                               SocketAddress socketAddress) {
    for (ResponseFuture responseFuture : nettyBatch.getResponseFutures()) {
      responseListenerRegistry.register(responseFuture);
    }
    CompletableFuture<Void> batchAwaitFuture = CompletableFuture.allOf(nettyBatch.getResponseCompletableFutures());
    Channel channel = channelPool.getOrCreate(jsonb, charset, socketAddress);
    try {
      channel.writeAndFlush(jsonb.toJson(nettyBatch.getBatchRequestDto()).getBytes(charset)).sync();
    } catch (InterruptedException e) {
      batchAwaitFuture.completeExceptionally(e);
      Thread.currentThread().interrupt();
    }
    return batchAwaitFuture.thenApply(whenResponsesReceived -> handleResult(nettyBatch.getResponseFutures()));
  }

  private static BatchResponse handleResult(ResponseFuture[] responseFutures) {
    List<BatchElement> batchElements = new ArrayList<>(responseFutures.length);
    for (ResponseFuture responseFuture : responseFutures) {
      responseFuture.getFuture().whenComplete((response, throwable) -> {
        String id = responseFuture.getId();
        BatchElement batchElement;
        if (response != null) {
          batchElement = new SuccessBatchElement(id, response);
        } else if (throwable != null) {
          batchElement = new ErrorBatchElement(id, throwable);
        } else {
          batchElement = new ErrorBatchElement(id, new IllegalStateException("Result and exception are null"));
        }
        batchElements.add(batchElement);
      });
    }
    return new BatchResponse(batchElements);
  }

  public void send(NotificationDto request, Jsonb jsonb, Charset charset,
                   SocketAddress socketAddress) {
    Channel channel = channelPool.getOrCreate(jsonb, charset, socketAddress);
    try {
      channel.writeAndFlush(jsonb.toJson(request).getBytes(charset)).sync();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    } catch (Exception e) {
      LOGGER.error("Exception occurred while sending notification");
    }
  }

  @Override
  public void close() throws IOException {
    channelPool.close();
  }
}
