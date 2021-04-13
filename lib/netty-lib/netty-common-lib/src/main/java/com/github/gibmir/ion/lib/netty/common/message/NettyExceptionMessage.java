package com.github.gibmir.ion.lib.netty.common.message;

import com.github.gibmir.ion.api.message.ExceptionMessage;

public class NettyExceptionMessage implements ExceptionMessage {
  private final String id;
  private final int code;
  private final String message;

  private NettyExceptionMessage(String id, int code, String message) {
    this.id = id;
    this.code = code;
    this.message = message;
  }

  public static NettyExceptionMessage create(String id, int code, String message) {
    return new NettyExceptionMessage(id, code, message);
  }

  public static NettyExceptionMessage withoutId(int code, String message) {
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
