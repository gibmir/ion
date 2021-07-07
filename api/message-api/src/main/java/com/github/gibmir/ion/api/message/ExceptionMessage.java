package com.github.gibmir.ion.api.message;

public interface ExceptionMessage extends Message {
  /**
   * @return request id
   */
  String getId();

  /**
   * @return exception code
   */
  int getCode();

  /**
   * @return exception message
   */
  String getMessage();

  @Override
  default RequestMessage asRequest() {
    throw new IllegalStateException("Can't represent exception as notification");
  }

  @Override
  default NotificationMessage asNotification() {
    throw new IllegalStateException("Can't represent exception as notification");
  }

  @Override
  default BatchMessage asBatch() {
    throw new IllegalStateException("Can't represent exception as batch");
  }

  @Override
  default ExceptionMessage asException() {
    return this;
  }

  @Override
  default MessageType resolveType() {
    return MessageType.EXCEPTION;
  }
}
