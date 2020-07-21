package com.github.gibmir.ion.api.client.factory.provider;

import com.github.gibmir.ion.api.client.factory.RequestFactory;

import java.util.ServiceLoader;

public interface RequestFactoryProvider {
  static RequestFactoryProvider load() {
    return ServiceLoader.load(RequestFactoryProvider.class).findFirst().orElseThrow();
  }

  RequestFactory provide();
}
