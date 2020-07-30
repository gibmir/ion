package com.github.gibmir.ion.lib.netty.client.sender.handler.response.future;

import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public class ResponseFuture {
  private final String id;
  private final Type returnType;
  private final CompletableFuture<Object> responseFuture;

  public ResponseFuture(String id, Type returnType, CompletableFuture<Object> responseFuture) {
    this.id = id;
    this.returnType = returnType;
    this.responseFuture = responseFuture;
  }

  public Type getReturnType() {
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
