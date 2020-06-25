package com.github.gibmir.ion.api.core.request;

import com.github.gibmir.ion.api.client.sender.JsonRpcRequestSender;
import com.github.gibmir.ion.api.client.context.RequestContext;
import com.github.gibmir.ion.api.core.request.callback.ResponseCallback;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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

  public R execute() throws Throwable {
    SyncCallback<R> responseCallback = new SyncCallback<>(timeout, timeUnit);
    executeAsync(responseCallback);
    return responseCallback.getResult();
  }

  public void executeAsync(ResponseCallback<R> responseCallback) {
    try {
      var futureTask = new FutureTask<>(() -> {
        doExecute(responseCallback);
        return null;
      });
      new Thread(futureTask).start();
      futureTask.get(timeout, timeUnit);
    } catch (ExecutionException e) {
      throw new JsonRpcProcessingException(e.getCause());
    } catch (TimeoutException e) {
      responseCallback.onComplete(null, e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      responseCallback.onComplete(null, e);
    }
  }

  private void doExecute(ResponseCallback<R> responseCallback) {
    AsyncResponseProcessor<R> asyncResponseProcessor = new AsyncResponseProcessor<>(responseCallback, resultType);
    String id = jsonRpcRequestIdSupplier.get();
    JsonRpcRequest request = new PositionalRequest(id, methodName, args);
    byte[] requestPayload = jsonb.toJson(request).getBytes(charset);
    RequestContext requestContext = RequestContext.defaultContext(methodName, requestPayload, id);
    jsonRpcRequestSender.sendAsync(requestContext, (responsePayload, exception) -> {
      if (responsePayload != null) {
        JsonObject jsonObject = jsonb.fromJson(new String(responsePayload, charset), JsonObject.class);
        JsonRpcResponse jsonRpcResponse = SerializationUtils.extractResponseFrom(jsonObject, resultType, jsonb);
        jsonRpcResponse.processWith(asyncResponseProcessor);
      } else if (exception != null) {
        responseCallback.onComplete(null, new JsonRpcProcessingException(exception));
      } else {
        ErrorResponse errorResponse = ErrorResponse.withJsonRpcError(id, Errors.INVALID_RPC.getError());
        responseCallback.onComplete(null, new JsonRpcProcessingException(errorResponse));
      }
    });
  }

  private static class SyncCallback<R> implements ResponseCallback<R> {
    private final Semaphore semaphore = new Semaphore(0);
    private final long timeout;
    private final TimeUnit timeUnit;
    private Throwable throwable;
    private R result;

    private SyncCallback(long timeout, TimeUnit timeUnit) {
      this.timeout = timeout;
      this.timeUnit = timeUnit;
    }

    @Override
    public void onComplete(R result, Throwable throwable) {
      try {
        this.result = result;
        this.throwable = throwable;
      } finally {
        semaphore.release();
      }
    }

    public R getResult() throws Throwable {
      if (semaphore.tryAcquire(timeout, timeUnit)) {
        if (throwable != null) {
          throw throwable;
        }
        return result;
      } else {
        throw new TimeoutException();
      }
    }
  }

  private static class AsyncResponseProcessor<T> implements JsonRpcResponseProcessor {
    private final ResponseCallback<T> responseCallback;
    private final Class<T> returnType;

    private AsyncResponseProcessor(ResponseCallback<T> responseCallback, Class<T> returnType) {
      this.responseCallback = responseCallback;
      this.returnType = returnType;
    }

    @Override
    public void process(ErrorResponse errorResponse) {
      responseCallback.onComplete(null, new JsonRpcProcessingException(errorResponse));
    }

    @Override
    public void process(SuccessResponse successResponse) {
      responseCallback.onComplete(returnType.cast(successResponse.getResult()), null);
    }
  }
}
