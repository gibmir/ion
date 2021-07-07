package com.github.gibmir.ion.lib.netty.common.configuration.ssl;

import io.netty.handler.ssl.SslProvider;

import java.util.function.Supplier;

public enum NettySslProvider implements Supplier<SslProvider> {
  DISABLED(null), JDK(SslProvider.JDK), OPENSSL(SslProvider.OPENSSL), OPENSSL_REFCNT(SslProvider.OPENSSL_REFCNT);

  private final SslProvider sslProvider;

  NettySslProvider(final SslProvider sslProvider) {
    this.sslProvider = sslProvider;
  }

  @Override
  public SslProvider get() {
    return sslProvider;
  }
}
