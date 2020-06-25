package com.github.gibmir.ion.api.client.context;

public interface RequestContext {
  String getMethodName();

  String getId();

  byte[] getPayload();

  class DefaultRequestContext implements RequestContext {
    private final String methodName;
    private final String id;
    private final byte[] requestPayload;

    public DefaultRequestContext(String methodName, String id, byte[] requestPayload) {
      this.methodName = methodName;
      this.id = id;
      this.requestPayload = requestPayload;
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
  }

  static RequestContext defaultContext(String methodName, byte[] requestPayload, String id) {
    return new DefaultRequestContext(methodName, id, requestPayload);
  }
}
