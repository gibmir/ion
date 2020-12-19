package com.github.gibmir.ion.lib.netty.common.exceptions;

public class NettyInitializationException extends RuntimeException {
  public NettyInitializationException(String message) {
    super(message);
  }

  public NettyInitializationException(String message, Throwable cause) {
    super(message, cause);
  }
}
