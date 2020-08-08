package com.github.gibmir.ion.lib.netty.client.sender.codecs.encoder;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JsonRpcRequestEncoderTest {

  @Test
  void testEncodeCorrectByteArray() {
    EmbeddedChannel embeddedChannel = new EmbeddedChannel();
    embeddedChannel.pipeline().addFirst(new JsonRpcRequestEncoder());
    assertDoesNotThrow(() -> embeddedChannel.writeOutbound("pog".getBytes()));
  }
}
