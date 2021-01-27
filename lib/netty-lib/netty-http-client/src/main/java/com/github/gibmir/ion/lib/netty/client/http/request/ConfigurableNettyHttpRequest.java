package com.github.gibmir.ion.lib.netty.client.http.request;

import com.github.gibmir.ion.api.client.request.ConfigurableRequest;

import java.net.SocketAddress;
import java.net.URI;

public interface ConfigurableNettyHttpRequest<R extends ConfigurableRequest<R>> extends ConfigurableRequest<R> {
  R uri(URI uri);

  URI uri();
}
