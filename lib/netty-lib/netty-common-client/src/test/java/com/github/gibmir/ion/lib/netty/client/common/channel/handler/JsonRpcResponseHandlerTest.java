package com.github.gibmir.ion.lib.netty.client.common.channel.handler;

import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class JsonRpcResponseHandlerTest {
  @Test
  void smokeChannelReadComplete() {
    ResponseListenerRegistry registry = mock(ResponseListenerRegistry.class);
    JsonRpcResponseHandler jsonRpcResponseHandler = new JsonRpcResponseHandler(registry);

    ChannelHandlerContext context = mock(ChannelHandlerContext.class);
    jsonRpcResponseHandler.channelReadComplete(context);
    verify(context).flush();
  }

  @Test
  void smokeExceptionCaught() {
    ResponseListenerRegistry registry = mock(ResponseListenerRegistry.class);
    JsonRpcResponseHandler jsonRpcResponseHandler = new JsonRpcResponseHandler(registry);

    ChannelHandlerContext context = mock(ChannelHandlerContext.class);
    jsonRpcResponseHandler.exceptionCaught(context, new TestException());
    verify(context).close();
  }

  public static class TestException extends RuntimeException {

  }
}
