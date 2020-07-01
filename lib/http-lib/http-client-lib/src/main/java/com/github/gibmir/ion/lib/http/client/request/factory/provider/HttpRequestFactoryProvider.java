package com.github.gibmir.ion.lib.http.client.request.factory.provider;

import com.github.gibmir.ion.api.client.request.factory.properties.RequestConstants;
import com.github.gibmir.ion.api.client.request.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.lib.http.client.configuration.HttpRequestConstants;
import com.github.gibmir.ion.lib.http.client.request.factory.HttpRequestFactory;
import com.github.gibmir.ion.lib.http.client.sender.HttpRequestSender;

import java.net.URI;
import java.net.http.HttpClient;
import java.nio.charset.Charset;
import java.time.Duration;

public class HttpRequestFactoryProvider implements RequestFactoryProvider {
  @Override
  public HttpRequestFactory provide() {
    Configuration configuration = ConfigurationProvider.load().provide();
    URI defaultUri = configuration.getOptionalValue(HttpRequestConstants.HTTP_URL_PROPERTY, String.class)
      .map(URI::create)
      .orElseThrow();
    Duration defaultTimeout = configuration.getOptionalValue(RequestConstants.REQUEST_TIMEOUT_PROPERTY, String.class)
      .map(Long::valueOf)
      .map(Duration::ofSeconds)
      .orElse(RequestConstants.DEFAULT_TIMEOUT);
    Charset defaultCharset = configuration.getOptionalValue(RequestConstants.REQUEST_CHARSET_PROPERTY, String.class)
      .map(Charset::forName).orElse(RequestConstants.DEFAULT_CHARSET);
    HttpClient httpClient = HttpClient.newBuilder().build();
    return new HttpRequestFactory(new HttpRequestSender(httpClient), defaultUri, defaultTimeout,
      RequestConstants.DEFAULT_JSONB, defaultCharset);
  }
}
