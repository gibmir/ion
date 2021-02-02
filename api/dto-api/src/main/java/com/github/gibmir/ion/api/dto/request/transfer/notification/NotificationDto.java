package com.github.gibmir.ion.api.dto.request.transfer.notification;

import com.github.gibmir.ion.api.dto.request.transfer.AbstractJsonRpcRequest;

import java.util.Map;

public class NotificationDto extends AbstractJsonRpcRequest {
  private static final Object[] EMPTY_PAYLOAD = new Object[0];

  public NotificationDto() {
    super();
  }

  /**
   * <b>use static fabrics to prevent types miscast</b>
   *
   * @see NotificationDto#positional(String, Object[])
   * @see NotificationDto#named(String, Map)
   * @see NotificationDto#empty(String)
   */
  public NotificationDto(String procedureName, Object[] args) {
    super(procedureName, args);
  }

  private NotificationDto(String procedureName, Object args) {
    super(procedureName, args);
  }

  public static NotificationDto named(String methodName, Map<String, Object> namedArgs) {
    return new NotificationDto(methodName, namedArgs);
  }

  public static NotificationDto positional(String methodName, Object[] positionalArgs) {
    return new NotificationDto(methodName, positionalArgs);
  }

  public static NotificationDto empty(String methodName) {
    return new NotificationDto(methodName, EMPTY_PAYLOAD);
  }
}
