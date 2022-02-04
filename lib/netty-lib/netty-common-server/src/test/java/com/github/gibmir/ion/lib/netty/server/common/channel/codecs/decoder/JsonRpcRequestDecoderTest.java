package com.github.gibmir.ion.lib.netty.server.common.channel.codecs.decoder;

import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JsonRpcRequestDecoderTest {

  @Test
  @SuppressWarnings("All")
  void testDecode() {
    Jsonb jsonb = mock(Jsonb.class);
    JsonRpcRequestDecoder decoder = new JsonRpcRequestDecoder(jsonb, StandardCharsets.UTF_8);
    EmbeddedChannel channel = new EmbeddedChannel(decoder);
    byte[] payload = new byte[]{1, 2, 3, 4, 5};
    doAnswer(__ -> "some result").when(jsonb).fromJson(eq(new String(payload, StandardCharsets.UTF_8)), eq(JsonValue.class));
    channel.writeInbound(payload);
    verify(jsonb).fromJson(eq(new String(payload, StandardCharsets.UTF_8)), eq(JsonValue.class));
  }
}
