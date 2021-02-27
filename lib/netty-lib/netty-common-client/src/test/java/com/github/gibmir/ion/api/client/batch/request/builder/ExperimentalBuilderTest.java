package com.github.gibmir.ion.api.client.batch.request.builder;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class ExperimentalBuilderTest {

  @Test
  void smoke() {
    String expectedString = "a";
    long expectedLong = 1L;
    ExperimentalBatch.from(expectedString).add(expectedLong)
      .process(batchLong -> assertThat(batchLong, is(expectedLong)))
      .next()
      .process(batchString -> assertThat(batchString, is(expectedString)));
  }

  public static class ExperimentalBatch<Current, Previous> {
    private final Current current;
    private final Previous previous;
    private final boolean hasNext;

    private ExperimentalBatch(Current current, Previous previous, boolean hasNext) {
      this.current = current;
      this.previous = previous;
      this.hasNext = hasNext;
    }

    public static <Current> ExperimentalBatch<Current, Void> from(Current current) {
      return new ExperimentalBatch<>(current, null, false);
    }

    public <CT> ExperimentalBatch<CT, ExperimentalBatch<Current, Previous>> add(CT element) {
      return new ExperimentalBatch<>(element, this, true);
    }

    public ExperimentalBatch<Current, Previous> process(Consumer<Current> currentConsumer) {
      currentConsumer.accept(current);
      return this;
    }

    public Previous next() {
      return previous;
    }

    public boolean hasNext() {
      return hasNext;
    }
  }
}
