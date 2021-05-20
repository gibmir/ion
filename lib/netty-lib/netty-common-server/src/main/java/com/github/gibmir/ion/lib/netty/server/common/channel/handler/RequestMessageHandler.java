package com.github.gibmir.ion.lib.netty.server.common.channel.handler;

import com.github.gibmir.ion.api.message.Message;
import com.github.gibmir.ion.lib.netty.server.common.processor.ServerProcessor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @implNote Handler {@link ChannelHandlerContext#write(Object) starts outbound handlers processing}
 */
public class RequestMessageHandler extends SimpleChannelInboundHandler<Message> {
  private final ServerProcessor serverProcessor;

  public RequestMessageHandler(ServerProcessor serverProcessor) {
    this.serverProcessor = serverProcessor;
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
    ctx.write(serverProcessor.process(msg));
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }
}
