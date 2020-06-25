package com.github.gibmir.ion.api.discovery.event;

import com.github.gibmir.ion.api.discovery.event.data.EventData;
import com.github.gibmir.ion.api.discovery.event.type.ApiEventType;

public class ApiEvent implements Event {
  private final String apiName;
  private final ApiEventType apiEventType;
  private final EventData eventData;

  public ApiEvent(String apiName, EventData eventData, ApiEventType apiEventType) {
    this.apiName = apiName;
    this.eventData = eventData;
    this.apiEventType = apiEventType;
  }

  @Override
  public String getApiName() {
    return apiName;
  }

  @Override
  public ApiEventType getApiEventType() {
    return apiEventType;
  }

  @Override
  public EventData getEventData() {
    return eventData;
  }
}
