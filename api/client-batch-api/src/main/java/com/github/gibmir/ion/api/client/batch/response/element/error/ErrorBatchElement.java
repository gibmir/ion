package com.github.gibmir.ion.api.client.batch.response.element.error;

import com.github.gibmir.ion.api.client.batch.response.element.type.BatchElementType;
import com.github.gibmir.ion.api.client.batch.response.element.BatchElement;

public class ErrorBatchElement implements BatchElement {
  private final String id;
  private final Throwable throwable;

  public ErrorBatchElement(String id, Throwable throwable) {
    this.id = id;
    this.throwable = throwable;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Throwable getResponseObject() {
    return throwable;
  }

  @Override
  public BatchElementType resolveType() {
    return BatchElementType.ERROR;
  }
}
