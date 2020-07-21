package com.github.gibmir.ion.api.client.request.batch.response.element;

import com.github.gibmir.ion.api.client.request.batch.response.element.type.BatchElementType;

public interface BatchElement {
  String getId();

  Object getResponseObject();

  BatchElementType resolveType();
}
