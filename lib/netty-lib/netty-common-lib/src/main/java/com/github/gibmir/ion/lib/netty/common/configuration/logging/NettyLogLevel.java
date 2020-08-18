package com.github.gibmir.ion.lib.netty.common.configuration.logging;

import io.netty.handler.logging.LogLevel;

import java.util.function.Supplier;
//todo there is a possibility to remove loghandler
public enum NettyLogLevel implements Supplier<LogLevel> {
  TRACE(LogLevel.TRACE),
  DEBUG(LogLevel.DEBUG),
  INFO(LogLevel.INFO),
  WARN(LogLevel.WARN),
  ERROR(LogLevel.ERROR),
  /**
   * Provides disabled log level.
   *
   * @implNote You shouldn't create {@link io.netty.handler.logging.LoggingHandler} if this log level was resolved
   */
  DISABLED(LogLevel.ERROR);
  private final LogLevel logLevel;

  NettyLogLevel(LogLevel logLevel) {
    this.logLevel = logLevel;
  }

  /**
   * Provides {@link LogLevel netty log level enum}.
   *
   * @return netty log level.
   */
  @Override
  public LogLevel get() {
    return logLevel;
  }
}
