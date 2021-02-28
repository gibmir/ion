package com.github.gibmir.ion.lib.netty.server.common.processor;

import com.github.gibmir.ion.api.server.processor.ProcedureProcessor;
import com.github.gibmir.ion.api.server.processor.ProcedureProcessorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;

import java.nio.charset.Charset;

import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TEST_PROCEDURE_IMPL_0;
import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TEST_PROCEDURE_IMPL_1;
import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TEST_PROCEDURE_IMPL_2;
import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TEST_PROCEDURE_IMPL_3;
import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TestProcedure0;
import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TestProcedure1;
import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TestProcedure2;
import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TestProcedure3;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

class NettyProcedureProcessorFactoryTest {

  private ProcedureProcessorFactory procedureProcessorFactory;

  @BeforeEach
  void beforeEach() {
    procedureProcessorFactory = new NettyProcedureProcessorFactory(mock(Jsonb.class), mock(Charset.class));
  }

  @Test
  void testProcedureProcessorFrom0() {
    ProcedureProcessor<TestProcedure0> procedureProcessor = procedureProcessorFactory.create(TestProcedure0.class,
      TEST_PROCEDURE_IMPL_0);
    assertThat(procedureProcessor, instanceOf(ProcedureProcessor0.class));
  }

  @Test
  void testProcedureProcessorFrom1() {
    ProcedureProcessor<TestProcedure1> procedureProcessor = procedureProcessorFactory.create(TestProcedure1.class,
      TEST_PROCEDURE_IMPL_1);
    assertThat(procedureProcessor, instanceOf(ProcedureProcessor1.class));
  }


  @Test
  void testProcedureProcessorFrom2() {
    ProcedureProcessor<TestProcedure2> procedureProcessor = procedureProcessorFactory.create(TestProcedure2.class,
      TEST_PROCEDURE_IMPL_2);
    assertThat(procedureProcessor, instanceOf(ProcedureProcessor2.class));
  }


  @Test
  void testProcedureProcessorFrom3() {
    ProcedureProcessor<TestProcedure3> procedureProcessor = procedureProcessorFactory.create(TestProcedure3.class, TEST_PROCEDURE_IMPL_3);
    assertThat(procedureProcessor, instanceOf(ProcedureProcessor3.class));
  }
}
