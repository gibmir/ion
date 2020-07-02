package com.github.gibmir.ion.api.dto.request.transfer;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

import java.util.Map;

public class RequestDto extends AbstractJsonRpcRequest implements JsonRpcRequest {
  protected String id;

  public RequestDto() {
    super();
  }

  private RequestDto(String id, String methodName, Object args) {
    super(methodName, args);
    this.id = id;
  }

  public static RequestDto named(String id, String methodName, Map<String, Object> namedArgs) {
    return new RequestDto(id, methodName, namedArgs);
  }

  public static RequestDto positional(String id, String methodName, Object[] positionalArgs) {
    return new RequestDto(id, methodName, positionalArgs);
  }

  public Object getArgs() {
    return args;
  }

  public void setArgs(Object args) {
    this.args = args;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Override
  public JsonRpcResponse processWith(JsonRpcRequestProcessor jsonRpcRequestProcessor) {
    return jsonRpcRequestProcessor.process(this);
  }
}
