package com.github.gibmir.ion.api.dto.response.transfer.batch;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

public class BatchResponseDto implements JsonRpcResponse {
  private JsonRpcResponse[] jsonRpcResponses;

  public BatchResponseDto() {
  }

  public BatchResponseDto(JsonRpcResponse[] jsonRpcResponses) {
    this.jsonRpcResponses = jsonRpcResponses;
  }

  public JsonRpcResponse[] getJsonRpcResponses() {
    return jsonRpcResponses;
  }

  public void setJsonRpcResponses(JsonRpcResponse[] jsonRpcResponses) {
    this.jsonRpcResponses = jsonRpcResponses;
  }

  @Override
  public void processWith(JsonRpcResponseProcessor jsonRpcResponseProcessor) {
    jsonRpcResponseProcessor.process(this);
  }
}
