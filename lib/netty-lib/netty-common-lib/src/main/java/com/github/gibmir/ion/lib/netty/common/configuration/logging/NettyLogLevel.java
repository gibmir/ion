package com.github.gibmir.ion.lib.netty.common.configuration.logging;

import io.netty.handler.logging.LogLevel;

import java.util.function.Supplier;

public enum NettyLogLevel implements Supplier<LogLevel> {
  @SuppressWarnings("unused")
  TRACE(LogLevel.TRACE),
  @SuppressWarnings("unused")
  DEBUG(LogLevel.DEBUG),
  @SuppressWarnings("unused")
  INFO(LogLevel.INFO),
  @SuppressWarnings("unused")
  WARN(LogLevel.WARN),
  @SuppressWarnings("unused")
  ERROR(LogLevel.ERROR),
  /**
   * Provides disabled log level.
   *
   * @implNote You shouldn't create {@link io.netty.handler.logging.LoggingHandler} if this log level was resolved
   */
  DISABLED(null);
  private final LogLevel logLevel;

  NettyLogLevel(final LogLevel logLevel) {
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
