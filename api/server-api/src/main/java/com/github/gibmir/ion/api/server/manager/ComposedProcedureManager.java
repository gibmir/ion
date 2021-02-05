package com.github.gibmir.ion.api.server.manager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ComposedProcedureManager implements ProcedureManager {
  private final ProcedureManager[] procedureManagers;

  public ComposedProcedureManager(ProcedureManager[] procedureManagers) {
    this.procedureManagers = procedureManagers;
  }

  public ComposedProcedureManager(Collection<ProcedureManager> procedureManagers) {
    this.procedureManagers = procedureManagers.toArray(new ProcedureManager[0]);
  }

  @Override
  public void close() {
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
