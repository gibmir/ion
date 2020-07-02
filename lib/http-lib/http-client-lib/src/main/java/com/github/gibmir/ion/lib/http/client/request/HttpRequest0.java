package com.github.gibmir.ion.lib.http.client.request;

import com.github.gibmir.ion.api.client.request.Request0;
import com.github.gibmir.ion.api.dto.request.transfer.NotificationDto;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.lib.http.client.sender.HttpRequestSender;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.net.http.HttpRequest;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class HttpRequest0<R> extends AbstractHttpRequest<R, HttpRequest0<R>>
  implements Request0<R> {

  public static final Object[] EMPTY_PAYLOAD = new Object[0];

  public HttpRequest0(Class<R> returnType, String methodName, HttpRequestSender defaultHttpRequestSender, URI defaultUri,
                      Duration defaultTimeout, Jsonb defaultJsonb, Charset defaultCharset) {
    super(returnType, methodName, defaultHttpRequestSender, defaultUri, defaultTimeout, defaultJsonb, defaultCharset);
  }

  @Override
  public CompletableFuture<R> call(String id) {
    RequestDto requestDto = new RequestDto();
    requestDto.setId(id);
    requestDto.setMethodName(methodName);
    String json = jsonb.toJson(requestDto);
    HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(json.getBytes(charset));
    return httpRequestSender.send(bodyPublisher, uri, timeout, jsonb, charset, returnType);
  }

  @Override
  public void notificationCall() {
    NotificationDto notificationDto = new NotificationDto(methodName, EMPTY_PAYLOAD);
    String json = jsonb.toJson(notificationDto);
    HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers.ofByteArray(json.getBytes(charset));
    httpRequestSender.send(bodyPublisher, uri, timeout);
  }

  @Override
  public HttpRequest0<R> uri(URI uri) {
    this.uri = uri;
    return this;
  }

  @Override
  public HttpRequest0<R> jsonb(Jsonb jsonb) {
    this.jsonb = jsonb;
    return this;
  }

  @Override
  public HttpRequest0<R> timeout(Duration timeout) {
    this.timeout = timeout;
    return this;
  }

  @Override
  public HttpRequest0<R> charset(Charset charset) {
    this.charset = charset;
    return this;
  }
}
