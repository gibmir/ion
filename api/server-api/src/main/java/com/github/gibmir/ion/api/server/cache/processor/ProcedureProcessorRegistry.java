package com.github.gibmir.ion.api.server.cache.processor;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;

public interface ProcedureProcessorRegistry {
  JsonRpcRequestProcessor getProcedureProcessorFor(String procedureName);

  void register(String procedureName, JsonRpcRequestProcessor jsonRpcRequestProcessor);

  void unregister(String procedureName);
}
