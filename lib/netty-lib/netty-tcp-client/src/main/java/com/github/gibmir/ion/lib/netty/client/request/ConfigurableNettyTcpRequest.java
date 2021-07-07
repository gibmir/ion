package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.client.request.ConfigurableRequest;

import java.net.SocketAddress;

public interface ConfigurableNettyTcpRequest<R extends ConfigurableRequest<R>> extends ConfigurableRequest<R> {
  /**
   * Sets socket address to request.
   *
   * @param socketAddress server socket address
   * @return this
   */
  R socketAddress(SocketAddress socketAddress);

  /**
   * @return current socket address
   */
  SocketAddress socketAddress();
}
