package com.github.gibmir.ion.lib.nio.server.callback;

import java.util.function.Consumer;
@FunctionalInterface
public interface PayloadCallback extends Consumer<byte[]> {
  void onResponse(byte[] payload);

  @Override
  default void accept(byte[] bytes) {
    onResponse(bytes);
  }
}
