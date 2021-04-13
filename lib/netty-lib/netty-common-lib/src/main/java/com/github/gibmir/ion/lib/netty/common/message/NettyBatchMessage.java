package com.github.gibmir.ion.lib.netty.common.message;

import com.github.gibmir.ion.api.message.BatchMessage;
import com.github.gibmir.ion.api.message.Message;

public class NettyBatchMessage implements BatchMessage {
  private final Message[] messages;

  public NettyBatchMessage(Message[] messages) {
    this.messages = messages;
  }

  @Override
  public Message[] getMessages() {
    return messages;
  }
}
