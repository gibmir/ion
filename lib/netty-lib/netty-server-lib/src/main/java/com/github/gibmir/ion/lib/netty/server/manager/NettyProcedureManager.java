package com.github.gibmir.ion.lib.netty.server.manager;

import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;

public class NettyProcedureManager implements ProcedureManager {
  private final ProcedureProcessorRegistry procedureProcessorRegistry;
  private final String procedureName;


  public NettyProcedureManager(ProcedureProcessorRegistry procedureProcessorRegistry, String procedureName) {
    this.procedureProcessorRegistry = procedureProcessorRegistry;
    this.procedureName = procedureName;
  }

  @Override
  public void close() {
    procedureProcessorRegistry.unregister(procedureName);
  }

  @Override
  public String getProcedureName() {
    return procedureName;
  }
}
