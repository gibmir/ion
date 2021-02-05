package com.github.gibmir.ion.api.server;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;

import java.util.Collection;

public interface JsonRpcServer {

  <R, P extends JsonRemoteProcedure0<R>> ProcedureManager registerProcedureProcessor(
    Class<P> procedureClass, P procedureImpl);

  <T, R, P extends JsonRemoteProcedure1<T, R>> ProcedureManager registerProcedureProcessor(
    Class<P> procedureClass, P procedureImpl);

  <T1, T2, R, P extends JsonRemoteProcedure2<T1, T2, R>> ProcedureManager registerProcedureProcessor(
    Class<P> procedureClass, P procedureImpl);

  <T1, T2, T3, R, P extends JsonRemoteProcedure3<T1, T2, T3, R>> ProcedureManager registerProcedureProcessor(
    Class<P> procedureClass, P procedureImpl);

  /**
   * Register procedure processors from provided vararg.
   *
   * @param procedureProcessors processors vararg. <b>must not be {@code null}</b>
   */
  ProcedureManager register(ProcedureProcessor... procedureProcessors);

  /**
   * Register procedure processors from provided collection.
   *
   * @param procedureProcessors processors collection. <b>must not be {@code null}</b>
   */
  ProcedureManager register(Collection<ProcedureProcessor> procedureProcessors);
}
