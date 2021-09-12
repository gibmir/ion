package com.github.gibmir.ion.lib.netty.client.common.channel.codecs.encoder;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

class JsonRpcRequestEncoderTest {

  @Test
  void smoke() {
    JsonRpcRequestEncoder jsonRpcRequestEncoder = new JsonRpcRequestEncoder();
    EmbeddedChannel embeddedChannel = new EmbeddedChannel(jsonRpcRequestEncoder);

    String testMessage = "testMessage";
    Object bytes = testMessage.getBytes(StandardCharsets.UTF_8);
    embeddedChannel.writeOutbound(bytes);

    Object result = embeddedChannel.readOutbound();
    assertThat(result, notNullValue());
  }
}
