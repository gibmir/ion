package com.github.gibmir.ion.lib.netty.server.common.factory;

import com.github.gibmir.ion.api.server.processor.ProcedureProcessorFactory;
import com.github.gibmir.ion.api.server.processor.request.registry.ProcedureProcessorRegistry;
import com.github.gibmir.ion.lib.netty.server.common.NettyJsonRpcServer;
import io.netty.channel.EventLoopGroup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class NettyJsonRpcServerFactoryTest {

  @Test
  void testCreate() {
    ProcedureProcessorFactory testProcessorFactory = mock(ProcedureProcessorFactory.class);
    NettyJsonRpcServerFactory serverFactory = new NettyJsonRpcServerFactory(mock(Logger.class), mock(EventLoopGroup.class),
      mock(EventLoopGroup.class), mock(ProcedureProcessorRegistry.class), testProcessorFactory);
    NettyJsonRpcServer nettyJsonRpcServer = serverFactory.create();

    assertThat(nettyJsonRpcServer.getProcedureProcessorFactory(), equalTo(testProcessorFactory));
  }

  @Test
  void testClose() {
    Logger logger = mock(Logger.class);
    EventLoopGroup bossGroup = mock(EventLoopGroup.class);
    EventLoopGroup workerGroup = mock(EventLoopGroup.class);
    NettyJsonRpcServerFactory serverFactory = new NettyJsonRpcServerFactory(logger,
      bossGroup, workerGroup, mock(ProcedureProcessorRegistry.class),
      mock(ProcedureProcessorFactory.class));

    serverFactory.close();

    verify(logger).info(anyString(), eq(workerGroup), eq(bossGroup));
    verify(bossGroup).shutdownGracefully();
    verify(workerGroup).shutdownGracefully();
  }

  @Test
  void testCloseOnlyBossGroup() {
    Logger logger = mock(Logger.class);
    EventLoopGroup bossGroup = mock(EventLoopGroup.class);
    EventLoopGroup workerGroup = mock(EventLoopGroup.class);
    NettyJsonRpcServerFactory serverFactory = new NettyJsonRpcServerFactory(logger,
      bossGroup, workerGroup, mock(ProcedureProcessorRegistry.class),
      mock(ProcedureProcessorFactory.class));

    doAnswer(__ -> true).when(workerGroup).isShutdown();
    serverFactory.close();

    verify(logger).info(anyString(), eq(workerGroup), eq(bossGroup));
    verify(workerGroup, never()).shutdownGracefully();
    verify(bossGroup).shutdownGracefully();
  }

  @Test
  void testCloseOnlyWorkerGroup() {
    Logger logger = mock(Logger.class);
    EventLoopGroup bossGroup = mock(EventLoopGroup.class);
    EventLoopGroup workerGroup = mock(EventLoopGroup.class);
    NettyJsonRpcServerFactory serverFactory = new NettyJsonRpcServerFactory(logger,
      bossGroup, workerGroup, mock(ProcedureProcessorRegistry.class),
      mock(ProcedureProcessorFactory.class));

    doAnswer(__ -> true).when(bossGroup).isShutdown();
    serverFactory.close();

    verify(logger).info(anyString(), eq(workerGroup), eq(bossGroup));
    verify(bossGroup, never()).shutdownGracefully();
    verify(workerGroup).shutdownGracefully();
  }
}
