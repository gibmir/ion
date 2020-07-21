package com.github.gibmir.ion.api.dto.request.transfer.notification;

import com.github.gibmir.ion.api.dto.request.transfer.AbstractJsonRpcRequest;

public class NotificationDto extends AbstractJsonRpcRequest {
  public NotificationDto() {
  }

  public NotificationDto(String procedureName, Object[] args) {
    super(procedureName, args);
  }

}
