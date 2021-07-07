package com.github.gibmir.ion.lib.netty.common.message;

import com.github.gibmir.ion.api.message.RequestMessage;

public final class NettyRequestMessage extends NettyAbstractMessage implements RequestMessage {
  private final String id;

  public NettyRequestMessage(final String id, final String method, final String argumentsJson) {
    super(method, argumentsJson);
    this.id = id;
  }

  @Override
  public String getId() {
    return id;
  }
}
