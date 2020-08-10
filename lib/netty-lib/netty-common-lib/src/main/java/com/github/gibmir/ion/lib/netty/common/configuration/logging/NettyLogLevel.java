package com.github.gibmir.ion.lib.netty.common.configuration.logging;

import io.netty.handler.logging.LogLevel;

import java.util.function.Supplier;

public enum NettyLogLevel implements Supplier<LogLevel> {
  TRACE(LogLevel.TRACE),
  DEBUG(LogLevel.DEBUG),
  INFO(LogLevel.INFO),
  WARN(LogLevel.WARN),
  ERROR(LogLevel.ERROR),
  DISABLED(LogLevel.ERROR);
  private final LogLevel logLevel;

  NettyLogLevel(LogLevel logLevel) {
    this.logLevel = logLevel;
  }

  @Override
  public LogLevel get() {
    return logLevel;
  }
}
