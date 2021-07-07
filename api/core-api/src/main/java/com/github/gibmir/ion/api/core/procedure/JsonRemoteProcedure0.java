package com.github.gibmir.ion.api.core.procedure;

import java.util.function.Supplier;

@FunctionalInterface
public interface JsonRemoteProcedure0<R> extends Supplier<R> {
  /**
   * @return procedure call result
   */
  R call();

  /**
   * Implements java functional interface.
   *
   * @return call result
   */
  default R get() {
    return call();
  }
}
