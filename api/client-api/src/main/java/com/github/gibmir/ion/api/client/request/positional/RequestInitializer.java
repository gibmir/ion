package com.github.gibmir.ion.api.client.request.positional;

import com.github.gibmir.ion.api.client.request.id.JsonRpcRequestIdSupplier;
import com.github.gibmir.ion.api.client.sender.JsonRpcRequestSender;

public class RequestInitializer<R> {
  private final String procedureName;
  private final Class<R> returnType;
  private final Object[] args;

  public RequestInitializer(String procedureName, Class<R> returnType, Object... args) {
    this.procedureName = procedureName;
    this.returnType = returnType;
    this.args = args;
  }

  public Request<R> initialize(JsonRpcRequestIdSupplier jsonRpcRequestIdSupplier, JsonRpcRequestSender jsonRpcRequestSender) {
    return new Request<>(procedureName, returnType, jsonRpcRequestIdSupplier, jsonRpcRequestSender, args);
  }
}
