package com.github.gibmir.ion.lib.netty.server.factory;

import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.factory.JsonRpcServerFactory;
import com.github.gibmir.ion.lib.netty.server.NettyJsonRpcServer;
import io.netty.channel.EventLoopGroup;

public class NettyJsonRpcServerFactory implements JsonRpcServerFactory {
  private final EventLoopGroup bossGroup;
  private final EventLoopGroup workerGroup;
  private final ProcedureProcessorRegistry procedureProcessorRegistry;

  public NettyJsonRpcServerFactory(EventLoopGroup bossGroup, EventLoopGroup workerGroup,
                                   ProcedureProcessorRegistry procedureProcessorRegistry) {
    this.bossGroup = bossGroup;
    this.workerGroup = workerGroup;
    this.procedureProcessorRegistry = procedureProcessorRegistry;
  }

  @Override
  public NettyJsonRpcServer create() {
    return new NettyJsonRpcServer(procedureProcessorRegistry);
  }

  @Override
  public void close() {
    workerGroup.shutdownGracefully();
    bossGroup.shutdownGracefully();
  }
}
