package com.github.gibmir.ion.api.core.procedure;

import java.util.function.BiFunction;

@FunctionalInterface
public interface JsonRemoteProcedure2<T1, T2, R> extends BiFunction<T1, T2, R> {
  R call(T1 arg1, T2 arg2);

  default R apply(T1 t1, T2 t2) {
    return call(t1, t2);
  }
}
