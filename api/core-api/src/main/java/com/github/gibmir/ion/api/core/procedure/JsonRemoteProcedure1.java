package com.github.gibmir.ion.api.core.procedure;

import java.util.function.Function;

@FunctionalInterface
public interface JsonRemoteProcedure1<T, R> extends Function<T, R> {
  /**
   * @param arg procedure argument
   * @return procedure call result
   */
  R call(T arg);

  /**
   * Implements java functional interface.
   *
   * @param arg argument
   * @return call result
   */
  default R apply(T arg) {
    return call(arg);
  }
}
