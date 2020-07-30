package com.github.gibmir.ion.api.client.request;

import java.util.concurrent.CompletableFuture;

public interface Request1<T, R> {
  CompletableFuture<R> positionalCall(String id, T arg);

  CompletableFuture<R> namedCall(String id, T arg);

  void notificationCall(T arg);
}
