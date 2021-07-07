package com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry;

import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;

import javax.json.JsonValue;

public interface ResponseListenerRegistry {
  /**
   * Registers future response.
   *
   * @param responseFuture future response
   */
  void register(ResponseFuture responseFuture);

  /**
   * Notifies future with specified json response.
   *
   * @param jsonValue json response
   */
  void notifyListenerWith(JsonValue jsonValue);
}
