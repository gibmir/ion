package com.github.gibmir.ion.api.message;

public interface Message {

  MessageType resolveType();

  NotificationMessage asNotification();

  RequestMessage asRequest();

  BatchMessage asBatch();

  ExceptionMessage asException();
}
