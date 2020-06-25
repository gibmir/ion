package com.github.gibmir.ion.api.core.procedure;

import java.util.function.Function;

@FunctionalInterface
public interface JsonRemoteProcedure1<T, R> extends Function<T, R> {
  R call(T arg);

  default R apply(T arg) {
    return call(arg);
  }
}
