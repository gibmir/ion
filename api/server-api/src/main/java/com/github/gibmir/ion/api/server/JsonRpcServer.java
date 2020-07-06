package com.github.gibmir.ion.api.server;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;

public interface JsonRpcServer {
  <R, P extends JsonRemoteProcedure0<R>> ProcedureManager registerProcedureProcessor(
    Class<? extends JsonRemoteProcedure0<R>> procedureClass, P procedureImpl);

  <T, R, P extends JsonRemoteProcedure1<T, R>> ProcedureManager registerProcedureProcessor(
    Class<? extends JsonRemoteProcedure1<T, R>> procedureClass, P procedureImpl);

  <T1, T2, R, P extends JsonRemoteProcedure2<T1, T2, R>> ProcedureManager registerProcedureProcessor(
    Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedureClass, P procedureImpl);

  <T1, T2, T3, R, P extends JsonRemoteProcedure3<T1, T2, T3, R>> ProcedureManager registerProcedureProcessor(
    Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedureClass, P procedureImpl);
}
