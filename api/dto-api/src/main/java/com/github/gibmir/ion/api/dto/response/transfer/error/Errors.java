package com.github.gibmir.ion.api.dto.response.transfer.error;

public enum Errors {
  // 32700 PARSE ERRORS
  INCORRECT_FORM(new JsonRpcError(32700, "parse error. not well formed")),
  UNSUPPORTED_ENCODING(new JsonRpcError(32701, "parse error. unsupported encoding")),
  INVALID_CHARACTER(new JsonRpcError(32702, "parse error. invalid character for encoding")),
  // 32600 SERVER ERRORS
  INVALID_RPC(new JsonRpcError(32600, "invalid rpc. not conforming to spec")),
  REQUEST_METHOD_NOT_FOUND(new JsonRpcError(32601, "request method not found")),
  INVALID_METHOD_PARAMETERS(new JsonRpcError(32602, "internal xml-rpc error")),
  INTERNAL_RPC_ERROR(new JsonRpcError(32603, "internal xml-rpc error")),
  // 32500
  APPLICATION_ERROR(new JsonRpcError(32500, "application error")),
  // 32400
  SYSTEM_ERROR(new JsonRpcError(32400, "system error")),
  // 32300
  TRANSPORT_ERROR(new JsonRpcError(32300, "transport error"));
  private final JsonRpcError error;

  Errors(JsonRpcError error) {
    this.error = error;
  }

  public JsonRpcError getError() {
    return error;
  }
}
