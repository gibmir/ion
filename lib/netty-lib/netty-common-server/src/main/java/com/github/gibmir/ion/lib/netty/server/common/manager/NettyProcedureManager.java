package com.github.gibmir.ion.lib.netty.server.common.manager;

import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NettyProcedureManager implements ProcedureManager {
  private static final Logger LOGGER = LoggerFactory.getLogger(NettyProcedureManager.class);
  private final ProcedureProcessorRegistry procedureProcessorRegistry;
  private final String procedureName;


  public NettyProcedureManager(final ProcedureProcessorRegistry procedureProcessorRegistry, final String procedureName) {
    this.procedureProcessorRegistry = procedureProcessorRegistry;
    this.procedureName = procedureName;
  }

  @Override
  public void close() {
    LOGGER.debug("Closing procedure [{}] processor", procedureName);
    procedureProcessorRegistry.unregister(procedureName);
  }
}
