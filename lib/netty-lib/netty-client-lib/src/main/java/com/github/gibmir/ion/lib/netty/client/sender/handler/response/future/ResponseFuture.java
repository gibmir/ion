package com.github.gibmir.ion.lib.netty.client.sender.handler.response.future;

import java.util.concurrent.CompletableFuture;

public class ResponseFuture {
  private final String id;
  private final Class<?> returnType;
  private final CompletableFuture<Object> responseFuture;

  public ResponseFuture(String id, Class<?> returnType, CompletableFuture<Object> responseFuture) {
    this.id = id;
    this.returnType = returnType;
    this.responseFuture = responseFuture;
  }

  public Class<?> getReturnType() {
    return returnType;
  }

  public CompletableFuture<Object> getFuture() {
    return responseFuture;
  }

  public void completeExceptionally(Throwable throwable) {
    responseFuture.completeExceptionally(throwable);
  }

  public String getId() {
    return id;
  }
}
