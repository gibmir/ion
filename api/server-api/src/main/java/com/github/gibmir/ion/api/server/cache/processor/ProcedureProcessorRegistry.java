package com.github.gibmir.ion.api.server.cache.processor;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;

public interface ProcedureProcessorRegistry {
  JsonRpcRequestProcessor getProcedureProcessorFor(String methodName);

  void putProcedureProcessorFor(String methodName, JsonRpcRequestProcessor jsonRpcRequestProcessor);

  void clean(String methodName);
}
