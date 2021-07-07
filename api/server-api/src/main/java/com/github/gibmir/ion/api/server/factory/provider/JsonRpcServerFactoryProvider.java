package com.github.gibmir.ion.api.server.factory.provider;

import com.github.gibmir.ion.api.server.factory.JsonRpcServerFactory;

import java.util.ServiceLoader;

public interface JsonRpcServerFactoryProvider {
  /**
   * Loads factory provider through SPI.
   *
   * @return loaded server factory provider
   */
  static JsonRpcServerFactoryProvider load() {
    return ServiceLoader.load(JsonRpcServerFactoryProvider.class).findFirst().orElseThrow();
  }

  /**
   * Provides server factory.
   *
   * @return json-rpc server factory
   */
  JsonRpcServerFactory provide();
}
