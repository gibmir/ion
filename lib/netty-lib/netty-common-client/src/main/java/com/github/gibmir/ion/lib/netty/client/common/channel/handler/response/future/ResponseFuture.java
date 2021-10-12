package com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;

import javax.json.bind.Jsonb;
import java.lang.reflect.Type;

public final class ResponseFuture {
  private final Jsonb responseJsonb;
  private final String id;
  private final Type returnType;
  private final ResponseCallback<?> responseCallback;

  public ResponseFuture(final String id, final Type returnType, final Jsonb responseJsonb,
                        final ResponseCallback<?> responseCallback) {
    this.id = id;
    this.returnType = returnType;
    this.responseJsonb = responseJsonb;
    this.responseCallback = responseCallback;
  }

  /**
   * @return request id
   */
  public String getId() {
    return id;
  }

  /**
   * @return serializer
   */
  public Jsonb getResponseJsonb() {
    return responseJsonb;
  }

  /**
   * @return return type
   */
  public Type getReturnType() {
    return returnType;
  }

  /**
   * Completes future with specified exception.
   *
   * @param throwable processing exception
   */
  public void completeExceptionally(final Throwable throwable) {
    responseCallback.onResponse(null, throwable);
  }

  /**
   * Completes future with response json.
   *
   * @param responseJson response json
   */
  public void complete(final String responseJson) {
    responseCallback.onResponse(responseJsonb.fromJson(responseJson, returnType), null);
  }

  /**
   * Completes future with json-rpc error.
   *
   * @param errorResponse json-rpc error
   */
  public void completeError(final ErrorResponse errorResponse) {
    responseCallback.onResponse(null, new JsonRpcProcessingException(errorResponse));
  }
}
