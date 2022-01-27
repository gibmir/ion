package com.github.gibmir.ion.lib.netty.server.common.manager;

import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import com.github.gibmir.ion.api.server.processor.request.registry.ProcedureProcessorRegistry;
import org.slf4j.Logger;

public final class NettyProcedureManager implements ProcedureManager {
  private final Logger logger;
  private final ProcedureProcessorRegistry procedureProcessorRegistry;
  private final String procedureName;


  public NettyProcedureManager(final Logger logger, final ProcedureProcessorRegistry procedureProcessorRegistry,
                               final String procedureName) {
    this.logger = logger;
    this.procedureProcessorRegistry = procedureProcessorRegistry;
    this.procedureName = procedureName;
  }

  @Override
  public void close() {
    logger.info("Closing procedure [{}] processor", procedureName);
    if (procedureProcessorRegistry != null) {
      procedureProcessorRegistry.unregister(procedureName);
    }
  }
}
