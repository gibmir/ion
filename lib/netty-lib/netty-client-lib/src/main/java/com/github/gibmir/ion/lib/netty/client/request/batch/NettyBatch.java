package com.github.gibmir.ion.lib.netty.client.request.batch;

import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;

import java.lang.reflect.Type;
import java.util.List;

public class NettyBatch {
  private final List<JsonRpcRequest> batchRequestDto;
  private final List<AwaitBatchPart> awaitBatchParts;

  public NettyBatch(List<JsonRpcRequest> batchRequestDto, List<AwaitBatchPart> awaitBatchParts) {
    this.batchRequestDto = batchRequestDto;
    this.awaitBatchParts = awaitBatchParts;
  }

  public List<JsonRpcRequest> getBatchRequestDto() {
    return batchRequestDto;
  }

  public List<AwaitBatchPart> getAwaitBatchParts() {
    return awaitBatchParts;
  }

  public static class AwaitBatchPart {
    private final String id;
    private final Type returnType;

    public AwaitBatchPart(String id, Type returnType) {
      this.id = id;
      this.returnType = returnType;
    }

    public String getId() {
      return id;
    }

    public Type getReturnType() {
      return returnType;
    }
  }
}
