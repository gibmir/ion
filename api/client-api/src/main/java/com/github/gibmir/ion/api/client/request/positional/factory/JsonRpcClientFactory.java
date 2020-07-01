package com.github.gibmir.ion.api.client.request.positional.factory;

import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.client.request.positional.RequestInitializer;

public class JsonRpcClientFactory {
  public static <R> JsonRemoteProcedure0<RequestInitializer<R>> noArg(String procedureName, Class<R> resultType) {
    return () -> new RequestInitializer<>(procedureName, resultType);
  }

  public static <R> JsonRemoteProcedure0<RequestInitializer<R>> noArg(Class<? extends JsonRemoteProcedure0<R>> procedure, Class<R> resultType) {
    return () -> new RequestInitializer<>(procedure.getName(), resultType);
  }

  public static <T, R> JsonRemoteProcedure1<T, RequestInitializer<R>> singleArg(String procedureName, Class<R> resultType) {
    return (arg1) -> new RequestInitializer<>(procedureName, resultType, arg1);
  }

  public static <T, R> JsonRemoteProcedure1<T, RequestInitializer<R>> singleArg(Class<? extends JsonRemoteProcedure1<T, R>> procedure,
                                                                                Class<R> returnType) {
    return (arg1) -> new RequestInitializer<>(procedure.getName(), returnType, arg1);
  }

  public static <T1, T2, R> JsonRemoteProcedure2<T1, T2, RequestInitializer<R>> twoArg(String methodName, Class<R> resultType) {
    return (arg1, arg2) -> new RequestInitializer<>(methodName, resultType, arg1, arg2);
  }

  public static <T1, T2, R> JsonRemoteProcedure2<T1, T2, RequestInitializer<R>> twoArg(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedure,
                                                                                       Class<R> resultType) {
    return (arg1, arg2) -> new RequestInitializer<>(procedure.getName(), resultType, arg1, arg2);
  }

  public static <T1, T2, T3, R> JsonRemoteProcedure3<T1, T2, T3, RequestInitializer<R>> threeArg(String methodName, Class<R> resultType) {
    return (arg1, arg2, arg3) -> new RequestInitializer<>(methodName, resultType, arg1, arg2, arg3);
  }

  public static <T1, T2, T3, R> JsonRemoteProcedure3<T1, T2, T3, RequestInitializer<R>> threeArg(
    Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedure, Class<R> resultType) {
    return (arg1, arg2, arg3) -> new RequestInitializer<>(procedure.getName(), resultType, arg1, arg2, arg3);
  }
}
