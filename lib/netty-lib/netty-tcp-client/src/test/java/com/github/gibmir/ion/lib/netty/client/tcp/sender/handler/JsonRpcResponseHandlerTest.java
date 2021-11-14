package com.github.gibmir.ion.lib.netty.client.tcp.sender.handler;

import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.tcp.environment.TestEnvironment;
import com.github.gibmir.ion.lib.netty.client.tcp.environment.mock.ResponseListenerRegistryMock;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.jupiter.api.Test;

import javax.json.JsonValue;

import static com.github.gibmir.ion.lib.netty.client.tcp.environment.TestEnvironment.TEST_REAL_JSONB;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class JsonRpcResponseHandlerTest {

  @Test
  void testSuccessRead() {
    EmbeddedChannel embeddedChannel = new EmbeddedChannel();
    ResponseListenerRegistry responseListenerRegistry = ResponseListenerRegistryMock.emptyMock();

    embeddedChannel.pipeline().addFirst(new JsonRpcResponseHandler(responseListenerRegistry));
    JsonValue testCorrectJson = TEST_REAL_JSONB.fromJson("{\"key\":\"value\"}", JsonValue.class);
    embeddedChannel.writeInbound(testCorrectJson);
    verify(responseListenerRegistry, times(1)).notifyListenerWith(testCorrectJson);
  }

  /**
   * Exception won't be thrown.
   *
   * @see JsonRpcResponseHandler#exceptionCaught(ChannelHandlerContext, Throwable)
   */
  @Test
  void testReadWithExceptionally() {
    EmbeddedChannel embeddedChannel = new EmbeddedChannel();
    ResponseListenerRegistry responseListenerRegistry = ResponseListenerRegistryMock.emptyMock();
    doThrow(TestEnvironment.TestException.class).when(responseListenerRegistry).notifyListenerWith(any());
    embeddedChannel.pipeline().addFirst(new JsonRpcResponseHandler(responseListenerRegistry));
    JsonValue testCorrectJson = TEST_REAL_JSONB.fromJson("{\"key\":\"value\"}", JsonValue.class);
    embeddedChannel.writeInbound(testCorrectJson);
    verify(responseListenerRegistry, times(1)).notifyListenerWith(testCorrectJson);
  }
}
