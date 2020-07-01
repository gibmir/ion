package com.github.gibmir.ion.api.client.context;

import java.time.Duration;

public interface RequestContext {
  String getMethodName();

  String getId();

  byte[] getPayload();

  Duration getTimeout();

  class DefaultRequestContext implements RequestContext {
    private final String methodName;
    private final String id;
    private final byte[] requestPayload;
    private final Duration timeout;

    public DefaultRequestContext(String methodName, String id, byte[] requestPayload, Duration timeout) {
      this.methodName = methodName;
      this.id = id;
      this.requestPayload = requestPayload;
      this.timeout = timeout;
    }

    @Override
    public String getMethodName() {
      return methodName;
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public byte[] getPayload() {
      return requestPayload;
    }

    @Override
    public Duration getTimeout() {
      return timeout;
    }

  }

  static RequestContext defaultContext(String methodName, byte[] requestPayload, String id, Duration timeout) {
    return new DefaultRequestContext(methodName, id, requestPayload, timeout);
  }
}
