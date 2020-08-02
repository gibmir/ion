package com.github.gibmir.ion.api.client.factory;

import com.github.gibmir.ion.api.client.batch.request.builder.BatchRequestBuilder;
import com.github.gibmir.ion.api.client.request.Request0;
import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gibmir.ion.api.client.request.Request2;
import com.github.gibmir.ion.api.client.request.Request3;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;

import java.io.Closeable;

public interface RequestFactory extends Closeable {

  <R> Request0<R> noArg(Class<? extends JsonRemoteProcedure0<R>> procedure);

  <T, R> Request1<T, R> singleArg(Class<? extends JsonRemoteProcedure1<T, R>> procedure);

  <T1, T2, R> Request2<T1, T2, R> twoArg(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedure);

  <T1, T2, T3, R> Request3<T1, T2, T3, R> threeArg(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedure);

  BatchRequestBuilder<?> batch();
}
