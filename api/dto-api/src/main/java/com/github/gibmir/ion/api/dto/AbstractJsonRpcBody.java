package com.github.gibmir.ion.api.dto;

import javax.json.bind.annotation.JsonbProperty;

public abstract class AbstractJsonRpcBody {
  /**
   * Represents json-rpc 2.0.
   */
  public static final String JSON_RPC_PROTOCOL_VERSION = "2.0";
  /**
   * Represents json-rpc protocol version.
   */
  @JsonbProperty("jsonrpc")
  private String jsonRpcProtocolVersion;

  /**
   * Default constructor.
   */
  public AbstractJsonRpcBody() {
    jsonRpcProtocolVersion = JSON_RPC_PROTOCOL_VERSION;
  }

  /**
   * @return json rpc protocol version
   */
  public final String getJsonRpcProtocolVersion() {
    return jsonRpcProtocolVersion;
  }
}
