package com.github.gibmir.ion.lib.netty.client.sender.handler.response.future;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;

public class ResponseFuture implements JsonRpcResponseProcessor {
  private final Jsonb responseJsonb;
  private final String id;
  private final Type returnType;
  private final CompletableFuture<Object> responseFuture;

  public ResponseFuture(String id, Type returnType, CompletableFuture<Object> responseFuture,
                        Jsonb responseJsonb) {
    this.id = id;
    this.returnType = returnType;
    this.responseFuture = responseFuture;
    this.responseJsonb = responseJsonb;
  }

  public String getId() {
    return id;
  }

  public CompletableFuture<Object> getFuture() {
    return responseFuture;
  }

  public Jsonb getResponseJsonb() {
    return responseJsonb;
  }

  public Type getReturnType() {
    return returnType;
  }

  public void completeExceptionally(Throwable throwable) {
    responseFuture.completeExceptionally(throwable);
  }

  public void complete(Object value) {
    responseFuture.complete(value);
  }

  public void complete(JsonValue value) {
    responseFuture.complete(responseJsonb.fromJson(value.toString(), returnType));
  }

  @Override
  public void process(ErrorResponse errorResponse) {
    responseFuture.completeExceptionally(new JsonRpcProcessingException(errorResponse));
  }
}
