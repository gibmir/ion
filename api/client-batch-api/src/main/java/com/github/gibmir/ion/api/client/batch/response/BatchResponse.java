package com.github.gibmir.ion.api.client.batch.response;

import com.github.gibmir.ion.api.client.batch.response.element.BatchElement;

import java.util.List;

public class BatchResponse {

  private final List<BatchElement> batchElements;

  public BatchResponse(List<BatchElement> batchElements) {
    this.batchElements = batchElements;
  }

  public List<BatchElement> getBatchResponseElements() {
    return batchElements;
  }
}
