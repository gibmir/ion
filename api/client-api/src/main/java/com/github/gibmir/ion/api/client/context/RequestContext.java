package com.github.gibmir.ion.api.client.context;

import java.util.concurrent.TimeUnit;

public interface RequestContext {
  String getMethodName();

  String getId();

  byte[] getPayload();

  long getTimeout();

  TimeUnit getTimeUnit();

  class DefaultRequestContext implements RequestContext {
    private final String methodName;
    private final String id;
    private final byte[] requestPayload;
    private final long timeout;
    private final TimeUnit timeUnit;

    public DefaultRequestContext(String methodName, String id, byte[] requestPayload, long timeout, TimeUnit timeUnit) {
      this.methodName = methodName;
      this.id = id;
      this.requestPayload = requestPayload;
      this.timeout = timeout;
      this.timeUnit = timeUnit;
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
    public long getTimeout() {
      return timeout;
    }

    @Override
    public TimeUnit getTimeUnit() {
      return timeUnit;
    }
  }

  static RequestContext defaultContext(String methodName, byte[] requestPayload, String id, long timeout,
                                       TimeUnit timeUnit) {
    return new DefaultRequestContext(methodName, id, requestPayload, timeout, timeUnit);
  }
}
