package com.github.gibmir.ion.api.client.batch.request;

import com.github.gibmir.ion.api.client.batch.response.BatchResponse;

import java.util.concurrent.CompletableFuture;

public interface BatchRequest {

  CompletableFuture<BatchResponse> batchCall();
}
