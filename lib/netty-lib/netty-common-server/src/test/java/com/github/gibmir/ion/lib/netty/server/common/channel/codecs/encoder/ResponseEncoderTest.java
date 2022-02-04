package com.github.gibmir.ion.lib.netty.server.common.channel.codecs.encoder;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ResponseEncoderTest {
  @Test
  void smoke() {
    Logger logger = mock(Logger.class);
    Jsonb jsonb = mock(Jsonb.class);
    Charset charset = mock(Charset.class);
    ResponseEncoder responseEncoder = new ResponseEncoder(logger, jsonb, charset);
    EmbeddedChannel channel = new EmbeddedChannel(responseEncoder);
    JsonRpcResponse response = mock(JsonRpcResponse.class);

    channel.writeAndFlush(response);

    verify(response).processWith(any());
  }
}
