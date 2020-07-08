package com.github.gibmir.ion.lib.netty.server.handler;

import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class JsonRpcRequestHandler extends ChannelInboundHandlerAdapter {
  private final ProcedureProcessorRegistry procedureProcessorRegistry;

  public JsonRpcRequestHandler(ProcedureProcessorRegistry procedureProcessorRegistry) {
    this.procedureProcessorRegistry = procedureProcessorRegistry;
  }


  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    super.channelRead(ctx, msg);
    JsonRpcRequest jsonRpcRequest = (JsonRpcRequest) msg;
    JsonRpcResponse jsonRpcResponse = procedureProcessorRegistry.getProcedureProcessorFor(jsonRpcRequest.getProcedureName())
      .process(jsonRpcRequest);
    ctx.write(jsonRpcResponse);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    super.channelReadComplete(ctx);
    ctx.flush();
  }
}
