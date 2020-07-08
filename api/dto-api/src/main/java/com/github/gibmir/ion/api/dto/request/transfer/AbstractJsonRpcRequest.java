package com.github.gibmir.ion.api.dto.request.transfer;

import com.github.gibmir.ion.api.dto.AbstractJsonRpcBody;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;

import javax.json.bind.annotation.JsonbProperty;

public abstract class AbstractJsonRpcRequest extends AbstractJsonRpcBody implements JsonRpcRequest {

  @JsonbProperty("method")
  protected String procedureName;
  @JsonbProperty("params")
  protected Object args;

  public AbstractJsonRpcRequest() {
    super();
  }

  public AbstractJsonRpcRequest(String procedureName, Object args) {
    super();
    this.procedureName = procedureName;
    this.args = args;
  }

  public String getProcedureName() {
    return procedureName;
  }

  public void setProcedureName(String procedureName) {
    this.procedureName = procedureName;
  }

  public Object getArgs() {
    return args;
  }

  public void setArgs(Object args) {
    this.args = args;
  }
}
