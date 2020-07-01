package com.github.gibmir.ion.api.client.request;

import com.github.gibmir.ion.api.client.request.argument.NamedArgument;

import java.util.concurrent.CompletableFuture;

public interface Request1<T, R> {
  CompletableFuture<R> positionalCall(String id, T arg);

  void notificationCall(T arg);

  CompletableFuture<R> namedCall(String id, NamedArgument<T> namedArg);
}
