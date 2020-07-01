package com.github.gibmir.ion.api.dto.request.transfer;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

public class RequestDto extends AbstractJsonRpcRequest implements JsonRpcRequest {
  protected String id;

  public RequestDto() {
    super();
  }

  public RequestDto(String id, String methodName, Object[] args) {
    super(methodName, args);
    this.id = id;
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
