package com.github.gibmir.ion.lib.netty.client.common.channel.handler;

import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonValue;

public class JsonRpcResponseHandler extends SimpleChannelInboundHandler<JsonValue> {
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonRpcResponseHandler.class);
  private final ResponseListenerRegistry responseListenerRegistry;

  public JsonRpcResponseHandler(final ResponseListenerRegistry responseListenerRegistry) {
    this.responseListenerRegistry = responseListenerRegistry;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void channelReadComplete(final ChannelHandlerContext ctx) {
    ctx.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void exceptionCaught(final ChannelHandlerContext ctx, final Throwable cause) {
    LOGGER.error("Exception occurred while handling result.", cause);
    ctx.close();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void channelRead0(final ChannelHandlerContext ctx, final JsonValue msg) {
    responseListenerRegistry.notifyListenerWith(msg);
  }
}
