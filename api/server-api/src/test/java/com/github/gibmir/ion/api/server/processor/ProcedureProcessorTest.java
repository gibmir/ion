package com.github.gibmir.ion.api.server.processor;

import org.junit.jupiter.api.Test;

import static com.github.gibmir.ion.api.server.environment.ServerTestEnvironment.TEST_PROCEDURE_IMPL_0;
import static com.github.gibmir.ion.api.server.environment.ServerTestEnvironment.TEST_PROCEDURE_IMPL_1;
import static com.github.gibmir.ion.api.server.environment.ServerTestEnvironment.TEST_PROCEDURE_IMPL_2;
import static com.github.gibmir.ion.api.server.environment.ServerTestEnvironment.TEST_PROCEDURE_IMPL_3;
import static com.github.gibmir.ion.api.server.environment.ServerTestEnvironment.TestProcedure0;
import static com.github.gibmir.ion.api.server.environment.ServerTestEnvironment.TestProcedure1;
import static com.github.gibmir.ion.api.server.environment.ServerTestEnvironment.TestProcedure2;
import static com.github.gibmir.ion.api.server.environment.ServerTestEnvironment.TestProcedure3;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;

class ProcedureProcessorTest {


  @Test
  void testProcedureProcessorFrom0() {
    ProcedureProcessor procedureProcessor = ProcedureProcessor.from(TestProcedure0.class, TEST_PROCEDURE_IMPL_0);
    assertThat(procedureProcessor, instanceOf(ProcedureProcessor0.class));
  }

  @Test
  void testProcedureProcessorFrom1() {
    ProcedureProcessor procedureProcessor = ProcedureProcessor.from(TestProcedure1.class, TEST_PROCEDURE_IMPL_1);
    assertThat(procedureProcessor, instanceOf(ProcedureProcessor1.class));
  }


  @Test
  void testProcedureProcessorFrom2() {
    ProcedureProcessor procedureProcessor = ProcedureProcessor.from(TestProcedure2.class, TEST_PROCEDURE_IMPL_2);
    assertThat(procedureProcessor, instanceOf(ProcedureProcessor2.class));
  }


  @Test
  void testProcedureProcessorFrom3() {
    ProcedureProcessor procedureProcessor = ProcedureProcessor.from(TestProcedure3.class, TEST_PROCEDURE_IMPL_3);
    assertThat(procedureProcessor, instanceOf(ProcedureProcessor3.class));
  }
}
