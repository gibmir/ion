package com.github.gibmir.ion.lib.netty.server.common.factory;

import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.factory.JsonRpcServerFactory;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessorFactory;
import com.github.gibmir.ion.lib.netty.server.common.NettyJsonRpcServer;
import io.netty.channel.EventLoopGroup;

public class NettyJsonRpcServerFactory implements JsonRpcServerFactory {
  private final EventLoopGroup bossGroup;
  private final EventLoopGroup workerGroup;
  private final ProcedureProcessorRegistry procedureProcessorRegistry;
  private final ProcedureProcessorFactory procedureProcessorFactory;

  public NettyJsonRpcServerFactory(EventLoopGroup bossGroup, EventLoopGroup workerGroup,
                                   ProcedureProcessorRegistry procedureProcessorRegistry,
                                   ProcedureProcessorFactory procedureProcessorFactory) {
    this.bossGroup = bossGroup;
    this.workerGroup = workerGroup;
    this.procedureProcessorRegistry = procedureProcessorRegistry;
    this.procedureProcessorFactory = procedureProcessorFactory;
  }

  @Override
  public NettyJsonRpcServer create() {
    return new NettyJsonRpcServer(procedureProcessorRegistry, procedureProcessorFactory);
  }

  @Override
  public void close() {
    workerGroup.shutdownGracefully();
    bossGroup.shutdownGracefully();
  }
}
