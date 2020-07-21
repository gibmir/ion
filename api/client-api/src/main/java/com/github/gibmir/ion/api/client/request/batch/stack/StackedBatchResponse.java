package com.github.gibmir.ion.api.client.request.batch.stack;

import java.util.function.Consumer;

public class StackedBatchResponse<T> {
  private final T t;

  private StackedBatchResponse(T t) {
    this.t = t;
  }

  public static <T> StackedBatchResponse<T> from(T object) {
    return new StackedBatchResponse<>(object);
  }

  public T resultAsStack() {
    return t;
  }

  public <CT> StackedBatchResponse<Element<T, CT>> append(CT ct) {
    return StackedBatchResponse.from(new Element<>(t, ct));
  }

  public static class Element<A, B> {
    private final A a;
    private final B b;

    public Element(A a, B b) {
      this.a = a;
      this.b = b;
    }

    public A processResult(Consumer<B> bConsumer) {
      bConsumer.accept(b);
      return a;
    }

    public void endProcessing(Consumer<B> bConsumer, Consumer<A> aConsumer) {
      bConsumer.accept(b);
      aConsumer.accept(a);
    }
  }
}
