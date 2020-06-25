package com.github.gibmir.ion.api.dto.response;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;

public interface JsonRpcResponse {
  void processWith(JsonRpcResponseProcessor jsonRpcResponseProcessor);
}
