package com.github.gibmir.ion.lib.discovery.etcd;

import com.github.gibmir.ion.api.discovery.ServiceDiscovery;
import com.github.gibmir.ion.api.discovery.event.ApiEvent;
import com.github.gibmir.ion.api.discovery.event.Event;
import com.github.gibmir.ion.api.discovery.event.data.EventData;
import com.github.gibmir.ion.api.discovery.event.type.ApiEventType;
import com.github.gibmir.ion.api.discovery.listener.ServiceListener;
import com.github.gibmir.ion.lib.discovery.etcd.exception.ClosingException;
import com.github.gibmir.ion.lib.discovery.etcd.listener.ListenerEtcdWatchPair;
import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.Watch;
import io.etcd.jetcd.watch.WatchEvent;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EtcdServiceDiscovery implements ServiceDiscovery {
  private final Charset charset;
  private final Watch watchListener;
  private final KV kvClient;
  private final Map<String, ListenerEtcdWatchPair> apiPerListeners;
  private final Jsonb jsonb;

  public EtcdServiceDiscovery(Charset charset, Watch watchClient, KV kvClient,
                              Map<String, ListenerEtcdWatchPair> apiListeners, Jsonb jsonb) {
    this.charset = charset;
    this.watchListener = watchClient;
    this.kvClient = kvClient;
    this.apiPerListeners = apiListeners;
    this.jsonb = jsonb;
  }

  @Override
  public void registerListener(String apiName, ServiceListener<Event> listener) {
    Watch.Watcher watcher = watchListener.watch(ByteSequence.from(apiName, charset)
      , watchResponse -> parseEvents(apiName, watchResponse.getEvents(), jsonb, charset).forEach(listener::notifyWith/*Event*/));
    apiPerListeners.put(apiName, new ListenerEtcdWatchPair(watcher, listener));
  }

  @Override
  public void registerService(Event event) {
    ApiEventType eventType = event.getApiEventType();
    if (!ApiEventType.PUT.equals(eventType)) {
      String message = String.format("Provided event type [%s] must be [%s] ", eventType, ApiEventType.PUT);
      throw new IllegalArgumentException(message);
    }
    ByteSequence apiNameKey = ByteSequence.from(event.getApiName().getBytes());
    EventData eventData = event.getEventData();
    ByteSequence eventDataValue = ByteSequence.from(jsonb.toJson(eventData).getBytes());
    kvClient.put(apiNameKey, eventDataValue).whenComplete((putResponse, throwable) -> {
      if (putResponse != null) {
        //todo log
      }
      if (throwable != null) {
        //todo log
      }
    });
  }

  @Override
  public void closeListenerFor(String apiName) {
    apiPerListeners.computeIfPresent(apiName, (__, listener) -> {
      try {
        listener.close();
      } catch (Exception e) {
        //todo log exception
      }
      return /*remove key from map*/null;
    });
  }

  private static List<Event> parseEvents(String listenerApiName, List<WatchEvent> watchEvents,
                                         Jsonb jsonb, Charset charset) {
    List<Event> apiEvents = new ArrayList<>();
    for (WatchEvent watchEvent : watchEvents) {
      apiEvents.add(parseEvent(listenerApiName, watchEvent, jsonb, charset));
    }
    return apiEvents;
  }

  private static Event parseEvent(String listenerApiName, WatchEvent watchEvent, Jsonb jsonb, Charset charset) {
    ApiEventType apiEventType = resolveEventType(watchEvent.getEventType());
    KeyValue keyValue = watchEvent.getKeyValue();
    ByteSequence keyByteSequence = keyValue.getKey();
    ByteSequence valueByteSequence = keyValue.getValue();
    if (keyByteSequence == null || valueByteSequence == null) {
      throw new IllegalArgumentException(watchEvent + " is incorrect event for listener of API:" + listenerApiName);
    }
    String apiName = keyByteSequence.toString(charset);
    String valueJson = valueByteSequence.toString(charset);
    EventData eventData = jsonb.fromJson(valueJson, EventData.class);
    return new ApiEvent(apiName, eventData, apiEventType);
  }

  private static ApiEventType resolveEventType(WatchEvent.EventType eventType) {
    switch (eventType) {
      case PUT:
        return ApiEventType.PUT;
      case DELETE:
        return ApiEventType.DELETE;
      case UNRECOGNIZED:
      default:
        return ApiEventType.UNRECOGNIZED;
    }
  }

  @Override
  public void close() {
    List<Exception> suppressed = closeDiscovery();
    apiPerListeners.clear();
    if (suppressed.size() > 0) {
      ClosingException closingException = new ClosingException("Exceptions occurred while closing service discovery");
      suppressed.forEach(closingException::addSuppressed);
      throw closingException;
    }
  }

  private List<Exception> closeDiscovery() {
    List<Exception> suppressed = new ArrayList<>();
    for (ListenerEtcdWatchPair listener : apiPerListeners.values()) {
      try {
        listener.close();
      } catch (Exception e) {
        suppressed.add(e);
      }
    }
    return suppressed;
  }
}
