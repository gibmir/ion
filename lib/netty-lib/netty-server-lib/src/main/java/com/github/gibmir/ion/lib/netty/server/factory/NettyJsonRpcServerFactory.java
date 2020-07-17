package com.github.gibmir.ion.lib.netty.server.factory;

import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.factory.JsonRpcServerFactory;
import com.github.gibmir.ion.lib.netty.server.NettyJsonRpcServer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyJsonRpcServerFactory implements JsonRpcServerFactory {
  private final ExecutorService serverExecutorService;
  private final ProcedureProcessorRegistry procedureProcessorRegistry;

  public NettyJsonRpcServerFactory(ExecutorService serverExecutorService,
                                   ProcedureProcessorRegistry procedureProcessorRegistry) {
    this.serverExecutorService = serverExecutorService;
    this.procedureProcessorRegistry = procedureProcessorRegistry;
  }

  @Override
  public NettyJsonRpcServer create() {
    return new NettyJsonRpcServer(procedureProcessorRegistry);
  }

  @Override
  public void close() {
    serverExecutorService.shutdown();
    try {
      // Wait a while for existing tasks to terminate
      //todo shutdown config
      if (!serverExecutorService.awaitTermination(60, TimeUnit.SECONDS)) {
        serverExecutorService.shutdownNow(); // Cancel currently executing tasks
        // Wait a while for tasks to respond to being cancelled
        serverExecutorService.awaitTermination(60, TimeUnit.SECONDS);
      }
    } catch (InterruptedException ie) {
      // (Re-)Cancel if current thread also interrupted
      serverExecutorService.shutdownNow();
      // Preserve interrupt status
      Thread.currentThread().interrupt();
    }
  }
}
