package com.github.gibmir.ion.api.server.serialization;

import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.core.procedure.signature.ParameterizedJsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.server.cache.signature.SignatureRegistry;
import com.github.gibmir.ion.api.server.cache.signature.SimpleSignatureRegistry;
import org.junit.jupiter.api.Test;

import javax.json.JsonStructure;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.lang.invoke.MethodType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServerSerializationUtilsTest {

  public static final Jsonb JSONB = JsonbBuilder.create();
  public static final String ID = "12345-54321";
  public static final String PROCEDURE_NAME = "testProcedureName";
  public static final Object[] ARGS = {1, 2, 3};
  public static final RequestDto REQUEST_DTO = RequestDto.positional(ID, PROCEDURE_NAME, ARGS);
  public static final ParameterizedJsonRemoteProcedureSignature PARAMETERIZED_SIGNATURE =
    new ParameterizedJsonRemoteProcedureSignature("some", new String[]{"asd"}, new Type[]{int.class}, int.class,
      MethodType.methodType(int.class), 1);

  @Test
  void testExtractCorrectRequest() {
    JsonStructure jsonStructure = JSONB.fromJson(JSONB.toJson(REQUEST_DTO), JsonStructure.class);
    Map<String, JsonRemoteProcedureSignature> signatureMap = Collections.singletonMap(PROCEDURE_NAME, PARAMETERIZED_SIGNATURE);
    SignatureRegistry signatureRegistry = new SimpleSignatureRegistry(signatureMap);
    JsonRpcRequest jsonRpcRequest = ServerSerializationUtils.extractRequestFrom(jsonStructure, signatureRegistry, JSONB);
    assertTrue(jsonRpcRequest instanceof RequestDto);
    assertThat(((RequestDto) jsonRpcRequest).getId(), equalTo(ID));
    assertThat(((RequestDto) jsonRpcRequest).getArgs(), equalTo(ARGS));
  }

  public static final String REQUEST_STRING = "{\"jsonrpc\":\"2.0\"," +
    "\"method\":\"" + PROCEDURE_NAME + "\"," +
    "\"id\":\"" + ID + "\"," +
    "\"params\":[1,2,3]}";

  @Test
  void testExtractCorrectRequestFromString() {
    JsonStructure jsonStructure = JSONB.fromJson(REQUEST_STRING, JsonStructure.class);
    Map<String, JsonRemoteProcedureSignature> signatureMap = Collections.singletonMap(PROCEDURE_NAME, PARAMETERIZED_SIGNATURE);
    SignatureRegistry signatureRegistry = new SimpleSignatureRegistry(signatureMap);
    JsonRpcRequest jsonRpcRequest = ServerSerializationUtils.extractRequestFrom(jsonStructure, signatureRegistry, JSONB);
    assertTrue(jsonRpcRequest instanceof RequestDto);
    assertThat(((RequestDto) jsonRpcRequest).getId(), equalTo(ID));
    assertThat(((RequestDto) jsonRpcRequest).getArgs(), equalTo(ARGS));
  }

  public static final SignatureRegistry EMPTY_SIGNATURE_REGISTRY = new SimpleSignatureRegistry(Collections.emptyMap());

  @Test
  void testExtractIncorrectRequestWithUnsupportedMethod() {
    JsonStructure jsonStructure = JSONB.fromJson(REQUEST_STRING, JsonStructure.class);
    UnsupportedOperationException unsupportedOperationException = assertThrows(UnsupportedOperationException.class,
      () -> ServerSerializationUtils.extractRequestFrom(jsonStructure, EMPTY_SIGNATURE_REGISTRY, JSONB));
    assertThat(unsupportedOperationException.getMessage(), containsString(PROCEDURE_NAME));
  }

  public static final String REQUEST_STRING_WITHOUT_PROTOCOL = "{ \"method\":\"" + PROCEDURE_NAME + "\"," +
    "\"id\":\"" + ID + "\"," +
    "\"params\":[1,2,3]}";

  @Test
  void testExtractIncorrectRequestWithoutProtocol() {
    JsonStructure jsonStructure = JSONB.fromJson(REQUEST_STRING_WITHOUT_PROTOCOL, JsonStructure.class);
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
      () -> ServerSerializationUtils.extractRequestFrom(jsonStructure, EMPTY_SIGNATURE_REGISTRY, JSONB));
    assertThat(illegalArgumentException.getMessage(), containsStringIgnoringCase("Protocol"));
    assertThat(illegalArgumentException.getMessage(), containsStringIgnoringCase(ID));
    assertThat(illegalArgumentException.getMessage(), containsStringIgnoringCase(PROCEDURE_NAME));
  }

  public static final String REQUEST_STRING_WITHOUT_ID = "{\"jsonrpc\":\"2.0\"," +
    "\"method\":\"" + PROCEDURE_NAME + "\"," +
    "\"params\":[1,2,3]}";

  @Test
  void testExtractIncorrectRequestWithoutId() {
    JsonStructure jsonStructure = JSONB.fromJson(REQUEST_STRING_WITHOUT_ID, JsonStructure.class);
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
      () -> ServerSerializationUtils.extractRequestFrom(jsonStructure, EMPTY_SIGNATURE_REGISTRY, JSONB));
    assertThat(illegalArgumentException.getMessage(), containsStringIgnoringCase("Id"));
    assertThat(illegalArgumentException.getMessage(), containsStringIgnoringCase(PROCEDURE_NAME));
  }

  @Test
  void testExtractIncorrectRequestWithoutMethod() {
    JsonStructure jsonStructure = JSONB.fromJson("{\"jsonrpc\":\"2.0\"," +
      "\"id\":\"" + ID + "\"," +
      "\"params\":[1,2,3]}", JsonStructure.class);
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
      () -> ServerSerializationUtils.extractRequestFrom(jsonStructure, EMPTY_SIGNATURE_REGISTRY, JSONB));
    assertThat(illegalArgumentException.getMessage(), containsStringIgnoringCase("Method"));
    assertThat(illegalArgumentException.getMessage(), containsStringIgnoringCase(ID));
  }

  public static final String BATCH = "[{\"jsonrpc\":\"2.0\"," +
    "\"method\":\"testProcedureName\"," +
    "\"id\":\"12345-54321\"," +
    "\"params\":[1,2,3]}," +
    "{\"jsonrpc\":\"2.0\"," +
    "\"method\":\"testProcedureName\"," +
    "\"id\":\"12345-54321\"," +
    "\"params\":[1,2,3]}]\n";

  @Test
  void testExtractBatch() {
    JsonStructure jsonStructure = JSONB.fromJson(BATCH, JsonStructure.class);
    assertThrows(UnsupportedOperationException.class,
      () -> ServerSerializationUtils.extractRequestFrom(jsonStructure, EMPTY_SIGNATURE_REGISTRY, JSONB));
  }
}
