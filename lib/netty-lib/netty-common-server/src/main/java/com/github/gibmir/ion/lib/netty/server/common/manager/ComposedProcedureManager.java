package com.github.gibmir.ion.lib.netty.server.common.manager;

import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ComposedProcedureManager implements ProcedureManager {
  private final Logger logger;

  private final Collection<ProcedureManager> procedureManagers;

  public ComposedProcedureManager(Logger logger, final Collection<ProcedureManager> procedureManagers) {
    this.logger = logger;
    this.procedureManagers = procedureManagers;
  }

  @Override
  public void close() {
    logger.info("Closing multiple procedure managers {}", procedureManagers);
    List<Exception> closeExceptions = new ArrayList<>();
    for (ProcedureManager procedureManager : procedureManagers) {
      try {
        procedureManager.close();
      } catch (Exception closingException) {
        closeExceptions.add(closingException);
      }
    }
    if (/*exceptions were present*/!closeExceptions.isEmpty()) {
      IllegalStateException illegalStateException = new IllegalStateException("Exception occurred while closing procedure managers");
      for (Exception closeException : closeExceptions) {
        illegalStateException.addSuppressed(closeException);
      }
      throw illegalStateException;
    }
  }
}
