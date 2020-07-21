package com.github.gibmir.ion.lib.http.client.request;

import com.github.gibmir.ion.api.client.request.Request2;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
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

  public HttpRequest2(Class<R> returnType, String procedureName, HttpRequestSender defaultHttpRequestSender, URI defaultUri,
                      Duration defaultTimeout, Jsonb defaultJsonb, Charset defaultCharset) {
    super(returnType, procedureName, defaultHttpRequestSender, defaultUri, defaultTimeout, defaultJsonb, defaultCharset);
  }

  @Override
  public CompletableFuture<R> positionalCall(String id, T1 arg1, T2 arg2) {
    RequestDto requestDto = RequestDto.positional(id, procedureName, new Object[]{arg1, arg2});
    String json = jsonb.toJson(requestDto);
    HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(json.getBytes(charset));
    return httpRequestSender.send(bodyPublisher, uri, timeout, jsonb, charset, returnType);
  }

  @Override
  public void notificationCall(T1 arg1, T2 arg2) {
    NotificationDto notificationDto = new NotificationDto(procedureName, new Object[]{arg1, arg2});
    String json = jsonb.toJson(notificationDto);
    HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(json.getBytes(charset));
    httpRequestSender.send(bodyPublisher, uri, timeout);
  }

  @Override
  public HttpRequest2<T1, T2, R> uri(URI uri) {
    return new HttpRequest2<>(returnType, procedureName, httpRequestSender, uri, timeout, jsonb, charset);
  }

  @Override
  public HttpRequest2<T1, T2, R> jsonb(Jsonb jsonb) {
    return new HttpRequest2<>(returnType, procedureName, httpRequestSender, uri, timeout, jsonb, charset);
  }

  @Override
  public HttpRequest2<T1, T2, R> timeout(Duration timeout) {
    return new HttpRequest2<>(returnType, procedureName, httpRequestSender, uri, timeout, jsonb, charset);
  }

  @Override
  public HttpRequest2<T1, T2, R> charset(Charset charset) {
    return new HttpRequest2<>(returnType, procedureName, httpRequestSender, uri, timeout, jsonb, charset);
  }
}
