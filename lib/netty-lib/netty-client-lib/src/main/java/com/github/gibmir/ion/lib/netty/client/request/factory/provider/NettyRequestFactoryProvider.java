package com.github.gibmir.ion.lib.netty.client.request.factory.provider;

import com.github.gibmir.ion.api.client.request.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.lib.netty.client.request.factory.NettyRequestFactory;

public class NettyRequestFactoryProvider implements RequestFactoryProvider {
  @Override
  public NettyRequestFactory provide() {
    //todo initialize with config
    return null;
  }
}
