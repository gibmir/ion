package com.github.gibmir.ion.lib.http.client.request;

import com.github.gibmir.ion.lib.http.client.sender.HttpRequestSender;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.nio.charset.Charset;
import java.time.Duration;

public abstract class AbstractHttpRequest<T, Request extends ConfigurableHttpRequest<Request>>
  implements ConfigurableHttpRequest<Request> {
  protected final Class<T> returnType;
  protected final String methodName;
  protected final HttpRequestSender httpRequestSender;
  protected URI uri;
  protected Duration timeout;
  protected Jsonb jsonb;
  protected Charset charset;

  public AbstractHttpRequest(Class<T> returnType, String methodName, HttpRequestSender defaultHttpRequestSender,
                             URI defaultUri, Duration defaultTimeout, Jsonb defaultJsonb, Charset defaultCharset) {
    this.httpRequestSender = defaultHttpRequestSender;
    this.returnType = returnType;
    this.methodName = methodName;
    this.uri = defaultUri;
    this.timeout = defaultTimeout;
    this.jsonb = defaultJsonb;
    this.charset = defaultCharset;
  }

  @Override
  public URI uri() {
    return uri;
  }

  @Override
  public Jsonb jsonb() {
    return jsonb;
  }

  @Override
  public Duration timeout() {
    return timeout;
  }

  @Override
  public Charset charset() {
    return charset;
  }
}
