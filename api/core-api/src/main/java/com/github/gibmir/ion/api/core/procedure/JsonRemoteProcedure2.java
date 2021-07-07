package com.github.gibmir.ion.api.core.procedure;

import java.util.function.BiFunction;

@FunctionalInterface
public interface JsonRemoteProcedure2<T1, T2, R> extends BiFunction<T1, T2, R> {
  /**
   * @param arg1 first argument
   * @param arg2 second argument
   * @return procedure call result
   */
  R call(T1 arg1, T2 arg2);

  /**
   * Implements java functional interface.
   *
   * @param t1 first argument
   * @param t2 second argument
   * @return call result
   */
  default R apply(T1 t1, T2 t2) {
    return call(t1, t2);
  }
}
