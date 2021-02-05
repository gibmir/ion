package com.github.gibmir.ion.api.server.processor;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.server.JsonRpcServer;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;

class ProcedureProcessor0<R, P extends JsonRemoteProcedure0<R>> implements ProcedureProcessor {
  private final Class<P> procedure;
  private final P processor;

  public ProcedureProcessor0(Class<P> procedure, P processor) {
    this.procedure = procedure;
    this.processor = processor;
  }

  @Override
  public ProcedureManager register(JsonRpcServer jsonRpcServer) {
    return jsonRpcServer.registerProcedureProcessor(procedure, processor);
  }
}
