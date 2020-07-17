package com.github.gibmir.ion.lib.netty.client.sender.handler.response.future;

import java.util.concurrent.CompletableFuture;

public class ResponseFuture {
  private final Class<?> returnType;
  private final CompletableFuture<Object> responseFuture;

  public ResponseFuture(Class<?> returnType, CompletableFuture<Object> responseFuture) {
    this.returnType = returnType;
    this.responseFuture = responseFuture;
  }

  public Class<?> getReturnType() {
    return returnType;
  }

  public CompletableFuture<Object> getResponseFuture() {
    return responseFuture;
  }

  public void completeExceptionally(Throwable throwable) {
    responseFuture.completeExceptionally(throwable);
  }
}
