package com.github.gibmir.ion.api.server.processor;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;

public interface ProcedureProcessorFactory {
  /**
   * Creates processor by specified arguments.
   *
   * @param procedureClass API class
   * @param procedureImpl  API impl
   * @param <R>            return type
   * @param <P>            procedure type
   * @return processor
   */
  <R, P extends JsonRemoteProcedure0<R>> ProcedureProcessor<P> create(Class<P> procedureClass, P procedureImpl);

  /**
   * Creates processor by specified arguments.
   *
   * @param procedureClass API class
   * @param procedureImpl  API impl
   * @param <T>            first argument type
   * @param <R>            return type
   * @param <P>            procedure type
   * @return processor
   */
  <T, R, P extends JsonRemoteProcedure1<T, R>> ProcedureProcessor<P> create(Class<P> procedureClass,
                                                                            P procedureImpl);

  /**
   * Creates processor by specified arguments.
   *
   * @param procedureClass API class
   * @param procedureImpl  API impl
   * @param <T1>           first argument type
   * @param <T2>           second argument type
   * @param <R>            return type
   * @param <P>            procedure type
   * @return processor
   */
  <T1, T2, R, P extends JsonRemoteProcedure2<T1, T2, R>> ProcedureProcessor<P> create(Class<P> procedureClass,
                                                                                      P procedureImpl);

  /**
   * Creates processor by specified arguments.
   *
   * @param procedureClass API class
   * @param procedureImpl  API impl
   * @param <T1>           first argument type
   * @param <T2>           second argument type
   * @param <T3>           third argument type
   * @param <R>            return type
   * @param <P>            procedure type
   * @return processor
   */
  <T1, T2, T3, R, P extends JsonRemoteProcedure3<T1, T2, T3, R>> ProcedureProcessor<P> create(
    Class<P> procedureClass, P procedureImpl);
}
