package com.github.gibmir.ion.api.dto.request.transfer;

import com.github.gibmir.ion.api.dto.AbstractJsonRpcBody;

import javax.json.bind.annotation.JsonbProperty;

public abstract class AbstractJsonRpcRequest extends AbstractJsonRpcBody {

  @JsonbProperty("method")
  protected String methodName;
  @JsonbProperty("params")
  protected Object[] args;

  public AbstractJsonRpcRequest() {
    super();
  }

  public AbstractJsonRpcRequest(String methodName, Object[] args) {
    super();
    this.methodName = methodName;
    this.args = args;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public Object[] getArgs() {
    return args;
  }

  public void setArgs(Object[] args) {
    this.args = args;
  }
}
