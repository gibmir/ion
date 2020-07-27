package com.github.gibmir.ion.api.dto.request.transfer.batch;

import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;

public class BatchRequestDto implements JsonRpcRequest {
  private JsonRpcRequest[] jsonRpcRequests;

  public BatchRequestDto() {
  }

  public BatchRequestDto(JsonRpcRequest[] jsonRpcRequests) {
    this.jsonRpcRequests = jsonRpcRequests;
  }

  public JsonRpcRequest[] getJsonRpcRequests() {
    return jsonRpcRequests;
  }

  public void setJsonRpcRequests(JsonRpcRequest[] jsonRpcRequests) {
    this.jsonRpcRequests = jsonRpcRequests;
  }
}
