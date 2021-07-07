package com.github.gibmir.ion.lib.netty.common.exceptions;

public class NettyInitializationException extends RuntimeException {
  public NettyInitializationException(final String message) {
    super(message);
  }

  public NettyInitializationException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
