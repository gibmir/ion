package com.github.gibmir.ion.api.dto.request.transfer.notification;

import com.github.gibmir.ion.api.dto.request.transfer.AbstractJsonRpcRequest;

import java.util.Map;

public class NotificationDto extends AbstractJsonRpcRequest {
  private static final Object[] EMPTY_PAYLOAD = new Object[0];

  public NotificationDto() {
    super();
  }

  /**
   * <b>use static fabrics to prevent types miscast</b>.
   *
   * @see NotificationDto#positional(String, Object[])
   * @see NotificationDto#named(String, Map)
   * @see NotificationDto#empty(String)
   */
  public NotificationDto(final String procedureName, final Object[] args) {
    super(procedureName, args);
  }

  private NotificationDto(final String procedureName, final Object args) {
    super(procedureName, args);
  }

  /**
   * Static factory for named method parameters.
   *
   * @param methodName method name
   * @param namedArgs  arguments
   * @return notification
   */
  public static NotificationDto named(final String methodName, final Map<String, Object> namedArgs) {
    return new NotificationDto(methodName, namedArgs);
  }

  /**
   * Static factory for positional method parameters.
   *
   * @param methodName     method name
   * @param positionalArgs arguments
   * @return notification
   */
  public static NotificationDto positional(final String methodName, final Object[] positionalArgs) {
    return new NotificationDto(methodName, positionalArgs);
  }

  /**
   * Static factory for method without parameters.
   *
   * @param methodName method name
   * @return notification
   */
  public static NotificationDto empty(final String methodName) {
    return new NotificationDto(methodName, EMPTY_PAYLOAD);
  }
}
