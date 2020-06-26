package com.github.gibmir.ion.api.core.request.positional;

import com.github.gibmir.ion.api.client.context.RequestContext;
import com.github.gibmir.ion.api.client.sender.JsonRpcRequestSender;
import com.github.gibmir.ion.api.core.request.id.JsonRpcRequestIdSupplier;
import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.positional.PositionalRequest;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import com.github.gibmir.ion.api.dto.serialization.SerializationUtils;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import javax.json.bind.spi.JsonbProvider;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

//TODO schema generation
public class Request<R> {
  private final String methodName;
  private final Class<R> resultType;
  private final Object[] args;
  private final JsonRpcRequestIdSupplier jsonRpcRequestIdSupplier;
  private final JsonRpcRequestSender jsonRpcRequestSender;
  private Charset charset = StandardCharsets.UTF_8;
  private TimeUnit timeUnit = TimeUnit.SECONDS;
  private long timeout = 60;
  private Jsonb jsonb = JsonbProvider.provider().create().build();

  public static class Initializer<R> {
    private final String procedureName;
    private final Class<R> returnType;
    private final Object[] args;

    public Initializer(String procedureName, Class<R> returnType, Object... args) {
      this.procedureName = procedureName;
      this.returnType = returnType;
      this.args = args;
    }

    public Request<R> initialize(JsonRpcRequestIdSupplier jsonRpcRequestIdSupplier, JsonRpcRequestSender jsonRpcRequestSender) {
      return new Request<>(procedureName, returnType, jsonRpcRequestIdSupplier, jsonRpcRequestSender, args);
    }
  }

  private Request(String procedureName, Class<R> resultType, JsonRpcRequestIdSupplier jsonRpcRequestIdSupplier,
                  JsonRpcRequestSender jsonRpcRequestSender, Object... args) {
    this.methodName = procedureName;
    this.resultType = resultType;
    this.jsonRpcRequestIdSupplier = jsonRpcRequestIdSupplier;
    this.jsonRpcRequestSender = jsonRpcRequestSender;
    this.args = args;
  }

  public Request<R> timeout(long timeout, TimeUnit timeUnit) {
    this.timeout = timeout;
    this.timeUnit = timeUnit;
    return this;
  }

  public Request<R> charset(Charset charset) {
    this.charset = charset;
    return this;
  }

  public Request<R> jsonb(Jsonb jsonb) {
    this.jsonb = jsonb;
    return this;
  }

  public CompletableFuture<R> execute() {
    CompletableFuture<R> completableFuture = new CompletableFuture<>();
    CompletableResponseProcessor<R> asyncResponseProcessor = new CompletableResponseProcessor<>(resultType, completableFuture);
    String id = jsonRpcRequestIdSupplier.get();
    JsonRpcRequest request = new PositionalRequest(id, methodName, args);
    byte[] requestPayload = jsonb.toJson(request).getBytes(charset);
    RequestContext requestContext = RequestContext.defaultContext(methodName, requestPayload, id, timeout, timeUnit);
    jsonRpcRequestSender.sendAsync(requestContext, (responsePayload, exception) -> {
      if (responsePayload != null) {
        processResponse(completableFuture, asyncResponseProcessor, responsePayload);
      } else if (exception != null) {
        completableFuture.completeExceptionally(exception);
      } else {
        ErrorResponse errorResponse = ErrorResponse.withJsonRpcError(id, Errors.INVALID_RPC.getError());
        JsonRpcProcessingException jsonRpcProcessingException = new JsonRpcProcessingException(errorResponse);
        completableFuture.completeExceptionally(jsonRpcProcessingException);
      }
    });
    return completableFuture;
  }



  private void processResponse(CompletableFuture<R> completableFuture, CompletableResponseProcessor<R> asyncResponseProcessor,
                               byte[] responsePayload) {
    try {
      JsonObject jsonObject = jsonb.fromJson(new String(responsePayload, charset), JsonObject.class);
      JsonRpcResponse jsonRpcResponse = SerializationUtils.extractResponseFrom(jsonObject, resultType, jsonb);
      jsonRpcResponse.processWith(asyncResponseProcessor);
    } catch (Exception e) {
      completableFuture.completeExceptionally(e);
    }
  }

  private static class CompletableResponseProcessor<T> implements JsonRpcResponseProcessor {
    private final Class<T> returnType;
    private final CompletableFuture<T> completableFuture;

    private CompletableResponseProcessor(Class<T> returnType, CompletableFuture<T> completableFuture) {
      this.returnType = returnType;
      this.completableFuture = completableFuture;
    }

    @Override
    public void process(ErrorResponse errorResponse) {
      completableFuture.completeExceptionally(new JsonRpcProcessingException(errorResponse));
    }

    @Override
    public void process(SuccessResponse successResponse) {
      completableFuture.complete(returnType.cast(successResponse.getResult()));
    }
  }
}
