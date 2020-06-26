package com.github.gibmir.ion.api.core.request.positional.factory;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.request.positional.Request.Initializer;

public class PositionalRequestFactory {
  public static <R> JsonRemoteProcedure0<Initializer<R>> noArg(String procedureName, Class<R> resultType) {
    return () -> new Initializer<>(procedureName, resultType);
  }

  public static <R> JsonRemoteProcedure0<Initializer<R>> noArg(Class<? extends JsonRemoteProcedure0<R>> procedure, Class<R> resultType) {
    return () -> new Initializer<>(procedure.getName(), resultType);
  }

  public static <T, R> JsonRemoteProcedure1<T, Initializer<R>> singleArg(String procedureName, Class<R> resultType) {
    return (arg1) -> new Initializer<>(procedureName, resultType, arg1);
  }

  public static <T, R> JsonRemoteProcedure1<T, Initializer<R>> singleArg(Class<? extends JsonRemoteProcedure1<T, R>> procedure,
                                                                         Class<R> returnType) {
    return (arg1) -> new Initializer<>(procedure.getName(), returnType, arg1);
  }

  public static <T1, T2, R> JsonRemoteProcedure2<T1, T2, Initializer<R>> twoArg(String methodName, Class<R> resultType) {
    return (arg1, arg2) -> new Initializer<>(methodName, resultType, arg1, arg2);
  }

  public static <T1, T2, R> JsonRemoteProcedure2<T1, T2, Initializer<R>> twoArg(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedure,
                                                                                Class<R> resultType) {
    return (arg1, arg2) -> new Initializer<>(procedure.getName(), resultType, arg1, arg2);
  }

  public static <T1, T2, T3, R> JsonRemoteProcedure3<T1, T2, T3, Initializer<R>> threeArg(String methodName, Class<R> resultType) {
    return (arg1, arg2, arg3) -> new Initializer<>(methodName, resultType, arg1, arg2, arg3);
  }

  public static <T1, T2, T3, R> JsonRemoteProcedure3<T1, T2, T3, Initializer<R>> threeArg(
    Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedure, Class<R> resultType) {
    return (arg1, arg2, arg3) -> new Initializer<>(procedure.getName(), resultType, arg1.getClass(), arg2, arg3);
  }
}
