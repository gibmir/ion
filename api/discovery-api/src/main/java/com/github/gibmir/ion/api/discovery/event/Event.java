package com.github.gibmir.ion.api.discovery.event;

import com.github.gibmir.ion.api.discovery.event.data.EventData;
import com.github.gibmir.ion.api.discovery.event.type.ApiEventType;

public interface Event {
  String getApiName();

  ApiEventType getApiEventType();

  EventData getEventData();
}
