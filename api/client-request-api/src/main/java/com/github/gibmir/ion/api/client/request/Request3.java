package com.github.gibmir.ion.api.client.request;

import java.util.concurrent.CompletableFuture;

public interface Request3<T1, T2, T3, R> {
  CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2, T3 arg3);

  CompletableFuture<R> namedCall(String id, T1 arg1, T2 arg2, T3 arg3);

  void notificationCall(T1 arg1, T2 arg2, T3 arg3);
}
