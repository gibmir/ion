package com.github.gibmir.ion.lib.netty.server.common.factory;

import com.github.gibmir.ion.api.server.factory.JsonRpcServerFactory;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessorFactory;
import com.github.gibmir.ion.api.server.processor.request.registry.ProcedureProcessorRegistry;
import com.github.gibmir.ion.lib.netty.server.common.NettyJsonRpcServer;
import io.netty.channel.EventLoopGroup;
import org.slf4j.Logger;

public final class NettyJsonRpcServerFactory implements JsonRpcServerFactory {
  private final Logger logger;
  private final EventLoopGroup bossGroup;
  private final EventLoopGroup workerGroup;
  private final ProcedureProcessorRegistry procedureProcessorRegistry;
  private final ProcedureProcessorFactory procedureProcessorFactory;

  public NettyJsonRpcServerFactory(final Logger logger, final EventLoopGroup bossGroup, final EventLoopGroup workerGroup,
                                   final ProcedureProcessorRegistry procedureProcessorRegistry,
                                   final ProcedureProcessorFactory procedureProcessorFactory) {
    this.logger = logger;
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
    logger.info("json-rpc server factory is closing. worker [{}], boss [{}]", workerGroup, bossGroup);
    if (workerGroup != null && !workerGroup.isShuttingDown() && !workerGroup.isShutdown()) {
      workerGroup.shutdownGracefully();
    }
    if (bossGroup != null && !bossGroup.isShuttingDown() && !bossGroup.isShutdown()) {
      bossGroup.shutdownGracefully();
    }
  }
}
