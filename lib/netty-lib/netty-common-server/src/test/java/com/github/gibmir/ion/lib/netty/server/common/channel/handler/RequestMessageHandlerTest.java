package com.github.gibmir.ion.lib.netty.server.common.channel.handler;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.message.Message;
import com.github.gibmir.ion.api.server.processor.request.registry.ProcedureProcessorRegistry;
import com.github.gibmir.ion.lib.netty.server.common.processor.ServerProcessor;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RequestMessageHandlerTest {

  @Test
  void smoke() {
    ServerProcessor serverProcessor = mock(ServerProcessor.class);
    RequestMessageHandler handler = new RequestMessageHandler(serverProcessor);
    EmbeddedChannel channel = new EmbeddedChannel(handler);
    Message message = mock(Message.class);
    JsonRpcResponse response = mock(JsonRpcResponse.class);
    doAnswer(__ -> response).when(serverProcessor).process(eq(message));

    channel.writeInbound(message);

    verify(serverProcessor).process(eq(message));
  }

}
