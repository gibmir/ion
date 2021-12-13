package com.github.gibmir.ion.lib.netty.server.common.manager;

import com.github.gibmir.ion.api.server.processor.request.registry.ProcedureProcessorRegistry;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NettyProcedureManagerTest {

  @Test
  void testClose() {
    ProcedureProcessorRegistry registry = mock(ProcedureProcessorRegistry.class);
    Logger logger = mock(Logger.class);
    String testProcedureName = "testProcedureName";
    NettyProcedureManager manager = new NettyProcedureManager(logger, registry, testProcedureName);
    manager.close();

    verify(logger).info(anyString(), eq(testProcedureName));
    verify(registry).unregister(eq(testProcedureName));
  }
}
