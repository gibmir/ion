package com.github.gibmir.ion.api.server.factory.provider;

import com.github.gibmir.ion.api.server.factory.JsonRpcServerFactory;

import java.util.ServiceLoader;

public interface JsonRpcServerFactoryProvider {
  static JsonRpcServerFactoryProvider load() {
    return ServiceLoader.load(JsonRpcServerFactoryProvider.class).findFirst().orElseThrow();
  }

  JsonRpcServerFactory provide();
}
