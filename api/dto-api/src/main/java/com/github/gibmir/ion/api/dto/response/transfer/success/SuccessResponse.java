package com.github.gibmir.ion.api.dto.response.transfer.success;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.AbstractJsonRpcResponse;

import javax.json.bind.annotation.JsonbProperty;

public class SuccessResponse extends AbstractJsonRpcResponse implements JsonRpcResponse {

  @JsonbProperty("result")
  private Object result;

  public SuccessResponse() {
    super();
  }

  public SuccessResponse(final String id, final Object result) {
    super(id);
    this.result = result;
  }

  /**
   * @return response result
   */
  public Object getResult() {
    return result;
  }

  /**
   * Sets response result.
   *
   * @param result response result
   */
  public void setResult(final Object result) {
    this.result = result;
  }

  /**
   * Creates response with string id.
   *
   * @param id     response id
   * @param result response object
   * @return response
   */
  public static SuccessResponse createWithStringId(final String id, final Object result) {
    return new SuccessResponse(id, result);
  }

  /**
   * Creates response with numeric id.
   *
   * @param id     response id
   * @param result response object
   * @return response
   */
  public static SuccessResponse createWithNumericId(final int id, final Object result) {
    return new SuccessResponse(Integer.toString(id), result);
  }

  /**
   * Creates response without id.
   *
   * @param result response object
   * @return response
   */
  public static SuccessResponse createWithNullId(final Object result) {
    return new SuccessResponse(JSON_RPC_NULL_ID, result);
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
