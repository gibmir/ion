package com.github.gibmir.ion.api.dto.request.transfer.batch;

import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;

public class BatchRequestDto implements JsonRpcRequest {
  /**
   * batch of json-rpo requests.
   */
  private JsonRpcRequest[] jsonRpcRequests;

  /**
   * Default constructor.
   */
  public BatchRequestDto() {
  }

  /**
   * @param jsonRpcRequests batch requests
   */
  public BatchRequestDto(final JsonRpcRequest[] jsonRpcRequests) {
    this.jsonRpcRequests = jsonRpcRequests;
  }

  /**
   * @return batch requests
   */
  public final JsonRpcRequest[] getJsonRpcRequests() {
    return jsonRpcRequests;
  }

  /**
   * Sets batch requests.
   *
   * @param jsonRpcRequests batch requests
   */
  public final void setJsonRpcRequests(final JsonRpcRequest[] jsonRpcRequests) {
    this.jsonRpcRequests = jsonRpcRequests;
  }
}
