package com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;

import javax.json.bind.Jsonb;
import java.lang.reflect.Type;

public class ResponseFuture implements JsonRpcResponseProcessor {
  private final Jsonb responseJsonb;
  private final String id;
  private final Type returnType;
  private final ResponseCallback<?> responseCallback;

  public ResponseFuture(String id, Type returnType, Jsonb responseJsonb, ResponseCallback<?> responseCallback) {
    this.id = id;
    this.returnType = returnType;
    this.responseJsonb = responseJsonb;
    this.responseCallback = responseCallback;
  }

  public String getId() {
    return id;
  }

  public Jsonb getResponseJsonb() {
    return responseJsonb;
  }

  public Type getReturnType() {
    return returnType;
  }

  public void completeExceptionally(Throwable throwable) {
    responseCallback.onResponse(null, throwable);
  }

  public void complete(String responseJson) {
    responseCallback.onResponse(responseJsonb.fromJson(responseJson, returnType), null);
  }

  @Override
  public void process(ErrorResponse errorResponse) {
    responseCallback.onResponse(null, new JsonRpcProcessingException(errorResponse));
  }
}
