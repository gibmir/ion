package com.github.gibmir.ion.api.dto.response.transfer.error;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.AbstractJsonRpcResponse;

import javax.json.bind.annotation.JsonbProperty;

public class ErrorResponse extends AbstractJsonRpcResponse implements JsonRpcResponse {
  public static final int UNKNOWN_ERROR_CODE = -32000;
  @JsonbProperty("error")
  private JsonRpcError jsonRpcError;

  public ErrorResponse() {
    super();
  }

  public ErrorResponse(final String id, final JsonRpcError jsonRpcError) {
    super(id);
    this.jsonRpcError = jsonRpcError;
  }

  /**
   * Static factory to create error response from exception.
   *
   * @param id        response id
   * @param throwable cause
   * @return error response
   */
  public static ErrorResponse fromThrowable(final String id, final Throwable throwable) {
    return new ErrorResponse(id, new JsonRpcError(UNKNOWN_ERROR_CODE, throwable.getMessage()));
  }

  /**
   * Static factory to create error response from exception.
   *
   * @param throwable cause
   * @return error response
   */
  public static ErrorResponse withNullId(final Throwable throwable) {
    return new ErrorResponse(JSON_RPC_NULL_ID, new JsonRpcError(UNKNOWN_ERROR_CODE, throwable.toString()
      + ". Error message: " + throwable.getMessage()));
  }

  /**
   * Static factory to create error response from exception.
   *
   * @param jsonRpcError cause
   * @return error response
   */
  public static ErrorResponse withNullId(final JsonRpcError jsonRpcError) {
    return new ErrorResponse(JSON_RPC_NULL_ID, jsonRpcError);
  }

  /**
   * Static factory to create error response from exception.
   *
   * @param id           response id
   * @param jsonRpcError cause
   * @return error response
   */
  public static ErrorResponse fromJsonRpcError(final String id, final JsonRpcError jsonRpcError) {
    return new ErrorResponse(id, jsonRpcError);
  }

  /**
   * @return error
   */
  public JsonRpcError getJsonRpcError() {
    return jsonRpcError;
  }

  /**
   * Sets json rpc error.
   *
   * @param jsonRpcError error
   */
  public void setJsonRpcError(final JsonRpcError jsonRpcError) {
    this.jsonRpcError = jsonRpcError;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    String error = jsonRpcError != null ? jsonRpcError.toString() : "";
    return '{'
      + " jsonrpc: " + getJsonRpcProtocolVersion() + ','
      + " id: " + getId() + ','
      + " error: " + error
      + '}';
  }
}
