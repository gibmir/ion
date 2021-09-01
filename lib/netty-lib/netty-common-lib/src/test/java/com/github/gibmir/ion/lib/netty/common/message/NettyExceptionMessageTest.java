package com.github.gibmir.ion.lib.netty.common.message;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NettyExceptionMessageTest {

  @Test
  void testCreate() {
    String testId = "test-id";
    int testCode = 32;
    String testMessage = "test-message";
    NettyExceptionMessage nettyExceptionMessage = NettyExceptionMessage.create(testId, testCode, testMessage);
    assertThat(nettyExceptionMessage.getId(), is(testId));
    assertThat(nettyExceptionMessage.getCode(), is(testCode));
    assertThat(nettyExceptionMessage.getMessage(), is(testMessage));
  }

  @Test
  void testWithoutId() {
    int testCode = 32;
    String testMessage = "test-message";
    NettyExceptionMessage nettyExceptionMessage = NettyExceptionMessage.withoutId(testCode, testMessage);
    assertThat(nettyExceptionMessage.getId(), is(nullValue()));
    assertThat(nettyExceptionMessage.getCode(), is(testCode));
    assertThat(nettyExceptionMessage.getMessage(), is(testMessage));
  }
}
