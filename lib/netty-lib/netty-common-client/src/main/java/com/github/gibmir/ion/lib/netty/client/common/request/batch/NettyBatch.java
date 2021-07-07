package com.github.gibmir.ion.lib.netty.client.common.request.batch;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;

import java.lang.reflect.Type;
import java.util.List;

public final class NettyBatch {
  private final List<JsonRpcRequest> batchRequestDto;
  private final List<BatchPart<?>> batchParts;

  public NettyBatch(final List<JsonRpcRequest> batchRequestDto, final List<BatchPart<?>> batchParts) {
    this.batchRequestDto = batchRequestDto;
    this.batchParts = batchParts;
  }

  /**
   * @return batch requests
   */
  public List<JsonRpcRequest> getBatchRequestDto() {
    return batchRequestDto;
  }

  /**
   * @return batch parts
   */
  public List<BatchPart<?>> getBatchParts() {
    return batchParts;
  }

  public static class BatchPart<R> {
    private final String id;
    private final ResponseCallback<R> responseCallback;
    private final Type returnType;

    public BatchPart(final String id, final ResponseCallback<R> responseCallback, final Type returnType) {
      this.id = id;
      this.responseCallback = responseCallback;
      this.returnType = returnType;
    }

    /**
     * @return batch part id
     */
    public String getId() {
      return id;
    }

    /**
     * @return batch part callback
     */
    public ResponseCallback<R> getResponseCallback() {
      return responseCallback;
    }

    /**
     * @return batch part return type
     */
    public Type getReturnType() {
      return returnType;
    }
  }
}
