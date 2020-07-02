package com.github.gibmir.ion.lib.http.client.configuration;

import com.github.gibmir.ion.api.configuration.Configuration;

import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;

import static com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils.ROOT_PREFIX;

public class HttpRequestConfigurationUtils {
  private HttpRequestConfigurationUtils() {
  }

  //int properties
  public static final String HTTP_CLIENT_CONNECT_TIMEOUT = ROOT_PREFIX + ".http.client.connect.timeout";
  public static final String HTTP_CLIENT_EXECUTOR_POOL_SIZE = ROOT_PREFIX + ".http.client.executor.pool.size";
  public static final String HTTP_CLIENT_PRIORITY = ROOT_PREFIX + ".http.client.priority";
  //string properties
  public static final String HTTP_REQUEST_URL = ROOT_PREFIX + ".http.client.request.url";
  public static final String HTTP_CLIENT_VERSION = ROOT_PREFIX + ".http.client.version";
  public static final String HTTP_CLIENT_REDIRECT = ROOT_PREFIX + ".http.client.redirect";

  public static URI createUriWith(Configuration configuration) {
    return configuration.getOptionalValue(HttpRequestConfigurationUtils.HTTP_REQUEST_URL, String.class)
      .map(URI::create)
      .orElseThrow();
  }

  public static HttpClient createHttpClientWith(Configuration configuration) {
    HttpClient.Builder builder = HttpClient.newBuilder();
    configuration.getOptionalValue(HTTP_CLIENT_CONNECT_TIMEOUT, Integer.class)
      .map(Duration::ofSeconds)
      .ifPresent(builder::connectTimeout);
    configuration.getOptionalValue(HTTP_CLIENT_EXECUTOR_POOL_SIZE, Integer.class)
      .map(Executors::newFixedThreadPool)
      .ifPresent(builder::executor);
    configuration.getOptionalValue(HTTP_CLIENT_PRIORITY, Integer.class).ifPresent(builder::priority);
    configuration.getOptionalValue(HTTP_CLIENT_VERSION, String.class)
      .map(HttpClient.Version::valueOf)
      .ifPresent(builder::version);
    configuration.getOptionalValue(HTTP_CLIENT_REDIRECT, String.class)
      .map(HttpClient.Redirect::valueOf)
      .ifPresent(builder::followRedirects);
    return builder.build();
  }
}
