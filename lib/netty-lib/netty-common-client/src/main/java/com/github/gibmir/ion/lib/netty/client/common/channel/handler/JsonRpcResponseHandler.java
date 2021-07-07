package com.github.gibmir.ion.lib.netty.client.common.channel.handler;

import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonValue;

public final class JsonRpcResponseHandler extends ChannelInboundHandlerAdapter {
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonRpcResponseHandler.class);
  private final ResponseListenerRegistry responseListenerRegistry;

  public JsonRpcResponseHandler(final ResponseListenerRegistry responseListenerRegistry) {
    this.responseListenerRegistry = responseListenerRegistry;
  }


  @Override
  public void channelRead(final ChannelHandlerContext ctx, final Object msg) {
    if (msg instanceof JsonValue) {
      responseListenerRegistry.notifyListenerWith((JsonValue) msg);
    }
  }

  @Override
  public void channelReadComplete(final ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    LOGGER.error("Exception occurred while handling result.", cause);
    ctx.close();
  }
}
