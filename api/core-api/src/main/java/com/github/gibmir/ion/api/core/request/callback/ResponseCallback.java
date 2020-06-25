package com.github.gibmir.ion.api.core.request.callback;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ResponseCallback<T> {
  void onComplete(T result, Throwable throwable);

  static <T> ResponseCallback<T> as(BiConsumer<T, Throwable> biConsumer) {
    return biConsumer::accept;
  }
}
