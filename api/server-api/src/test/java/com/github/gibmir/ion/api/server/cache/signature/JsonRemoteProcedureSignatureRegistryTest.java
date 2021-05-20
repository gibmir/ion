package com.github.gibmir.ion.api.server.cache.signature;

import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodType;
import java.lang.reflect.Type;
import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

class JsonRemoteProcedureSignatureRegistryTest {

  public static final String TEST_PROCEDURE_NAME = "testProcedureName";
  public static JsonRemoteProcedureSignature PROCEDURE_SIGNATURE;

  @BeforeAll
  static void beforeAll() {
    PROCEDURE_SIGNATURE = mock(JsonRemoteProcedureSignature.class);
    doAnswer(__ -> "").when(PROCEDURE_SIGNATURE).getProcedureName();
    String[] parameterNames = new String[0];
    doAnswer(__ -> parameterNames).when(PROCEDURE_SIGNATURE).getParameterNames();
    Type[] parameterTypes = new Type[0];
    doAnswer(__ -> parameterTypes).when(PROCEDURE_SIGNATURE).getParameterNames();
    doAnswer(__ -> String.class).when(PROCEDURE_SIGNATURE).getReturnType();
    doAnswer(__ -> MethodType.methodType(String.class)).when(PROCEDURE_SIGNATURE).getMethodType();
    doAnswer(__ -> 1).when(PROCEDURE_SIGNATURE).getParametersCount();
  }

  @Test
  void testGetProcedureSignature() {
    HashMap<String, JsonRemoteProcedureSignature> signatureMap = new HashMap<>();
    SignatureRegistry signatureRegistry = new SimpleSignatureRegistry(signatureMap);

    assertThat(signatureRegistry.getProcedureSignatureFor(TEST_PROCEDURE_NAME), is(nullValue()));

    signatureMap.put(TEST_PROCEDURE_NAME, PROCEDURE_SIGNATURE);
    assertThat(signatureRegistry.getProcedureSignatureFor(TEST_PROCEDURE_NAME), equalTo(PROCEDURE_SIGNATURE));
  }

  @Test
  void testRegister() {
    HashMap<String, JsonRemoteProcedureSignature> signatureMap = new HashMap<>();
    SignatureRegistry signatureRegistry = new SimpleSignatureRegistry(signatureMap);
    signatureRegistry.register(TEST_PROCEDURE_NAME, PROCEDURE_SIGNATURE);

    assertThat(signatureMap.isEmpty(), is(false));
    assertThat(signatureMap.get(TEST_PROCEDURE_NAME), equalTo(PROCEDURE_SIGNATURE));
    assertThat(signatureRegistry.getProcedureSignatureFor(TEST_PROCEDURE_NAME), equalTo(PROCEDURE_SIGNATURE));

  }

  @Test
  void testUnregister() {
    HashMap<String, JsonRemoteProcedureSignature> signatureMap = new HashMap<>();
    SignatureRegistry signatureRegistry = new SimpleSignatureRegistry(signatureMap);
    assertDoesNotThrow(() -> signatureRegistry.unregister(TEST_PROCEDURE_NAME));
    signatureRegistry.register(TEST_PROCEDURE_NAME, PROCEDURE_SIGNATURE);

    assertThat(signatureMap.isEmpty(), is(false));
    assertThat(signatureMap.get(TEST_PROCEDURE_NAME), equalTo(PROCEDURE_SIGNATURE));
    assertThat(signatureRegistry.getProcedureSignatureFor(TEST_PROCEDURE_NAME), equalTo(PROCEDURE_SIGNATURE));

    assertDoesNotThrow(() -> signatureRegistry.unregister(TEST_PROCEDURE_NAME));

    assertThat(signatureMap.isEmpty(), is(true));
    assertThat(signatureMap.get(TEST_PROCEDURE_NAME), is(nullValue()));
    assertThat(signatureRegistry.getProcedureSignatureFor(TEST_PROCEDURE_NAME), is(nullValue()));
  }
}
