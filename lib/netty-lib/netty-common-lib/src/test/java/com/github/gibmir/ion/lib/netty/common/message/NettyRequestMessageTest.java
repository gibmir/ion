package com.github.gibmir.ion.lib.netty.common.message;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class NettyRequestMessageTest {

  @Test
  void smoke() {
    String testArguments = "testArguments";
    String testMethod = "testMethod";
    String testId = "testId";
    NettyRequestMessage requestMessage = new NettyRequestMessage(testId, testMethod, testArguments);
    assertThat(requestMessage.getId(), is(testId));
    assertThat(requestMessage.getMethodName(), is(testMethod));
    assertThat(requestMessage.getArgumentsJson(), is(testArguments));
  }
}
