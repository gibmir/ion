package com.github.gibmir.ion.lib.netty.common.message;

import com.github.gibmir.ion.api.message.ExceptionMessage;

public final class NettyExceptionMessage implements ExceptionMessage {
  private final String id;
  private final int code;
  private final String message;

  private NettyExceptionMessage(final String id, final int code, final String message) {
    this.id = id;
    this.code = code;
    this.message = message;
  }

  /**
   * Static factory. Creates exception with id.
   *
   * @param id      request id
   * @param code    exception code
   * @param message exception message
   * @return exception
   */
  public static NettyExceptionMessage create(final String id, final int code, final String message) {
    return new NettyExceptionMessage(id, code, message);
  }

  /**
   * Static factory. Creates exception without id.
   *
   * @param code    exception code
   * @param message exception message
   * @return exception
   */
  public static NettyExceptionMessage withoutId(final int code, final String message) {
    return new NettyExceptionMessage(null, code, message);
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public int getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
