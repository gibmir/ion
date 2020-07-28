package com.github.gibmir.ion.lib.http.client.batch;

import com.github.gibmir.ion.api.client.batch.request.BatchRequest;
import com.github.gibmir.ion.api.client.batch.request.builder.BatchRequestBuilder;
import com.github.gibmir.ion.api.client.batch.response.BatchResponse;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.lib.http.client.sender.HttpRequestSender;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class HttpBatchRequest implements BatchRequest {
  private final List<RequestDto> batchRequests;
  private final List<Class<?>> responseTypes;
  private final HttpRequestSender httpRequestSender;
  private final Jsonb jsonb;
  private final Charset charset;
  private final URI uri;
  private final Duration duration;

  private HttpBatchRequest(List<RequestDto> batchRequests, List<Class<?>> responseTypes,
                           HttpRequestSender httpRequestSender, Jsonb jsonb, Charset charset, URI uri,
                           Duration duration) {
    this.batchRequests = batchRequests;
    this.responseTypes = responseTypes;
    this.httpRequestSender = httpRequestSender;
    this.jsonb = jsonb;
    this.charset = charset;
    this.uri = uri;
    this.duration = duration;
  }

  @Override
  public CompletableFuture<BatchResponse> call() {
    HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers
      .ofByteArray(jsonb.toJson(batchRequests).getBytes(charset));

    return httpRequestSender.sendBatch(bodyPublisher, uri, duration, jsonb, charset, responseTypes);
  }

  public static class HttpBatchBuilder implements BatchRequestBuilder<HttpBatchBuilder> {
    //todo JsonRpcRequest
    private final List<RequestDto> batchRequests = new ArrayList<>();
    //todo class per id
    private final List<Class<?>> responseTypes = new ArrayList<>();
    private final HttpRequestSender httpRequestSender;
    private final Jsonb jsonb;
    private final Charset charset;
    private final URI uri;
    private final Duration duration;

    public HttpBatchBuilder(HttpRequestSender httpRequestSender, Jsonb jsonb, Charset charset, URI uri,
                            Duration duration) {
      this.httpRequestSender = httpRequestSender;
      this.jsonb = jsonb;
      this.charset = charset;
      this.uri = uri;
      this.duration = duration;
    }

    @Override
    public <R> HttpBatchBuilder add(String id, Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0,
                                    Class<R> returnType) {
      batchRequests.add(RequestDto.positional(id, jsonRemoteProcedure0.getName(), new Object[0]));
      return this;
    }

    @Override
    public <T, R> HttpBatchBuilder addPositional(String id,
                                                 Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1, T arg,
                                                 Class<R> returnType) {
      batchRequests.add(RequestDto.positional(id, jsonRemoteProcedure1.getName(), new Object[]{arg}));
      return this;
    }

    @Override
    public <T1, T2, R> HttpBatchBuilder addPositional(String id,
                                                      Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                                      T1 arg1, T2 arg2, Class<R> returnType) {
      batchRequests.add(RequestDto.positional(id, jsonRemoteProcedure2.getName(), new Object[]{arg1, arg2}));
      return this;
    }

    @Override
    public <T1, T2, T3, R> HttpBatchBuilder addPositional(String id,
                                                          Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                          T1 arg1, T2 arg2, T3 arg3, Class<R> returnType) {
      batchRequests.add(RequestDto.positional(id, jsonRemoteProcedure3.getName(), new Object[]{arg1, arg2, arg3}));
      return this;
    }

    //todo implement http notifications
    @Override
    public <R> HttpBatchBuilder addNotification(Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0) {
      return null;
    }

    @Override
    public <T, R> HttpBatchBuilder addNotification(Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1, T arg) {
      return null;
    }

    @Override
    public <T1, T2, R> HttpBatchBuilder addNotification(Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2, T1 arg1, T2 arg2) {
      return null;
    }

    @Override
    public <T1, T2, T3, R> HttpBatchBuilder addNotification(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3, T1 arg1, T2 arg2, T3 arg3) {
      return null;
    }

    @Override
    public HttpBatchRequest build() {
      return new HttpBatchRequest(batchRequests, responseTypes, httpRequestSender, jsonb, charset, uri, duration);
    }
  }
}
