package com.github.gibmir.ion.lib.discovery.etcd.factory.provider;

import com.github.gibmir.ion.api.configuration.Configuration;
import com.github.gibmir.ion.api.configuration.properties.ConfigurationUtils;
import com.github.gibmir.ion.api.configuration.provider.ConfigurationProvider;
import com.github.gibmir.ion.api.discovery.factory.provider.ServiceDiscoveryFactoryProvider;
import com.github.gibmir.ion.lib.discovery.etcd.configuration.EtcdProperties;
import com.github.gibmir.ion.lib.discovery.etcd.factory.EtcdServiceDiscoveryFactory;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.Watch;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class EtcdServiceDiscoveryFactoryProvider implements ServiceDiscoveryFactoryProvider {


  @Override
  public EtcdServiceDiscoveryFactory provide() {
    Configuration configuration = ConfigurationProvider.load().provide();
    Charset charset = configuration.getOptionalValue(ConfigurationUtils.JSONB_ENCODING, String.class).map(Charset::forName)
      .orElse(ConfigurationUtils.DEFAULT_CHARSET);
    List<String> values = configuration.getValues(EtcdProperties.ENDPOINTS, String.class);
    String[] endpoints = new String[values.size()];
    values.toArray(endpoints);
    Client etcdClient = Client.builder().endpoints(endpoints).build();
    KV kvClient = etcdClient.getKVClient();
    Watch watchClient = etcdClient.getWatchClient();
    Jsonb jsonb = ConfigurationUtils.createJsonbWith(configuration);
    return new EtcdServiceDiscoveryFactory(charset, watchClient, kvClient, new ConcurrentHashMap<>(), jsonb);
  }
}
