package com.github.gibmir.ion.lib.netty.server.common.processor;

import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

import static com.github.gibmir.ion.lib.netty.server.common.environment.TestEnvironment.TestProcedure0;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

class ProcedureProcessor0Test {

  @Test
  void testCharset() {
    Class<TestProcedure0> serviceClass = TestProcedure0.class;
    TestProcedure0 service = mock(serviceClass);
    Jsonb jsonb = mock(Jsonb.class);
    Charset charset = mock(Charset.class);
    var procedure = new ProcedureProcessor0<>(serviceClass, service, jsonb, charset);
    assertThat(procedure.charset(), equalTo(charset));
    Charset newCharset = mock(Charset.class);
    procedure.charset(newCharset);
    assertThat(procedure.charset(), equalTo(newCharset));
  }

  @Test
  void testJsonb() {
    Class<TestProcedure0> serviceClass = TestProcedure0.class;
    TestProcedure0 service = mock(serviceClass);
    Jsonb jsonb = mock(Jsonb.class);
    Charset charset = mock(Charset.class);
    var procedure = new ProcedureProcessor0<>(serviceClass, service, jsonb, charset);
    assertThat(procedure.jsonb(), equalTo(jsonb));
    Jsonb newJson = mock(Jsonb.class);
    procedure.jsonb(newJson);
    assertThat(procedure.jsonb(), equalTo(newJson));
  }

  @Test
  void testGetProcedure() {
    Class<TestProcedure0> serviceClass = TestProcedure0.class;
    TestProcedure0 service = mock(serviceClass);
    Jsonb jsonb = mock(Jsonb.class);
    Charset charset = mock(Charset.class);
    var procedure = new ProcedureProcessor0<>(serviceClass, service, jsonb, charset);
    assertThat(procedure.getProcedure(), equalTo(serviceClass));
  }

  @Test
  void testGetProcessor() {
    Class<TestProcedure0> serviceClass = TestProcedure0.class;
    TestProcedure0 service = mock(serviceClass);
    Jsonb jsonb = mock(Jsonb.class);
    Charset charset = mock(Charset.class);
    var procedure = new ProcedureProcessor0<>(serviceClass, service, jsonb, charset);
    assertThat(procedure.getProcessor(), equalTo(service));
  }
}
