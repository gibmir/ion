package com.github.gibmir.ion.lib.discovery.etcd.listener;

import com.github.gibmir.ion.api.discovery.event.Event;
import com.github.gibmir.ion.api.discovery.listener.ServiceListener;
import io.etcd.jetcd.Watch;

public class ListenerEtcdWatchPair implements AutoCloseable {
  private final Watch.Watcher watcher;
  private final ServiceListener<Event> serviceListener;

  public ListenerEtcdWatchPair(Watch.Watcher watcher, ServiceListener<Event> serviceListener) {
    this.watcher = watcher;
    this.serviceListener = serviceListener;
  }

  @Override
  public void close() throws Exception {
    serviceListener.close();
    watcher.close();
  }
}
