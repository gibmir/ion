package com.github.gibmir.ion.api.dto.response.transfer.error;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.AbstractJsonRpcResponse;

import javax.json.bind.annotation.JsonbProperty;

public class ErrorResponse extends AbstractJsonRpcResponse implements JsonRpcResponse {
  @JsonbProperty("error")
  private JsonRpcError jsonRpcError;

  public ErrorResponse() {
    super();
  }

  public ErrorResponse(String id, JsonRpcError jsonRpcError) {
    super(id);
    this.jsonRpcError = jsonRpcError;
  }

  public static ErrorResponse fromThrowable(String id, Throwable throwable) {
    return new ErrorResponse(id, new JsonRpcError(-32000, throwable.getMessage()));
  }

  public static ErrorResponse withNullId(Throwable throwable) {
    return new ErrorResponse(JSON_RPC_NULL_ID, new JsonRpcError(-32000, throwable.toString() +
      ". Error message: "
      + throwable.getMessage()));
  }

  public static ErrorResponse withJsonRpcError(String id, JsonRpcError jsonRpcError) {
    return new ErrorResponse(id, jsonRpcError);
  }

  public JsonRpcError getJsonRpcError() {
    return jsonRpcError;
  }

  public void setJsonRpcError(JsonRpcError jsonRpcError) {
    this.jsonRpcError = jsonRpcError;
  }

  @Override
  public void processWith(JsonRpcResponseProcessor jsonRpcResponseProcessor) {
    jsonRpcResponseProcessor.process(this);
  }

  @Override
  public String toString() {
    String error = jsonRpcError != null ? jsonRpcError.toString() : "";
    return '{' +
      " jsonrpc: " + jsonRpcProtocolVersion + ',' +
      " id: " + id + ',' +
      " error: " + error +
      '}';
  }
}
