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

  public SuccessResponse(String id, Object result) {
    super(id);
    this.result = result;
  }

  public Object getResult() {
    return result;
  }

  public void setResult(Object result) {
    this.result = result;
  }

  public static SuccessResponse createWithStringId(String id, Object result) {
    return new SuccessResponse(id, result);
  }

  public static SuccessResponse createWithNumericId(int id, Object result) {
    return new SuccessResponse(Integer.toString(id), result);
  }

  public static SuccessResponse createWithNullId(Object result) {
    return new SuccessResponse(JSON_RPC_NULL_ID, result);
  }

  @Override
  public void processWith(JsonRpcResponseProcessor jsonRpcResponseProcessor) {
    jsonRpcResponseProcessor.process(this);
  }
}
