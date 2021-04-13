package com.github.gibmir.ion.lib.netty.common.message;

public class NettyAbstractMessage {
  protected final String method;
  protected final String argumentsJson;

  public NettyAbstractMessage(String method, String argumentsJson) {
    this.method = method;
    this.argumentsJson = argumentsJson;
  }
}
