package com.github.gibmir.ion.lib.http.client.request.factory.provider;

import com.github.gibmir.ion.api.client.request.factory.configuration.RequestConfigurationUtils;
import com.github.gibmir.ion.api.client.request.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.lib.http.client.configuration.HttpRequestConfigurationUtils;
import com.github.gibmir.ion.lib.http.client.request.factory.HttpRequestFactory;
import com.github.gibmir.ion.lib.http.client.sender.HttpRequestSender;

import java.net.http.HttpClient;

public class HttpRequestFactoryProvider implements RequestFactoryProvider {
  private static volatile HttpRequestFactory httpRequestFactoryInstance;

  @Override
  public HttpRequestFactory provide() {
    HttpRequestFactory localInstance = httpRequestFactoryInstance;
    //double-check singleton
    if (localInstance == null) {
      synchronized (HttpRequestFactory.class) {
        localInstance = httpRequestFactoryInstance;
        if (localInstance == null) {
          httpRequestFactoryInstance = localInstance = createHttpRequestFactory();
        }
      }
    }
    return localInstance;
  }

  private HttpRequestFactory createHttpRequestFactory() {
    Configuration configuration = ConfigurationProvider.load().provide();
    HttpClient httpClient = HttpRequestConfigurationUtils.createHttpClientWith(configuration);
    HttpRequestSender defaultHttpRequestSender = new HttpRequestSender(httpClient);
    return new HttpRequestFactory(defaultHttpRequestSender, HttpRequestConfigurationUtils.createUriWith(configuration),
      RequestConfigurationUtils.createTimeoutWith(configuration),
      ConfigurationUtils.createJsonbWith(configuration),
      RequestConfigurationUtils.createCharsetWith(configuration));
  }
}
