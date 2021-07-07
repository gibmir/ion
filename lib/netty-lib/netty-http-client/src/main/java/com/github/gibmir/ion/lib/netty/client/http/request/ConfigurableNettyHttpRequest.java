package com.github.gibmir.ion.lib.netty.client.http.request;

import com.github.gibmir.ion.api.client.request.ConfigurableRequest;

import java.net.URI;

public interface ConfigurableNettyHttpRequest<R extends ConfigurableRequest<R>> extends ConfigurableRequest<R> {
  /**
   * Sets server uri.
   *
   * @param uri server uri
   * @return this
   */
  R uri(URI uri);

  /**
   * @return uri
   */
  URI uri();
}
