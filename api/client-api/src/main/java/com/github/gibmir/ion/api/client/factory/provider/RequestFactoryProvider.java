package com.github.gibmir.ion.api.client.factory.provider;

import com.github.gibmir.ion.api.client.factory.RequestFactory;

import java.util.ServiceLoader;

public interface RequestFactoryProvider {
  /**
   * @return request factory provider
   * @implSpec method uses SPI mechanism
   */
  static RequestFactoryProvider load() {
    return ServiceLoader.load(RequestFactoryProvider.class).findFirst().orElseThrow();
  }

  /**
   * @return request factory
   */
  RequestFactory provide();
}
