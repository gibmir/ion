package com.github.gibmir.ion.api.discovery.listener;

import com.github.gibmir.ion.api.discovery.event.Event;

public interface ServiceListener<E extends Event> extends AutoCloseable {

  void notifyWith(E event);

}
