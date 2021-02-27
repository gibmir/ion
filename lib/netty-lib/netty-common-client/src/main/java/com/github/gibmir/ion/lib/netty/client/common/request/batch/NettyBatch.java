package com.github.gibmir.ion.lib.netty.client.common.request.batch;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;

import java.lang.reflect.Type;
import java.util.List;

public class NettyBatch {
  private final List<JsonRpcRequest> batchRequestDto;
  private final List<BatchPart<?>> batchParts;

  public NettyBatch(List<JsonRpcRequest> batchRequestDto, List<BatchPart<?>> batchParts) {
    this.batchRequestDto = batchRequestDto;
    this.batchParts = batchParts;
  }

  public List<JsonRpcRequest> getBatchRequestDto() {
    return batchRequestDto;
  }

  public List<BatchPart<?>> getBatchParts() {
    return batchParts;
  }

  public static class BatchPart<R> {
    private final String id;
    private final ResponseCallback<R> responseCallback;
    private final Type returnType;

    public BatchPart(String id, ResponseCallback<R> responseCallback, Type returnType) {
      this.id = id;
      this.responseCallback = responseCallback;
      this.returnType = returnType;
    }

    public String getId() {
      return id;
    }

    public ResponseCallback<R> getResponseCallback() {
      return responseCallback;
    }

    public Type getReturnType() {
      return returnType;
    }
  }
}
