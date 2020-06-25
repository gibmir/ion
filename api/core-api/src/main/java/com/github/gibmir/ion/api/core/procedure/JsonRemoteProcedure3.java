package com.github.gibmir.ion.api.core.procedure;

@FunctionalInterface
public interface JsonRemoteProcedure3<T1, T2, T3, R> {
  R call(T1 arg1, T2 arg2, T3 arg3);
}
