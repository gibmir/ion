package com.github.gibmir.ion.lib.netty.server.environment.mock;

import com.github.gibmir.ion.api.server.processor.request.registry.ProcedureProcessorRegistry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

public class ProcedureProcessorRegistryMock {
  public static ProcedureProcessorRegistry emptyMock() {
    return mock(ProcedureProcessorRegistry.class);
  }

  public static ProcedureProcessorRegistry withException(Class<? extends Throwable> exception) {
    ProcedureProcessorRegistry mock = mock(ProcedureProcessorRegistry.class);
    doThrow(exception).when(mock).register(any(), any());
    return mock;
  }
}
