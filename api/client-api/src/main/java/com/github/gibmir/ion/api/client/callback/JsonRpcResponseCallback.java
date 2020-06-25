package com.github.gibmir.ion.api.client.callback;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface JsonRpcResponseCallback extends BiConsumer<byte[], Throwable> {
  void onComplete(byte[] responsePayload, Throwable exception);

  @Override
  default void accept(byte[] bytes, Throwable throwable) {
    onComplete(bytes, throwable);
  }
}
