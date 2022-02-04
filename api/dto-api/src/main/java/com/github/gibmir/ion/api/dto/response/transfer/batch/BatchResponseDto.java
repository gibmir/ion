package com.github.gibmir.ion.api.dto.response.transfer.batch;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

public class BatchResponseDto implements JsonRpcResponse {
  private JsonRpcResponse[] jsonRpcResponses;

  public BatchResponseDto() {
    this.jsonRpcResponses = new JsonRpcResponse[]{};
  }

  public BatchResponseDto(final JsonRpcResponse[] jsonRpcResponses) {
    this.jsonRpcResponses = jsonRpcResponses;
  }

  /**
   * @return batch responses
   */
  public final JsonRpcResponse[] getJsonRpcResponses() {
    return jsonRpcResponses;
  }

  /**
   * @param jsonRpcResponses batch responses
   */
  public void setJsonRpcResponses(final JsonRpcResponse[] jsonRpcResponses) {
    this.jsonRpcResponses = jsonRpcResponses;
  }

  /**
   * {@inheritDoc}
   *
   * @param jsonRpcResponseProcessor processor to process response
   */
  @Override
  public void processWith(final JsonRpcResponseProcessor jsonRpcResponseProcessor) {
    jsonRpcResponseProcessor.process(this);
  }
}
