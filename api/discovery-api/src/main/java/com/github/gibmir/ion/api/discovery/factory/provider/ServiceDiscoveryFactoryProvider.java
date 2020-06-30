package com.github.gibmir.ion.api.discovery.factory.provider;

import com.github.gibmir.ion.api.discovery.factory.ServiceDiscoveryFactory;

import java.util.ServiceLoader;

public interface ServiceDiscoveryFactoryProvider {
  static ServiceDiscoveryFactoryProvider load() {
    return ServiceLoader.load(ServiceDiscoveryFactoryProvider.class)
      .findFirst().orElseThrow();
  }

  ServiceDiscoveryFactory provide();
}
