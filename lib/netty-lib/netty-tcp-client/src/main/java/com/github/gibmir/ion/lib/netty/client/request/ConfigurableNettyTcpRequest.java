package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.ConfigurableRequest;

import java.net.SocketAddress;

public interface ConfigurableNettyTcpRequest<R extends ConfigurableRequest<R>> extends ConfigurableRequest<R> {

  R socketAddress(SocketAddress socketAddress);

  SocketAddress socketAddress();
}
