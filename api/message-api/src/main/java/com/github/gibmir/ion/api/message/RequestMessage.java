package com.github.gibmir.ion.api.message;

public interface RequestMessage extends Message {
  /**
   * @return request id
   */
  String getId();

  /**
   * @return method name
   */
  String getMethodName();

  /**
   * @return arguments json
   */
  String getArgumentsJson();

  @Override
  default MessageType resolveType() {
    return MessageType.REQUEST;
  }

  @Override
  default RequestMessage asRequest() {
    return this;
  }

  @Override
  default NotificationMessage asNotification() {
    throw new IllegalStateException("Can't represent request as notification");
  }

  @Override
  default BatchMessage asBatch() {
    throw new IllegalStateException("Can't represent request as batch");
  }

  @Override
  default ExceptionMessage asException() {
    throw new IllegalStateException("Can't represent request as exception");
  }
}
