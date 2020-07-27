package com.github.gibmir.ion.lib.netty.client.request.batch;

import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.future.ResponseFuture;

import java.util.concurrent.CompletableFuture;

public class NettyBatch {
  private final JsonRpcRequest[] batchRequestDto;
  private final ResponseFuture[] responseFutures;
  private final CompletableFuture<?>[] responseCompletableFutures;

  public NettyBatch(JsonRpcRequest[] batchRequestDto, ResponseFuture[] responseFutures,
                    CompletableFuture<?>[] responseCompletableFutures) {
    this.batchRequestDto = batchRequestDto;
    this.responseFutures = responseFutures;
    this.responseCompletableFutures = responseCompletableFutures;
  }

  public JsonRpcRequest[] getBatchRequestDto() {
    return batchRequestDto;
  }

  public ResponseFuture[] getResponseFutures() {
    return responseFutures;
  }

  public CompletableFuture<?>[] getResponseCompletableFutures() {
    return responseCompletableFutures;
  }
}
