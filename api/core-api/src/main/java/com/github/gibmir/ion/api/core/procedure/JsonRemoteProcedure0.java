package com.github.gibmir.ion.api.core.procedure;

import java.util.function.Supplier;

@FunctionalInterface
public interface JsonRemoteProcedure0<R> extends Supplier<R> {
  R call();

  default R get() {
    return call();
  }
}
