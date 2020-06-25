package com.github.gibmir.ion.api.dto.request.transfer.positional;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.dto.request.transfer.AbstractJsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

import javax.json.bind.annotation.JsonbProperty;

public class PositionalRequest extends AbstractJsonRpcRequest implements JsonRpcRequest {
  @JsonbProperty("params")
  private Object[] args;

  public PositionalRequest() {
    super();
  }

  public PositionalRequest(String id, String methodName, Object[] args) {
    super(id, methodName);
    this.args = args;
  }

  public Object[] getArgs() {
    return args;
  }

  public void setArgs(Object[] args) {
    this.args = args;
  }

  @Override
  public JsonRpcResponse processWith(JsonRpcRequestProcessor jsonRpcRequestProcessor) {
    return jsonRpcRequestProcessor.process(this);
  }
}
