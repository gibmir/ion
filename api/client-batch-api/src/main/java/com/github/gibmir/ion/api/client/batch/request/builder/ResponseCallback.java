package com.github.gibmir.ion.api.client.batch.request.builder;

@FunctionalInterface
public interface ResponseCallback<R> {
  /**
   * @param response            request result
   * @param processingException exception occurred during request processing
   */
  void onResponse(R response, Throwable processingException);
}

