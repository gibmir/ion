package com.github.gibmir.ion.lib.netty.common.configuration.logging;

import io.netty.handler.logging.LogLevel;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

class NettyLogLevelTest {
  @Test
  void smoke() {
    assertThat(NettyLogLevel.DEBUG.get(), equalTo(LogLevel.DEBUG));
    assertThat(NettyLogLevel.ERROR.get(), equalTo(LogLevel.ERROR));
    assertThat(NettyLogLevel.TRACE.get(), equalTo(LogLevel.TRACE));
    assertThat(NettyLogLevel.INFO.get(), equalTo(LogLevel.INFO));
    assertThat(NettyLogLevel.WARN.get(), equalTo(LogLevel.WARN));
    assertThat(NettyLogLevel.DISABLED.get(), nullValue());
  }
}
