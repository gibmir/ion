package com.github.gibmir.ion.lib.netty.server.handler;

import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class JsonRpcRequestHandler extends ChannelInboundHandlerAdapter {
  private final ProcedureProcessorRegistry procedureProcessorRegistry;

  public JsonRpcRequestHandler(ProcedureProcessorRegistry procedureProcessorRegistry) {
    this.procedureProcessorRegistry = procedureProcessorRegistry;
  }


  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    JsonRpcRequest jsonRpcRequest = (JsonRpcRequest) msg;
    JsonRpcResponse jsonRpcResponse = jsonRpcRequest.processWith(procedureProcessorRegistry.getProcedureProcessorFor(jsonRpcRequest.getProcedureName()));
    ctx.write(jsonRpcResponse).addListener(ChannelFutureListener.CLOSE);
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
