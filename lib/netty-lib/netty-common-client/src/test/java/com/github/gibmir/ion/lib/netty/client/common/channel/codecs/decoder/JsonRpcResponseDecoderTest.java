package com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JsonRpcResponseDecoderTest {

  @Test
  void smoke() {
    Jsonb jsonb = mock(Jsonb.class);
    doAnswer(__ -> "output").when(jsonb).fromJson(anyString(), any());
    JsonRpcResponseDecoder jsonRpcResponseDecoder = new JsonRpcResponseDecoder(jsonb, StandardCharsets.UTF_8);
    EmbeddedChannel channel = new EmbeddedChannel(jsonRpcResponseDecoder);
    String testMessage = "testMessage";
    Object bytes = testMessage.getBytes(StandardCharsets.UTF_8);
    channel.writeInbound(bytes);

    verify(jsonb).fromJson(eq(testMessage), eq(JsonValue.class));
  }
}
