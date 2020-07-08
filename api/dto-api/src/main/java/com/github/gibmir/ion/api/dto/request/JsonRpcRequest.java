package com.github.gibmir.ion.api.dto.request;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

public interface JsonRpcRequest {
  String getProcedureName();

  JsonRpcResponse processWith(JsonRpcRequestProcessor jsonRpcRequestProcessor);
}
