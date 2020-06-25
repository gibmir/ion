package com.github.gibmir.ion.api.dto.request.transfer;

import com.github.gibmir.ion.api.dto.AbstractJsonRpcBody;

import javax.json.bind.annotation.JsonbProperty;

public abstract class AbstractJsonRpcRequest extends AbstractJsonRpcBody {
  protected String id;
  @JsonbProperty("method")
  protected String methodName;

  public AbstractJsonRpcRequest() {
    super();
  }

  public AbstractJsonRpcRequest(String id, String methodName) {
    super();
    this.id = id;
    this.methodName = methodName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }
}
