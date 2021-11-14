package com.github.gibmir.ion.lib.netty.client.tcp.request.factory.provider;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;

class NettyTcpRequestFactoryProviderTest {

  @Test
  void smoke() {
    NettyTcpRequestFactoryProvider nettyTcpRequestFactoryProvider = new NettyTcpRequestFactoryProvider();
    // there is no config module in class path
    assertThrows(NoSuchElementException.class, nettyTcpRequestFactoryProvider::provide);
  }
}
