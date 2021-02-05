package com.github.gibmir.ion.api.server.processor;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.server.JsonRpcServer;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;

public interface ProcedureProcessor {
  static <R, P extends JsonRemoteProcedure0<R>> ProcedureProcessor from(Class<P> procedureClass, P procedureImpl) {
    return new ProcedureProcessor0<>(procedureClass, procedureImpl);
  }

  static <T, R, P extends JsonRemoteProcedure1<T, R>> ProcedureProcessor from(Class<P> procedureClass, P procedureImpl) {
    return new ProcedureProcessor1<>(procedureClass, procedureImpl);
  }

  static <T1, T2, R, P extends JsonRemoteProcedure2<T1, T2, R>> ProcedureProcessor from(Class<P> procedureClass,
                                                                                        P procedureImpl) {
    return new ProcedureProcessor2<>(procedureClass, procedureImpl);
  }

  static <T1, T2, T3, R, P extends JsonRemoteProcedure3<T1, T2, T3, R>> ProcedureProcessor from(Class<P> procedureClass,
                                                                                                P procedureImpl) {
    return new ProcedureProcessor3<>(procedureClass, procedureImpl);
  }

  ProcedureManager register(JsonRpcServer jsonRpcServer);

}
