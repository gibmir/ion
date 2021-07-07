package com.github.gibmir.ion.api.message;

public interface NotificationMessage extends Message {
  /**
   * @return method name
   */
  String getMethodName();

  /**
   * @return arguments json representation
   */
  String getArgumentsJson();

  @Override
  default MessageType resolveType() {
    return MessageType.NOTIFICATION;
  }

  @Override
  default RequestMessage asRequest() {
    throw new IllegalStateException("Can't represent notification as request");

  }

  @Override
  default NotificationMessage asNotification() {
    return this;
  }

  @Override
  default ExceptionMessage asException() {
    throw new IllegalStateException("Can't represent notification as exception");
  }

  @Override
  default BatchMessage asBatch() {
    throw new IllegalStateException("Can't represent notification as batch");
  }
}
