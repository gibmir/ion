package com.github.gibmir.ion.lib.netty.common.message;

import com.github.gibmir.ion.api.message.NotificationMessage;

public class NettyNotificationMessage extends NettyAbstractMessage implements NotificationMessage {

  public NettyNotificationMessage(String method, String argumentsJson) {
    super(method, argumentsJson);
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
