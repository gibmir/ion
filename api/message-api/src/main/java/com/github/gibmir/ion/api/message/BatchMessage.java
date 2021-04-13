package com.github.gibmir.ion.api.message;

public interface BatchMessage extends Message {
  Message[] getMessages();

  @Override
  default MessageType resolveType() {
    return MessageType.BATCH;
  }

  @Override
  default RequestMessage asRequest() {
    throw new IllegalStateException("Can't represent batch as request");
  }

  @Override
  default NotificationMessage asNotification() {
    throw new IllegalStateException("Can't represent batch as notification");
  }

  @Override
  default ExceptionMessage asException() {
    throw new IllegalStateException("Can't represent batch as exception");
  }

  @Override
  default BatchMessage asBatch() {
    return this;
  }
}
