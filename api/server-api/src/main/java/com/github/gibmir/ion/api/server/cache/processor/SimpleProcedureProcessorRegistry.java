package com.github.gibmir.ion.api.server.cache.processor;

import com.github.gibmir.ion.api.dto.processor.JsonRpcRequestProcessor;

import java.util.Map;

public class SimpleProcedureProcessorRegistry implements ProcedureProcessorRegistry {
  private final Map<String, JsonRpcRequestProcessor> processorMap;

  public SimpleProcedureProcessorRegistry(Map<String, JsonRpcRequestProcessor> processorMap) {
    this.processorMap = processorMap;
  }

  @Override
  public JsonRpcRequestProcessor getProcedureProcessorFor(String procedureName) {
    return processorMap.get(procedureName);
  }

  @Override
  public void register(String procedureName, JsonRpcRequestProcessor jsonRpcRequestProcessor) {
    processorMap.put(procedureName, jsonRpcRequestProcessor);
  }

  @Override
  public void unregister(String procedureName) {
    processorMap.computeIfPresent(procedureName, (k, v) -> null);
  }
}
