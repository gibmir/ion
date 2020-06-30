package com.github.gibmir.ion.lib.discovery.etcd.factory;

import com.github.gibmir.ion.api.discovery.factory.ServiceDiscoveryFactory;
import com.github.gibmir.ion.lib.discovery.etcd.EtcdServiceDiscovery;
import com.github.gibmir.ion.lib.discovery.etcd.listener.ListenerEtcdWatchPair;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.Watch;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.Map;

public class EtcdServiceDiscoveryFactory implements ServiceDiscoveryFactory {
  private final Charset charset;
  private final Watch watchClient;
  private final KV kvClient;
  private final Map<String, ListenerEtcdWatchPair> apiListeners;
  private final Jsonb jsonb;

  public EtcdServiceDiscoveryFactory(Charset charset, Watch watchClient, KV kvClient,
                                     Map<String, ListenerEtcdWatchPair> apiListeners, Jsonb jsonb) {
    this.charset = charset;
    this.watchClient = watchClient;
    this.kvClient = kvClient;
    this.apiListeners = apiListeners;
    this.jsonb = jsonb;
  }

  @Override
  public EtcdServiceDiscovery create() {
    return new EtcdServiceDiscovery(charset, watchClient, kvClient, apiListeners, jsonb);
  }
}
