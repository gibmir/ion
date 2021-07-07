package com.github.gibmir.ion.api.client.batch.stack;

import java.util.function.Consumer;

public final class StackedBatchResponse<T> {
  private final T t;

  private StackedBatchResponse(final T t) {
    this.t = t;
  }

  /**
   * Static factory.
   *
   * @param object object
   * @param <T>    object type
   * @return stack
   */
  public static <T> StackedBatchResponse<T> from(final T object) {
    return new StackedBatchResponse<>(object);
  }

  /**
   * @return result
   */
  public T resultAsStack() {
    return t;
  }

  /**
   * Append to stack.
   *
   * @param ct   companion
   * @param <CT> companion type
   * @return stack
   */
  public <CT> StackedBatchResponse<Element<T, CT>> append(final CT ct) {
    return StackedBatchResponse.from(new Element<>(t, ct));
  }

  public static class Element<A, B> {
    private final A a;
    private final B b;

    public Element(final A a, final B b) {
      this.a = a;
      this.b = b;
    }

    /**
     * Process result.
     *
     * @param bConsumer consumer to process result
     * @return next result
     */
    public A processResult(final Consumer<B> bConsumer) {
      bConsumer.accept(b);
      return a;
    }

    /**
     * Process last result.
     *
     * @param bConsumer first consumer
     * @param aConsumer last consumer
     */
    public void endProcessing(final Consumer<B> bConsumer, final Consumer<A> aConsumer) {
      bConsumer.accept(b);
      aConsumer.accept(a);
    }
  }
}
