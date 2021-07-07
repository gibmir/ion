package com.github.gibmir.ion.lib.netty.server.common.processor.registry;

import com.github.gibmir.ion.api.server.processor.request.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.server.processor.request.registry.ProcedureProcessorRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public final class NettyProcedureProcessorRegistry implements ProcedureProcessorRegistry {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyProcedureProcessorRegistry.class);
  private final Map<String, JsonRpcRequestProcessor> processorMap;

  public NettyProcedureProcessorRegistry(final Map<String, JsonRpcRequestProcessor> processorMap) {
    this.processorMap = processorMap;
  }

  @Override
  public JsonRpcRequestProcessor getProcedureProcessorFor(final String procedureName) {
    return processorMap.get(procedureName);
  }


  @Override
  public void register(final String procedureName, final JsonRpcRequestProcessor jsonRpcRequestProcessor) {
    processorMap.put(procedureName, jsonRpcRequestProcessor);
  }

  @Override
  public void unregister(final String procedureName) {
    processorMap.computeIfPresent(procedureName, (k, v) -> null);
  }
}
