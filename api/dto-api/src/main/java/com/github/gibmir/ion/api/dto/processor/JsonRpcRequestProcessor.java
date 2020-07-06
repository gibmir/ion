package com.github.gibmir.ion.api.dto.processor;

import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;

public interface JsonRpcRequestProcessor {
  JsonRpcResponse process(JsonRpcRequest requestDto);
}
