package com.github.gibmir.ion.lib.netty.common.configuration.ssl;

import io.netty.handler.ssl.SslProvider;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class NettySslProviderTest {
  @Test
  void smoke() {
    assertThat(NettySslProvider.JDK.get(), equalTo(SslProvider.JDK));
    assertThat(NettySslProvider.OPENSSL.get(), equalTo(SslProvider.OPENSSL));
    assertThat(NettySslProvider.OPENSSL_REFCNT.get(), equalTo(SslProvider.OPENSSL_REFCNT));
    assertThat(NettySslProvider.DISABLED.get(), nullValue());
  }
}
