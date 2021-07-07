package com.github.gibmir.ion.api.core.procedure;

@FunctionalInterface
public interface JsonRemoteProcedure3<T1, T2, T3, R> {
  /**
   * @param arg1 first argument
   * @param arg2 second argument
   * @param arg3 third argument
   * @return procedure call result
   */
  R call(T1 arg1, T2 arg2, T3 arg3);
}
