package com.github.gibmir.ion.api.client.request.factory.provider;

import com.github.gibmir.ion.api.client.request.factory.RequestFactory;

import java.util.ServiceLoader;

public interface RequestFactoryProvider {
  static RequestFactoryProvider load() {
    return ServiceLoader.load(RequestFactoryProvider.class).findFirst().orElseThrow();
  }

  RequestFactory provide();
}
