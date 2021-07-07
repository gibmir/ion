package com.github.gibmir.ion.api.message;

public interface Message {
  /**
   * @return resolved message type
   */
  MessageType resolveType();

  /**
   * Tries to represent message as notification.
   *
   * @return notification
   */
  NotificationMessage asNotification();

  /**
   * Tries to represent message as request.
   *
   * @return request
   */
  RequestMessage asRequest();

  /**
   * Tries to represent message as batch.
   *
   * @return batch
   */
  BatchMessage asBatch();

  /**
   * Tries to represent message as exception.
   *
   * @return exception
   */
  ExceptionMessage asException();
}
