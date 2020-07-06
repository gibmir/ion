package com.github.gibmir.ion.api.server.cache.processor;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;

import java.util.Map;

public class SimpleProcedureProcessorRegistry implements ProcedureProcessorRegistry {
  private final Map<String, JsonRpcRequestProcessor> processorMap;

  public SimpleProcedureProcessorRegistry(Map<String, JsonRpcRequestProcessor> processorMap) {
    this.processorMap = processorMap;
  }

  @Override
  public JsonRpcRequestProcessor getProcedureProcessorFor(String methodName) {
    return processorMap.get(methodName);
  }

  @Override
  public void putProcedureProcessorFor(String methodName, JsonRpcRequestProcessor jsonRpcRequestProcessor) {
    processorMap.put(methodName, jsonRpcRequestProcessor);
  }

  @Override
  public void clean(String methodName) {
    processorMap.computeIfPresent(methodName, (k, v) -> null);
  }
}
