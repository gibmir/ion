package com.github.gibmir.ion.lib.http.client.sender;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import com.github.gibmir.ion.api.dto.serialization.SerializationUtils;

import javax.json.JsonStructure;
import javax.json.bind.Jsonb;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class HttpRequestSender {
  private final HttpClient httpClient;

  public HttpRequestSender(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  public <R> CompletableFuture<R> send(HttpRequest.BodyPublisher bodyPublisher, URI uri, Duration timeout, Jsonb jsonb,
                                       Charset charset, Class<R> returnType) {
    HttpRequest httpRequest = HttpRequest.newBuilder()
      .uri(uri)
      .POST(bodyPublisher)
      .timeout(timeout)
      .build();
    return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
      .thenApply(httpResponse -> getResultFromHttpResponse(httpResponse, jsonb, charset, returnType));
  }

  public void send(HttpRequest.BodyPublisher bodyPublisher, URI uri, Duration timeout) {
    HttpRequest httpRequest = HttpRequest.newBuilder()
      .uri(uri)
      .POST(bodyPublisher)
      .timeout(timeout)
      .build();
    httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray());
  }

  private static <R> R getResultFromHttpResponse(HttpResponse<byte[]> httpResponse, Jsonb jsonb, Charset charset,
                                                 Class<R> returnType) {
    JsonStructure jsonStructure = jsonb.fromJson(new String(httpResponse.body(), charset), JsonStructure.class);
    JsonRpcResponse jsonRpcResponse = SerializationUtils.extractResponseFrom(jsonStructure, returnType, jsonb);
    return getResultFromResponse(jsonRpcResponse, returnType);
  }

  private static <R> R getResultFromResponse(JsonRpcResponse jsonRpcResponse, Class<R> returnType) {
    ResultProcessor<R> resultProcessor = new ResultProcessor<>(returnType);
    jsonRpcResponse.processWith(resultProcessor);
    return resultProcessor.result;
  }

  protected static class ResultProcessor<T> implements JsonRpcResponseProcessor {
    private final Class<T> returnType;
    protected T result;

    protected ResultProcessor(Class<T> returnType) {
      this.returnType = returnType;
    }

    @Override
    public void process(ErrorResponse errorResponse) {
      throw new JsonRpcProcessingException(errorResponse);
    }

    @Override
    public void process(SuccessResponse successResponse) {
      result = returnType.cast(successResponse.getResult());
    }
  }
}
