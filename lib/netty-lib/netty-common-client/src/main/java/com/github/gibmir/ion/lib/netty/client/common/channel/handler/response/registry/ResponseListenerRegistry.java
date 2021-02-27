package com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry;

import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;

import javax.json.JsonValue;

public interface ResponseListenerRegistry {
  void register(ResponseFuture responseFuture);

  void notifyListenerWith(JsonValue jsonValue);
}
