package com.github.gibmir.ion.api.dto.response.transfer;

import com.github.gibmir.ion.api.dto.AbstractJsonRpcBody;

public abstract class AbstractJsonRpcResponse extends AbstractJsonRpcBody {
  protected String id;

  public AbstractJsonRpcResponse() {
    super();
  }

  public AbstractJsonRpcResponse(String id) {
    super();
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
