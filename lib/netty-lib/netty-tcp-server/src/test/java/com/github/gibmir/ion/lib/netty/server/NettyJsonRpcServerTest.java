package com.github.gibmir.ion.lib.netty.server;

import com.github.gibmir.ion.api.server.cache.processor.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.server.cache.processor.ProcedureProcessorRegistry;
import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessorFactory;
import com.github.gibmir.ion.lib.netty.server.common.NettyJsonRpcServer;
import com.github.gibmir.ion.lib.netty.server.environment.NettyServerTestEnvironment;
import com.github.gibmir.ion.lib.netty.server.environment.NettyServerTestEnvironment.TestException;
import com.github.gibmir.ion.lib.netty.server.environment.mock.ProcedureProcessorRegistryMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NettyJsonRpcServerTest {

  private ProcedureProcessor<NettyServerTestEnvironment.TestApi0> procedureProcessor0;
  private ProcedureProcessor<NettyServerTestEnvironment.TestApi1> procedureProcessor1;
  private ProcedureProcessor<NettyServerTestEnvironment.TestApi2> procedureProcessor2;
  private ProcedureProcessor<NettyServerTestEnvironment.TestApi3> procedureProcessor3;

  @BeforeEach
  @SuppressWarnings("unchecked")
  void beforeEach() {
    procedureProcessor0 = mock(ProcedureProcessor.class);
    doAnswer(__ -> NettyServerTestEnvironment.TestApi0.class).when(procedureProcessor0).getProcedure();
    doAnswer(__ -> (NettyServerTestEnvironment.TestApi0) () -> "something").when(procedureProcessor0).getProcessor();
    procedureProcessor1 = mock(ProcedureProcessor.class);
    doAnswer(__ -> NettyServerTestEnvironment.TestApi1.class).when(procedureProcessor1).getProcedure();
    doAnswer(__ -> (NettyServerTestEnvironment.TestApi1) (str) -> str).when(procedureProcessor1).getProcessor();
    procedureProcessor2 = mock(ProcedureProcessor.class);
    doAnswer(__ -> NettyServerTestEnvironment.TestApi2.class).when(procedureProcessor2).getProcedure();
    doAnswer(__ -> (NettyServerTestEnvironment.TestApi2) (str1, str2) -> str1 + str2).when(procedureProcessor2).getProcessor();
    procedureProcessor3 = mock(ProcedureProcessor.class);
    doAnswer(__ -> NettyServerTestEnvironment.TestApi3.class).when(procedureProcessor3).getProcedure();
    doAnswer(__ -> (NettyServerTestEnvironment.TestApi3) (str1, str2, str3) -> str1 + str2 + str3)
      .when(procedureProcessor3).getProcessor();
  }

  @Test
  void testRegisterProcedureProcessor0() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));
    ProcedureManager procedureManager = assertDoesNotThrow(() -> nettyJsonRpcServer.register(procedureProcessor0));
    assertNotNull(procedureManager);
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi0.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testRegisterProcedureProcessor0WithCollection() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));
    ProcedureManager procedureManager = assertDoesNotThrow(() -> nettyJsonRpcServer.register(List.of(procedureProcessor0)));
    assertNotNull(procedureManager);
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi0.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testRegisterProcedureProcessor1() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));

    ProcedureManager procedureManager = assertDoesNotThrow(() -> nettyJsonRpcServer.register(procedureProcessor1));
    assertNotNull(procedureManager);
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi1.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testRegisterProcedureProcessor1WithCollection() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));

    ProcedureManager procedureManager = assertDoesNotThrow(
      () -> nettyJsonRpcServer.register(List.of(procedureProcessor1)));
    assertNotNull(procedureManager);
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi1.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testRegisterProcedureProcessor2() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));

    ProcedureManager procedureManager = assertDoesNotThrow(() -> nettyJsonRpcServer.register(procedureProcessor2));
    assertNotNull(procedureManager);
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi2.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testRegisterProcedureProcessor2WithCollection() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));
    ProcedureManager procedureManager = assertDoesNotThrow(
      () -> nettyJsonRpcServer.register(List.of(procedureProcessor2)));
    assertNotNull(procedureManager);
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi2.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testRegisterProcedureProcessor3() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));

    ProcedureManager procedureManager = assertDoesNotThrow(() -> nettyJsonRpcServer.register(procedureProcessor3));
    assertNotNull(procedureManager);
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi3.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testRegisterProcedureProcessor3WithCollection() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));

    ProcedureProcessor<NettyServerTestEnvironment.TestApi3> procedureProcessor = mock(ProcedureProcessor.class);
    doAnswer(__ -> NettyServerTestEnvironment.TestApi3.class).when(procedureProcessor).getProcedure();
    doAnswer(__ -> (NettyServerTestEnvironment.TestApi3) (str1, str2, str3) -> str1 + str2 + str3)
      .when(procedureProcessor).getProcessor();

    ProcedureManager procedureManager = assertDoesNotThrow(() -> nettyJsonRpcServer.register(List.of(procedureProcessor)));
    assertNotNull(procedureManager);
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi3.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testRegisterProcedureProcessorCollection() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));

    ProcedureManager procedureManager = assertDoesNotThrow(
      () -> nettyJsonRpcServer.register(List.of(procedureProcessor0, procedureProcessor1, procedureProcessor2,
        procedureProcessor3)));
    assertNotNull(procedureManager);
    assertThat(procedureManager, instanceOf(ProcedureManager.class));
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi0.class.getName()), any(JsonRpcRequestProcessor.class));
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi1.class.getName()), any(JsonRpcRequestProcessor.class));
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi2.class.getName()), any(JsonRpcRequestProcessor.class));
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi3.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testRegisterProcedureProcessorVararg() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.emptyMock();
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));
    ProcedureManager procedureManager = assertDoesNotThrow(() -> nettyJsonRpcServer.register(procedureProcessor0,
      procedureProcessor1, procedureProcessor2, procedureProcessor3));
    assertNotNull(procedureManager);
    assertThat(procedureManager, instanceOf(ProcedureManager.class));
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi0.class.getName()), any(JsonRpcRequestProcessor.class));
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi1.class.getName()), any(JsonRpcRequestProcessor.class));
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi2.class.getName()), any(JsonRpcRequestProcessor.class));
    verify(processorRegistry, times(1))
      .register(eq(NettyServerTestEnvironment.TestApi3.class.getName()), any(JsonRpcRequestProcessor.class));
  }

  @Test
  void testWithBrokenRegistryRegisterProcedureProcessor0() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.withException(TestException.class);
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));

    assertThrows(TestException.class, () -> nettyJsonRpcServer.register(procedureProcessor0));
  }

  @Test
  void testWithBrokenRegistryRegisterProcedureProcessor1() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.withException(TestException.class);
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));

    assertThrows(TestException.class, () -> nettyJsonRpcServer.register(procedureProcessor1));
  }

  @Test
  void testWithBrokenRegistryRegisterProcedureProcessor2() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.withException(TestException.class);
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));

    assertThrows(TestException.class, () -> nettyJsonRpcServer.register(procedureProcessor2));
  }

  @Test
  void testWithBrokenRegistryRegisterProcedureProcessor3() {
    ProcedureProcessorRegistry processorRegistry = ProcedureProcessorRegistryMock.withException(TestException.class);
    NettyJsonRpcServer nettyJsonRpcServer = new NettyJsonRpcServer(processorRegistry, mock(ProcedureProcessorFactory.class));

    assertThrows(TestException.class, () -> nettyJsonRpcServer.register(procedureProcessor3));
  }
}
