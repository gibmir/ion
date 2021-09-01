package com.github.gibmir.ion.lib.netty.common.message;

import com.github.gibmir.ion.api.message.Message;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

class NettyBatchMessageTest {

  @Test
  void smoke() {
    Message[] batchMessages = new Message[]{};
    NettyBatchMessage nettyBatchMessage = new NettyBatchMessage(batchMessages);
    assertThat(nettyBatchMessage.getMessages(), is(batchMessages));
  }
}
