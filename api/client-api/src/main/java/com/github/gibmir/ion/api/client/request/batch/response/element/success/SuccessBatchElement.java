package com.github.gibmir.ion.api.client.request.batch.response.element.success;

import com.github.gibmir.ion.api.client.request.batch.response.element.BatchElement;
import com.github.gibmir.ion.api.client.request.batch.response.element.type.BatchElementType;

public class SuccessBatchElement implements BatchElement {
  private final String id;
  private final Object result;

  public SuccessBatchElement(String id, Object result) {
    this.id = id;
    this.result = result;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Object getResponseObject() {
    return result;
  }


  @Override
  public BatchElementType resolveType() {
    return BatchElementType.SUCCESS;
  }
}
