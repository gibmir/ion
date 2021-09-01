package com.github.gibmir.ion.lib.netty.common.message;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class NettyNotificationMessageTest {

  @Test
  void smoke() {
    String testMethod = "test-method";
    String testArguments = "testArguments";
    NettyNotificationMessage notificationMessage = new NettyNotificationMessage(testMethod, testArguments);

    assertThat(notificationMessage.getMethodName(), is(testMethod));
    assertThat(notificationMessage.getArgumentsJson(), is(testArguments));
  }
}
