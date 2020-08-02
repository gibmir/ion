package com.github.gibmir.ion.lib.netty.server.handler;

import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.server.cache.processor.ServerProcessor;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class JsonRpcRequestHandler extends ChannelInboundHandlerAdapter {
  private final ServerProcessor serverProcessor;
  private final Jsonb jsonb;
  private final Charset charset;

  public JsonRpcRequestHandler(ServerProcessor serverProcessor,
                               Jsonb jsonb, Charset charset) {
    this.serverProcessor = serverProcessor;
    this.jsonb = jsonb;
    this.charset = charset;
  }


  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    serverProcessor.process((JsonValue) msg, jsonb, charset, ctx::write);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    ctx.write(ErrorResponse.withNullId(cause)).addListener(ChannelFutureListener.CLOSE);
  }
}
