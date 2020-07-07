package com.github.gibmir.ion.lib.http.client.request;

import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gibmir.ion.api.dto.request.transfer.NotificationDto;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.lib.http.client.sender.HttpRequestSender;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class HttpRequest1<T, R> extends AbstractHttpRequest<R, HttpRequest1<T, R>>
  implements Request1<T, R> {

  public HttpRequest1(Class<R> returnType, String methodName, HttpRequestSender defaultHttpRequestSender, URI defaultUri,
                      Duration defaultTimeout, Jsonb defaultJsonb, Charset defaultCharset) {
    super(returnType, methodName, defaultHttpRequestSender, defaultUri, defaultTimeout, defaultJsonb, defaultCharset);
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T arg) {
    RequestDto requestDto = RequestDto.positional(id, methodName, new Object[]{arg});
    String json = jsonb.toJson(requestDto);
    HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(json.getBytes(charset));
    return httpRequestSender.send(bodyPublisher, uri, timeout, jsonb, charset, returnType);
  }

  @Override
  public void notificationCall(T arg) {
    NotificationDto notificationDto = new NotificationDto(methodName, new Object[]{arg});
    String json = jsonb.toJson(notificationDto);
    HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(json.getBytes(charset));
    httpRequestSender.send(bodyPublisher, uri, timeout);
  }

  @Override
  public HttpRequest1<T, R> uri(URI uri) {
    this.uri = uri;
    return this;
  }

  @Override
  public HttpRequest1<T, R> jsonb(Jsonb jsonb) {
    this.jsonb = jsonb;
    return this;
  }

  @Override
  public HttpRequest1<T, R> timeout(Duration timeout) {
    this.timeout = timeout;
    return this;
  }

  @Override
  public HttpRequest1<T, R> charset(Charset charset) {
    this.charset = charset;
    return this;
  }
}
