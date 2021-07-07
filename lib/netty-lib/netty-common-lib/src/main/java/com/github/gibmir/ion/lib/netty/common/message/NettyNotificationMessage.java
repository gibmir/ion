package com.github.gibmir.ion.lib.netty.common.message;

import com.github.gibmir.ion.api.message.NotificationMessage;

public class NettyNotificationMessage extends NettyAbstractMessage implements NotificationMessage {

  public NettyNotificationMessage(final String method, final String argumentsJson) {
    super(method, argumentsJson);
  }
}
