package com.github.gibmir.ion.lib.netty.server.common;

import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessorFactory;
import com.github.gibmir.ion.api.server.processor.request.registry.ProcedureProcessorRegistry;
import com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NettyJsonRpcServerTest {

  private NettyJsonRpcServer nettyJsonRpcServer;
  private ProcedureProcessorFactory factory;
  private ProcedureProcessorRegistry registry;

  @BeforeEach
  void beforeEach() {
    registry = mock(ProcedureProcessorRegistry.class);
    factory = mock(ProcedureProcessorFactory.class);
    nettyJsonRpcServer = new NettyJsonRpcServer(registry, factory);
  }

  @Test
  void testGetProcedureProcessorFactory() {
    assertThat(nettyJsonRpcServer.getProcedureProcessorFactory(), equalTo(factory));
  }

  @Test
  void testRegisterWithVarargIncorrectProcessor() {
    ProcedureProcessor<?> processor = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.IncorrectProcedure.class).when(processor).getProcedure();

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
      () -> nettyJsonRpcServer.register(processor));
    assertThat(exception.getMessage(), containsString(TestEnvironment.IncorrectProcedure.class.getName()));
  }

  @Test
  void testRegisterWithVarargOneJsonRemoteProcedure0Processor() {
    ProcedureProcessor<?> processor = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.TestProcedure0.class).when(processor).getProcedure();
    ProcedureManager manager = nettyJsonRpcServer.register(processor);

    assertThat(manager, not(nullValue()));
    verify(registry).register(eq(TestEnvironment.TestProcedure0.class.getName()), any());
  }

  @Test
  void testRegisterWithVarargOneJsonRemoteProcedure1Processor() {
    ProcedureProcessor<?> processor = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.TestProcedure1.class).when(processor).getProcedure();
    ProcedureManager manager = nettyJsonRpcServer.register(processor);

    assertThat(manager, not(nullValue()));
    verify(registry).register(eq(TestEnvironment.TestProcedure1.class.getName()), any());

  }

  @Test
  void testRegisterWithVarargOneJsonRemoteProcedure2Processor() {
    ProcedureProcessor<?> processor = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.TestProcedure2.class).when(processor).getProcedure();
    ProcedureManager manager = nettyJsonRpcServer.register(processor);

    assertThat(manager, not(nullValue()));
    verify(registry).register(eq(TestEnvironment.TestProcedure2.class.getName()), any());

  }

  @Test
  void testRegisterWithVarargOneJsonRemoteProcedure3Processor() {
    ProcedureProcessor<?> processor = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.TestProcedure3.class).when(processor).getProcedure();
    ProcedureManager manager = nettyJsonRpcServer.register(processor);

    assertThat(manager, not(nullValue()));
    verify(registry).register(eq(TestEnvironment.TestProcedure3.class.getName()), any());
  }

  @Test
  void testRegisterWithVarargWithoutProcessor() {
    assertThrows(IllegalArgumentException.class, () -> nettyJsonRpcServer.register());
  }

  @Test
  void testRegisterWithVarargTwoProcessor() {
    ProcedureProcessor<?> first = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.TestProcedure0.class).when(first).getProcedure();
    ProcedureProcessor<?> second = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.TestProcedure1.class).when(second).getProcedure();
    ProcedureManager manager = nettyJsonRpcServer.register(first, second);

    assertThat(manager, not(nullValue()));
    verify(registry).register(eq(TestEnvironment.TestProcedure0.class.getName()), any());
    verify(registry).register(eq(TestEnvironment.TestProcedure1.class.getName()), any());

  }

  //Collection
  @Test
  void testRegisterWithCollectionOneJsonRemoteProcedure0Processor() {
    ProcedureProcessor<?> processor = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.TestProcedure0.class).when(processor).getProcedure();
    ProcedureManager manager = nettyJsonRpcServer.register(List.of(processor));

    assertThat(manager, not(nullValue()));
    verify(registry).register(eq(TestEnvironment.TestProcedure0.class.getName()), any());
  }

  @Test
  void testRegisterWithCollectionOneJsonRemoteProcedure1Processor() {
    ProcedureProcessor<?> processor = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.TestProcedure1.class).when(processor).getProcedure();
    ProcedureManager manager = nettyJsonRpcServer.register(List.of(processor));

    assertThat(manager, not(nullValue()));
    verify(registry).register(eq(TestEnvironment.TestProcedure1.class.getName()), any());

  }

  @Test
  void testRegisterWithCollectionOneJsonRemoteProcedure2Processor() {
    ProcedureProcessor<?> processor = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.TestProcedure2.class).when(processor).getProcedure();
    ProcedureManager manager = nettyJsonRpcServer.register(List.of(processor));

    assertThat(manager, not(nullValue()));
    verify(registry).register(eq(TestEnvironment.TestProcedure2.class.getName()), any());

  }

  @Test
  void testRegisterWithCollectionOneJsonRemoteProcedure3Processor() {
    ProcedureProcessor<?> processor = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.TestProcedure3.class).when(processor).getProcedure();
    ProcedureManager manager = nettyJsonRpcServer.register(List.of(processor));

    assertThat(manager, not(nullValue()));
    verify(registry).register(eq(TestEnvironment.TestProcedure3.class.getName()), any());
  }

  @Test
  void testRegisterWithCollectionTwoProcessor() {
    ProcedureProcessor<?> first = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.TestProcedure0.class).when(first).getProcedure();
    ProcedureProcessor<?> second = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.TestProcedure1.class).when(second).getProcedure();
    ProcedureManager manager = nettyJsonRpcServer.register(List.of(first, second));

    assertThat(manager, not(nullValue()));
    verify(registry).register(eq(TestEnvironment.TestProcedure0.class.getName()), any());
    verify(registry).register(eq(TestEnvironment.TestProcedure1.class.getName()), any());
  }

  @Test
  void testRegisterWithCollectionWithoutProcessor() {
    assertThrows(IllegalArgumentException.class, () -> nettyJsonRpcServer.register(Collections.emptyList()));
  }

  @Test
  void testRegisterWithCollectionIncorrectProcessor() {
    ProcedureProcessor<?> processor = mock(ProcedureProcessor.class);
    doAnswer(__ -> TestEnvironment.IncorrectProcedure.class).when(processor).getProcedure();

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
      () -> nettyJsonRpcServer.register(List.of(processor)));
    assertThat(exception.getMessage(), containsString(TestEnvironment.IncorrectProcedure.class.getName()));
  }
}
