package com.github.gibmir.ion.api.server.processor.request.registry;

import com.github.gibmir.ion.api.server.processor.request.JsonRpcRequestProcessor;

public interface ProcedureProcessorRegistry {
  /**
   * Provides procedure processor by specified name.
   *
   * @param procedureName procedure name
   * @return processor
   */
  JsonRpcRequestProcessor getProcedureProcessorFor(String procedureName);

  /**
   * Registers processor with specified name.
   *
   * @param procedureName           procedure name
   * @param jsonRpcRequestProcessor request processor
   */
  void register(String procedureName, JsonRpcRequestProcessor jsonRpcRequestProcessor);

  /**
   * Unregisters processor with specified name.
   *
   * @param procedureName procedure name
   */
  void unregister(String procedureName);
}
