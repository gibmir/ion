package com.github.gibmir.ion.api.dto.response.transfer.success;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.response.transfer.AbstractJsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

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

  @Override
  public void processWith(JsonRpcResponseProcessor jsonRpcResponseProcessor) {
    jsonRpcResponseProcessor.process(this);
  }
}
