package com.github.gibmir.ion.lib.netty.server;

import com.github.gibmir.ion.api.server.cache.processor.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import com.github.gibmir.ion.lib.netty.server.environment.NettyServerTestEnvironment;
import com.github.gibmir.ion.lib.netty.server.environment.NettyServerTestEnvironment.TestException;
import com.github.gibmir.ion.lib.netty.server.environment.mock.ProcedureProcessorRegistryMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NettyJsonRpcServerTest {

  @Test
  void testRegisterProcedureProcessor0() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry);

    ProcedureManager procedureManager = assertDoesNotThrow(
      () -> nettyJsonRpcServer.registerProcedureProcessor(NettyServerTestEnvironment.TestApi0.class, () -> "pog"));
    assertNotNull(procedureManager);
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi0.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testRegisterProcedureProcessor1() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry);

    ProcedureManager procedureManager = assertDoesNotThrow(
      () -> nettyJsonRpcServer.registerProcedureProcessor(NettyServerTestEnvironment.TestApi1.class, str -> str));
    assertNotNull(procedureManager);
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi1.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testRegisterProcedureProcessor2() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry);

    ProcedureManager procedureManager = assertDoesNotThrow(
      () -> nettyJsonRpcServer.registerProcedureProcessor(NettyServerTestEnvironment.TestApi2.class,
        (str1, str2) -> str1 + str2));
    assertNotNull(procedureManager);
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi2.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testRegisterProcedureProcessor3() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry);

    ProcedureManager procedureManager = assertDoesNotThrow(
      () -> nettyJsonRpcServer.registerProcedureProcessor(NettyServerTestEnvironment.TestApi3.class,
        (str1, str2, str3) -> str1 + str2 + str3));
    assertNotNull(procedureManager);
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi3.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testWithBrokenRegistryRegisterProcedureProcessor0() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.withException(TestException.class);
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry);

    assertThrows(TestException.class,
      () -> nettyJsonRpcServer.registerProcedureProcessor(NettyServerTestEnvironment.TestApi0.class, () -> "pog"));
  }

  @Test
  void testWithBrokenRegistryRegisterProcedureProcessor1() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.withException(TestException.class);
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry);

    assertThrows(TestException.class,
      () -> nettyJsonRpcServer.registerProcedureProcessor(NettyServerTestEnvironment.TestApi1.class, str -> str));
  }

  @Test
  void testWithBrokenRegistryRegisterProcedureProcessor2() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.withException(TestException.class);
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry);

    assertThrows(TestException.class,
      () -> nettyJsonRpcServer.registerProcedureProcessor(NettyServerTestEnvironment.TestApi2.class,
        (str1, str2) -> str1 + str2));
  }

  @Test
  void testWithBrokenRegistryRegisterProcedureProcessor3() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.withException(TestException.class);
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry);

    assertThrows(TestException.class,
      () -> nettyJsonRpcServer.registerProcedureProcessor(NettyServerTestEnvironment.TestApi3.class,
        (str1, str2, str3) -> str1 + str2 + str3));
  }
}
