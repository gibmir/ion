package com.github.gibmir.ion.api.dto;

import javax.json.bind.annotation.JsonbProperty;

public abstract class AbstractJsonRpcBody {
  public static final String JSON_RPC_PROTOCOL_VERSION = "2.0";
  @JsonbProperty("jsonrpc")
  protected String jsonRpcProtocolVersion;

  public AbstractJsonRpcBody() {
    jsonRpcProtocolVersion = JSON_RPC_PROTOCOL_VERSION;
  }

  public String getJsonRpcProtocolVersion() {
    return jsonRpcProtocolVersion;
  }
}
