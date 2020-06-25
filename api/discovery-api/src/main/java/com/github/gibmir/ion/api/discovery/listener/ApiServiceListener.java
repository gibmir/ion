package com.github.gibmir.ion.api.discovery.listener;

import com.github.gibmir.ion.api.discovery.event.Event;

import java.util.function.Consumer;

public class ApiServiceListener implements ServiceListener<Event> {
  private final Consumer<Event> listenerLogic;

  public ApiServiceListener(Consumer<Event> listenerLogic) {
    this.listenerLogic = listenerLogic;
  }

  @Override
  public void notifyWith(Event event) {
    listenerLogic.accept(event);
  }

  @Override
  public void close() {

  }
}
