package com.github.gibmir.ion.api.core.procedure;

import java.util.function.Supplier;

//todo schema API
@FunctionalInterface
public interface JsonRemoteProcedure0<R> extends Supplier<R> {
  R call();

  default R get() {
    return call();
  }
}
