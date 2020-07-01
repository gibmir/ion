package com.github.gibmir.ion.api.client.request;

import com.github.gibmir.ion.api.client.request.argument.NamedArgument;

import java.util.concurrent.CompletableFuture;

public interface Request3<T1, T2, T3, R> {
  CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2, T3 arg3);

  void notificationCall(T1 arg1, T2 arg2, T3 arg3);

  CompletableFuture<R> namedCall(String id, NamedArgument<T1> namedArgument1,
                                 NamedArgument<T2> namedArgument2, NamedArgument<T3> namedArgument3);
}
