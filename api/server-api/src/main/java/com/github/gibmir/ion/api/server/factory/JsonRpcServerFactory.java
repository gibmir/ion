package com.github.gibmir.ion.api.server.factory;

import com.github.gibmir.ion.api.server.JsonRpcServer;

import java.io.Closeable;

public interface JsonRpcServerFactory extends Closeable {
  /**
   * Creates json rpc server.
   *
   * @return json rpc server
   */
  JsonRpcServer create();
}
