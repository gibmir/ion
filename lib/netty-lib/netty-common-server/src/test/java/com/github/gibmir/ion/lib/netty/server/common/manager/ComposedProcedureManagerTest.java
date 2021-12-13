package com.github.gibmir.ion.lib.netty.server.common.manager;

import com.github.gibmir.ion.api.server.manager.ProcedureManager;
import com.github.gibmir.ion.api.server.processor.request.registry.ProcedureProcessorRegistry;
import org.hamcrest.CoreMatchers;
import org.hamcrest.collection.ArrayMatching;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.ArrayMatching.hasItemInArray;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ComposedProcedureManagerTest {

  @Test
  void testCloseWithOneManager() throws Exception {
    Logger logger = mock(Logger.class);
    ProcedureManager firstManager = mock(ProcedureManager.class);
    List<ProcedureManager> managers = List.of(firstManager);
    ComposedProcedureManager manager = new ComposedProcedureManager(logger, managers);
    manager.close();

    verify(logger).info(anyString(), eq(managers));
    verify(firstManager).close();
  }

  @Test
  void testCloseWithTwoManagers() throws Exception {
    Logger logger = mock(Logger.class);
    ProcedureManager firstManager = mock(ProcedureManager.class);
    ProcedureManager secondManager = mock(ProcedureManager.class);
    List<ProcedureManager> managers = List.of(firstManager, secondManager);
    ComposedProcedureManager manager = new ComposedProcedureManager(logger, managers);
    manager.close();

    verify(logger).info(anyString(), eq(managers));
    verify(firstManager).close();
    verify(secondManager).close();
  }

  @Test
  void testCloseWithManagerClosingException() throws Exception {
    Logger logger = mock(Logger.class);
    ProcedureManager firstManager = mock(ProcedureManager.class);
    TestException testException = new TestException();
    doThrow(testException).when(firstManager).close();
    List<ProcedureManager> managers = List.of(firstManager);
    ComposedProcedureManager manager = new ComposedProcedureManager(logger, managers);

    IllegalStateException exception = assertThrows(IllegalStateException.class, manager::close);

    assertThat(exception.getSuppressed().length, equalTo(1));
    assertThat(exception.getSuppressed(), hasItemInArray(testException));
    verify(logger).info(anyString(), eq(managers));
    verify(firstManager).close();
  }

  @Test
  void testCloseWithTwoManagerClosingException() throws Exception {
    Logger logger = mock(Logger.class);
    ProcedureManager firstManager = mock(ProcedureManager.class);
    ProcedureManager secondManager = mock(ProcedureManager.class);
    TestException firstTestException = new TestException();
    doThrow(firstTestException).when(firstManager).close();
    TestException secondTestException = new TestException();
    doThrow(secondTestException).when(secondManager).close();
    List<ProcedureManager> managers = List.of(firstManager, secondManager);
    ComposedProcedureManager manager = new ComposedProcedureManager(logger, managers);

    IllegalStateException exception = assertThrows(IllegalStateException.class, manager::close);

    assertThat(exception.getSuppressed().length, equalTo(2));
    assertThat(exception.getSuppressed(), hasItemInArray(firstTestException));
    assertThat(exception.getSuppressed(), hasItemInArray(secondTestException));
    verify(logger).info(anyString(), eq(managers));
    verify(firstManager).close();
  }

  public static class TestException extends RuntimeException {

  }
}
