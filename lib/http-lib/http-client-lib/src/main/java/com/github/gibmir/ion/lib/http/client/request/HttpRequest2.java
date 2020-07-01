package com.github.gibmir.ion.lib.http.client.request;

import com.github.gibmir.ion.api.client.request.Request2;
import com.github.gibmir.ion.api.client.request.named.NamedArgument;
import com.github.gibmir.ion.api.dto.request.transfer.NotificationDto;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.lib.http.client.sender.HttpRequestSender;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class HttpRequest2<T1, T2, R> extends AbstractHttpRequest<R, HttpRequest2<T1, T2, R>>
  implements Request2<T1, T2, R> {

  public HttpRequest2(Class<R> returnType, String methodName, HttpRequestSender defaultHttpRequestSender, URI defaultUri,
                      Duration defaultTimeout, Jsonb defaultJsonb, Charset defaultCharset) {
    super(returnType, methodName, defaultHttpRequestSender, defaultUri, defaultTimeout, defaultJsonb, defaultCharset);
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2) {
    RequestDto requestDto = new RequestDto(id, methodName, new Object[]{arg1, arg2});
    String json = jsonb.toJson(requestDto);
    HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(json.getBytes(charset));
    return httpRequestSender.send(bodyPublisher, uri, timeout, jsonb, charset, returnType);
  }

  @Override
  public void notificationCall(T1 arg1, T2 arg2) {
    NotificationDto notificationDto = new NotificationDto(methodName, new Object[]{arg1, arg2});
    String json = jsonb.toJson(notificationDto);
    HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(json.getBytes(charset));
    httpRequestSender.send(bodyPublisher, uri, timeout);
  }

  @Override
  public CompletableFuture<R> namedCall(String id, NamedArgument<T1> namedArgument1, NamedArgument<T2> namedArgument2) {
    RequestDto requestDto = new RequestDto(id, methodName, new Object[]{namedArgument1, namedArgument2});
    String json = jsonb.toJson(requestDto);
    HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(json.getBytes(charset));
    return httpRequestSender.send(bodyPublisher, uri, timeout, jsonb, charset, returnType);
  }

  @Override
  public HttpRequest2<T1, T2, R> uri(URI uri) {
    this.uri = uri;
    return this;
  }

  @Override
  public HttpRequest2<T1, T2, R> jsonb(Jsonb jsonb) {
    this.jsonb = jsonb;
    return this;
  }

  @Override
  public HttpRequest2<T1, T2, R> timeout(Duration timeout) {
    this.timeout = timeout;
    return this;
  }

  @Override
  public HttpRequest2<T1, T2, R> charset(Charset charset) {
    this.charset = charset;
    return this;
  }
}
