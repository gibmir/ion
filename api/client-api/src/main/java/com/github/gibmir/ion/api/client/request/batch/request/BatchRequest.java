package com.github.gibmir.ion.api.client.request.batch.request;

import com.github.gibmir.ion.api.client.request.batch.response.BatchResponse;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;

import java.util.concurrent.CompletableFuture;

public interface BatchRequest {

  CompletableFuture<BatchResponse> batchCall();

  interface Builder<B extends Builder<B>> {
    <R> B add(String id, Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0);

    <T, R> B addPositional(String id, Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1, T arg);

    <T1, T2, R> B addPositional(String id, Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2, T1 arg1,
                                T2 arg2);

    <T1, T2, T3, R> B addPositional(String id,
                                    Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                    T1 arg1, T2 arg2, T3 arg3);

    BatchRequest build();
  }
}
