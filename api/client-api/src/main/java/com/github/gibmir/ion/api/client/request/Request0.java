package com.github.gibmir.ion.api.client.request;

import java.util.concurrent.CompletableFuture;

public interface Request0<R> {
  CompletableFuture<R> call(String id);

  void notificationCall();
}
