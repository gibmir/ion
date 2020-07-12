package com.github.gibmir.ion.lib.netty.client.request.factory.provider;

import com.github.gibmir.ion.api.client.request.factory.configuration.RequestConfigurationUtils;
import com.github.gibmir.ion.api.client.request.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.lib.netty.client.configuration.NettyRequestConfigurationUtils;
import com.github.gibmir.ion.lib.netty.client.request.factory.NettyRequestFactory;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;

public class NettyRequestFactoryProvider implements RequestFactoryProvider {
  private static volatile NettyRequestFactory nettyRequestFactoryInstance;

  @Override
  public NettyRequestFactory provide() {
    NettyRequestFactory localInstance = nettyRequestFactoryInstance;
    //double-check singleton
    if (localInstance == null) {
      synchronized (NettyRequestFactory.class) {
        localInstance = nettyRequestFactoryInstance;
        if (localInstance == null) {
          nettyRequestFactoryInstance = localInstance = createNettyRequestFactory();
        }
      }
    }
    return localInstance;
  }

  private NettyRequestFactory createNettyRequestFactory() {
    Configuration configuration = ConfigurationProvider.load().provide();
    return new NettyRequestFactory(new JsonRpcNettySender(NettyRequestConfigurationUtils.resolveLogLevel(configuration),
      NettyRequestConfigurationUtils.resolveChannelClass(configuration),
      NettyRequestConfigurationUtils.createEventLoopGroup(configuration)),
      NettyRequestConfigurationUtils.createSocketAddressWith(configuration),
      ConfigurationUtils.createJsonbWith(configuration),
      RequestConfigurationUtils.readCharsetFrom(configuration));
  }
}
