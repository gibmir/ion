package com.github.gibmir.ion.api.discovery;

import com.github.gibmir.ion.api.discovery.event.Event;
import com.github.gibmir.ion.api.discovery.listener.ServiceListener;

public interface ServiceDiscovery extends AutoCloseable {
  void registerListener(String apiName, ServiceListener<Event> listener);

  void registerService(Event event);

  void closeListenerFor(String apiName);
}
