package com.github.gibmir.ion.api.dto.request.transfer;

import com.github.gibmir.ion.api.dto.AbstractJsonRpcBody;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;

import javax.json.bind.annotation.JsonbProperty;

public abstract class AbstractJsonRpcRequest extends AbstractJsonRpcBody
  implements JsonRpcRequest {
  /**
   * Represents procedure name.
   */
  @JsonbProperty("method")
  private String procedureName;
  /**
   * Represents procedure params.
   */
  @JsonbProperty("params")
  private Object args;

  /**
   * Default constructor.
   */
  public AbstractJsonRpcRequest() {
    super();
  }

  /**
   * @param procedureName procedure name
   * @param args          procedure arguments
   */
  public AbstractJsonRpcRequest(final String procedureName, final Object args) {
    super();
    this.procedureName = procedureName;
    this.args = args;
  }

  /**
   * @return procedure name
   */
  public final String getProcedureName() {
    return procedureName;
  }

  /**
   * Sets procedure name.
   *
   * @param procedureName procedure name
   */
  public final void setProcedureName(final String procedureName) {
    this.procedureName = procedureName;
  }

  /**
   * @return arguments
   */
  public final Object getArgs() {
    return args;
  }

  /**
   * Sets procedure arguments.
   *
   * @param args procedure arguments
   */
  public final void setArgs(final Object args) {
    this.args = args;
  }
}
