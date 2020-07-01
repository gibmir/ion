package com.github.gibmir.ion.lib.http.client.request.factory;

import com.github.gibmir.ion.api.client.request.factory.RequestFactory;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.lib.http.client.request.HttpRequest0;
import com.github.gibmir.ion.lib.http.client.request.HttpRequest1;
import com.github.gibmir.ion.lib.http.client.request.HttpRequest2;
import com.github.gibmir.ion.lib.http.client.request.HttpRequest3;
import com.github.gibmir.ion.lib.http.client.sender.HttpRequestSender;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.Duration;

public class HttpRequestFactory implements RequestFactory {
  private final HttpRequestSender defaultHttpRequestSender;
  private final URI defaultUri;
  private final Duration defaultTimeout;
  private final Jsonb defaultJsonb;
  private final Charset defaultCharset;

  public HttpRequestFactory(HttpRequestSender defaultHttpRequestSender, URI defaultUri, Duration defaultTimeout,
                            Jsonb defaultJsonb, Charset defaultCharset) {
    this.defaultHttpRequestSender = defaultHttpRequestSender;
    this.defaultUri = defaultUri;
    this.defaultTimeout = defaultTimeout;
    this.defaultJsonb = defaultJsonb;
    this.defaultCharset = defaultCharset;
  }

  @Override
  public <R> HttpRequest0<R> noArg(Class<? extends JsonRemoteProcedure0<R>> procedure, Class<R> returnType) {
    return new HttpRequest0<>(returnType, procedure.getName(), defaultHttpRequestSender, defaultUri, defaultTimeout,
      defaultJsonb, defaultCharset);
  }

  @Override
  public <T, R> HttpRequest1<T, R> singleArg(Class<? extends JsonRemoteProcedure1<T, R>> procedure, Class<R> returnType) {
    return new HttpRequest1<>(returnType, procedure.getName(), defaultHttpRequestSender, defaultUri, defaultTimeout,
      defaultJsonb, defaultCharset);
  }

  @Override
  public <T1, T2, R> HttpRequest2<T1, T2, R> twoArg(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedure,
                                                    Class<R> returnType) {
    return new HttpRequest2<>(returnType, procedure.getName(), defaultHttpRequestSender, defaultUri, defaultTimeout,
      defaultJsonb, defaultCharset);
  }

  @Override
  public <T1, T2, T3, R> HttpRequest3<T1, T2, T3, R> threeArg(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedure,
                                                              Class<R> returnType) {
    return new HttpRequest3<>(returnType, procedure.getName(), defaultHttpRequestSender, defaultUri, defaultTimeout,
      defaultJsonb, defaultCharset);
  }
}
