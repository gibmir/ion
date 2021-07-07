package com.github.gibmir.ion.api.dto.response.transfer;

import com.github.gibmir.ion.api.dto.AbstractJsonRpcBody;

public abstract class AbstractJsonRpcResponse extends AbstractJsonRpcBody {
  public static final String JSON_RPC_NULL_ID = "null";
  private String id;

  public AbstractJsonRpcResponse() {
    super();
  }

  public AbstractJsonRpcResponse(final String id) {
    super();
    this.id = id;
  }

  /**
   * @return response id
   */
  public String getId() {
    return id;
  }

  /**
   * Sets response id.
   *
   * @param id response id
   */
  public void setId(final String id) {
    this.id = id;
  }
}
