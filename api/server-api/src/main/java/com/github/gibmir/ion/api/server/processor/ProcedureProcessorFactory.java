package com.github.gibmir.ion.api.server.processor;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;

public interface ProcedureProcessorFactory {

  <R, P extends JsonRemoteProcedure0<R>> ProcedureProcessor<P> create(Class<P> procedureClass, P procedureImpl);

  <T, R, P extends JsonRemoteProcedure1<T, R>> ProcedureProcessor<P> create(Class<P> procedureClass,
                                                                            P procedureImpl);

  <T1, T2, R, P extends JsonRemoteProcedure2<T1, T2, R>> ProcedureProcessor<P> create(Class<P> procedureClass,
                                                                                      P procedureImpl);

  <T1, T2, T3, R, P extends JsonRemoteProcedure3<T1, T2, T3, R>> ProcedureProcessor<P> create(
    Class<P> procedureClass, P procedureImpl);
}
