package com.github.gibmir.ion.lib.netty.client.sender.handler;

import com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry.ResponseListenerRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonStructure;
import javax.json.bind.Jsonb;

public class JsonRpcResponseHandler extends ChannelInboundHandlerAdapter {
  private static final Logger LOGGER = LoggerFactory.getLogger(JsonRpcResponseHandler.class);
  private final Jsonb jsonb;
  private final ResponseListenerRegistry responseListenerRegistry;

  public JsonRpcResponseHandler(Jsonb jsonb, ResponseListenerRegistry responseListenerRegistry) {
    this.jsonb = jsonb;
    this.responseListenerRegistry = responseListenerRegistry;
  }


  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    responseListenerRegistry.notifyListenerWith((JsonStructure) msg, jsonb);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    LOGGER.error("Exception occurred while handling result.", cause);
    ctx.close();
  }
}
