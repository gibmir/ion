package com.github.gibmir.ion.lib.http.client.sender;

import com.github.gibmir.ion.api.client.batch.response.BatchResponse;
import com.github.gibmir.ion.api.client.batch.response.element.BatchElement;
import com.github.gibmir.ion.api.client.batch.response.element.error.ErrorBatchElement;
import com.github.gibmir.ion.api.client.batch.response.element.success.SuccessBatchElement;
import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.batch.BatchResponseDto;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
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
import java.util.ArrayList;
import java.util.List;
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

  public CompletableFuture<BatchResponse> sendBatch(HttpRequest.BodyPublisher bodyPublisher, URI uri, Duration timeout, Jsonb jsonb,
                                                    Charset charset, List<Class<?>> returnTypes) {
    HttpRequest httpRequest = HttpRequest.newBuilder()
      .uri(uri)
      .POST(bodyPublisher)
      .timeout(timeout)
      .build();
    return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofByteArray())
      .thenApply(httpResponse -> getResultFromHttpResponse(httpResponse, jsonb, charset, returnTypes));
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

  private static BatchResponse getResultFromHttpResponse(HttpResponse<byte[]> httpResponse, Jsonb jsonb, Charset charset,
                                                         List<Class<?>> returnTypes) {
    JsonStructure jsonStructure = jsonb.fromJson(new String(httpResponse.body(), charset), JsonStructure.class);
    JsonRpcResponse jsonRpcResponse = SerializationUtils.extractBatchResponseFrom(jsonStructure, returnTypes, jsonb);
    return getResultFromBatchResponse(jsonRpcResponse);
  }

  private static <R> R getResultFromResponse(JsonRpcResponse jsonRpcResponse, Class<R> returnType) {
    RequestProcessor<R> requestProcessor = new RequestProcessor<>(returnType);
    jsonRpcResponse.processWith(requestProcessor);
    return requestProcessor.result;
  }

  private static BatchResponse getResultFromBatchResponse(JsonRpcResponse jsonRpcResponse) {
    BatchProcessor batchProcessor = new BatchProcessor();
    jsonRpcResponse.processWith(batchProcessor);
    return batchProcessor.batchResponse;
  }

  private static class RequestProcessor<T> implements JsonRpcResponseProcessor {
    private final Class<T> returnType;
    protected T result;

    protected RequestProcessor(Class<T> returnType) {
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

    @Override
    public void process(BatchResponseDto batchResponseDto) {
      process(ErrorResponse.withNullId(Errors.INTERNAL_RPC_ERROR.getError()
        .appendMessage("Server return batch on client request")));
    }
  }

  private static class BatchProcessor implements JsonRpcResponseProcessor {
    private BatchResponse batchResponse;
    private final List<BatchElement> batchElements = new ArrayList<>();

    @Override
    public void process(ErrorResponse errorResponse) {
      batchElements.add(new ErrorBatchElement(errorResponse.getId(), new JsonRpcProcessingException(errorResponse)));
    }

    @Override
    public void process(SuccessResponse successResponse) {
      batchElements.add(new SuccessBatchElement(successResponse.getId(), successResponse.getResult()));

    }

    @Override
    public void process(BatchResponseDto batchResponseDto) {
      JsonRpcResponse[] jsonRpcResponses = batchResponseDto.getJsonRpcResponses();
      for (JsonRpcResponse jsonRpcRespons : jsonRpcResponses) {
        jsonRpcRespons.processWith(this);
      }
      batchResponse = new BatchResponse(batchElements);
    }
  }
}
