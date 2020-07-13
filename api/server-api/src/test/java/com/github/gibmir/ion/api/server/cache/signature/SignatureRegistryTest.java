package com.github.gibmir.ion.api.server.cache.signature;

import com.github.gibmir.ion.api.dto.method.signature.ParameterizedSignature;
import com.github.gibmir.ion.api.dto.method.signature.Signature;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class SignatureRegistryTest {

  public static final String TEST_PROCEDURE_NAME = "testProcedureName";
  public static final ParameterizedSignature PROCEDURE_SIGNATURE = new ParameterizedSignature();

  @Test
  void testGetProcedureSignature() {
    HashMap<String, Signature> signatureMap = new HashMap<>();
    SignatureRegistry signatureRegistry = new SimpleSignatureRegistry(signatureMap);

    assertThat(signatureRegistry.getProcedureSignatureFor(TEST_PROCEDURE_NAME), is(nullValue()));

    signatureMap.put(TEST_PROCEDURE_NAME, PROCEDURE_SIGNATURE);
    assertThat(signatureRegistry.getProcedureSignatureFor(TEST_PROCEDURE_NAME), equalTo(PROCEDURE_SIGNATURE));
  }

  @Test
  void testRegister() {
    HashMap<String, Signature> signatureMap = new HashMap<>();
    SignatureRegistry signatureRegistry = new SimpleSignatureRegistry(signatureMap);
    signatureRegistry.register(TEST_PROCEDURE_NAME, PROCEDURE_SIGNATURE);

    assertThat(signatureMap.isEmpty(), is(false));
    assertThat(signatureMap.get(TEST_PROCEDURE_NAME), equalTo(PROCEDURE_SIGNATURE));
    assertThat(signatureRegistry.getProcedureSignatureFor(TEST_PROCEDURE_NAME), equalTo(PROCEDURE_SIGNATURE));

  }

  @Test
  void testUnregister() {
    HashMap<String, Signature> signatureMap = new HashMap<>();
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
