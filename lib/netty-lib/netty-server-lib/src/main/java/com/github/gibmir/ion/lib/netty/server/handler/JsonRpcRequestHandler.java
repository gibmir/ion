package com.github.gibmir.ion.lib.netty.server.handler;

import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.server.cache.processor.ServerProcessor;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import javax.json.JsonStructure;
import javax.json.bind.Jsonb;

public class JsonRpcRequestHandler extends ChannelInboundHandlerAdapter {
  private final ServerProcessor serverProcessor;
  private final Jsonb jsonb;

  public JsonRpcRequestHandler(ServerProcessor serverProcessor,
                               Jsonb jsonb) {
    this.serverProcessor = serverProcessor;
    this.jsonb = jsonb;
  }


  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    JsonStructure jsonStructure = (JsonStructure) msg;
    serverProcessor.process(jsonStructure, jsonb, ctx::write);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    ErrorResponse errorResponse = ErrorResponse.withNullId(cause);
    ctx.write(errorResponse).addListener(ChannelFutureListener.CLOSE);
  }
}
