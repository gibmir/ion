package com.github.gibmir.ion.lib.http.client.request;

import com.github.gibmir.ion.api.client.request.ConfigurableRequest;

import java.net.URI;
import java.time.Duration;

public interface ConfigurableHttpRequest<R extends ConfigurableRequest<R>> extends ConfigurableRequest<R> {
  R uri(URI uri);

  URI uri();

  Duration timeout();

  R timeout(Duration timeout);
}
