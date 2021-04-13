package com.github.gibmir.ion.lib.netty.common.message;

import com.github.gibmir.ion.api.message.RequestMessage;

public class NettyRequestMessage extends NettyAbstractMessage implements RequestMessage {
  private final String id;

  public NettyRequestMessage(String id, String method, String argumentsJson) {
    super(method, argumentsJson);
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public String getMethodName() {
    return method;
  }

  @Override
  public String getArgumentsJson() {
    return argumentsJson;
  }
}
