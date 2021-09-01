package com.github.gibmir.ion.lib.netty.common.message;

public abstract class NettyAbstractMessage {
  private final String method;
  private final String argumentsJson;

  public NettyAbstractMessage(final String method, final String argumentsJson) {
    this.method = method;
    this.argumentsJson = argumentsJson;
  }

  /**
   * @return method name
   */
  public final String getMethodName() {
    return method;
  }

  /**
   * @return arguments json
   */
  public final String getArgumentsJson() {
    return argumentsJson;
  }
}
