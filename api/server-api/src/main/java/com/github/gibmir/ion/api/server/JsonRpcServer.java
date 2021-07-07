package com.github.gibmir.ion.api.server;

import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessorFactory;

import java.util.Collection;

public interface JsonRpcServer {
  /**
   * @return {@link ProcedureProcessor processor} factory
   */
  ProcedureProcessorFactory getProcedureProcessorFactory();

  /**
   * Register procedure processors from provided vararg.
   *
   * @param procedureProcessors processors vararg. <b>must not be {@code null}</b>
   * @return procedure manager
   */
  ProcedureManager register(ProcedureProcessor<?>... procedureProcessors);

  /**
   * Register procedure processors from provided collection.
   *
   * @param procedureProcessors processors collection. <b>must not be {@code null}</b>
   * @return procedure manager
   */
  ProcedureManager register(Collection<ProcedureProcessor<?>> procedureProcessors);
}
