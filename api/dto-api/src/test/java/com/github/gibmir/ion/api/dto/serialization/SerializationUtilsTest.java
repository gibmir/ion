package com.github.gibmir.ion.api.dto.serialization;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.error.JsonRpcError;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import javax.json.JsonStructure;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.containsStringIgnoringCase;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SerializationUtilsTest {

  public static final Jsonb JSONB = JsonbBuilder.create();
  public static final int RESULT_OBJECT = 1;
  public static final SuccessResponse SUCCESS_RESPONSE_WITH_NULL_ID = SuccessResponse.createWithNullId(RESULT_OBJECT);

  @Test
  void testExtractSuccessResponseFromCorrectSuccessResponse() {
    JsonStructure structure = JSONB.fromJson(JSONB.toJson(SUCCESS_RESPONSE_WITH_NULL_ID), JsonStructure.class);
    JsonRpcResponse jsonRpcResponse = assertDoesNotThrow(
      () -> SerializationUtils.extractResponseFrom(structure, int.class, JSONB));

    assertTrue(jsonRpcResponse instanceof SuccessResponse);
    assertThat(((SuccessResponse) jsonRpcResponse).getResult(), is(RESULT_OBJECT));
  }

  public static final String TEST_ID = "test-1";
  public static final String SUCCESS_RESPONSE_JSON = "{\"jsonrpc\":\"2.0\"" +
    ",\"id\":\"" + TEST_ID + "\"" +
    ",\"result\":" + RESULT_OBJECT + "}";

  @Test
  void testExtractSuccessResponseFromCorrectStructure() {
    JsonStructure structure = JSONB.fromJson(SUCCESS_RESPONSE_JSON, JsonStructure.class);
    JsonRpcResponse jsonRpcResponse = assertDoesNotThrow(
      () -> SerializationUtils.extractResponseFrom(structure, int.class, JSONB));

    assertTrue(jsonRpcResponse instanceof SuccessResponse);
    assertThat(((SuccessResponse) jsonRpcResponse).getId(), is(TEST_ID));
    assertThat(((SuccessResponse) jsonRpcResponse).getResult(), is(RESULT_OBJECT));
  }

  public static final String RESPONSE_WITH_NULL_ID = "{\"jsonrpc\":\"2.0\"" +
    ",\"id\":\"" + null + "\"" +
    ",\"result\":" + RESULT_OBJECT + "}";

  @Test
  void testExtractSuccessResponseFromCorrectStructureWithNullId() {
    JsonStructure structure = JSONB.fromJson(RESPONSE_WITH_NULL_ID, JsonStructure.class);
    JsonRpcResponse jsonRpcResponse = assertDoesNotThrow(
      () -> SerializationUtils.extractResponseFrom(structure, int.class, JSONB));

    assertTrue(jsonRpcResponse instanceof SuccessResponse);
    assertThat(((SuccessResponse) jsonRpcResponse).getResult(), is(RESULT_OBJECT));
  }

  public static final String RESPONSE_WITHOUT_PROTOCOL = "{\"id\":\"" + null + "\"" +
    ",\"result\":" + RESULT_OBJECT + "}";

  @Test
  void testExtractSuccessResponseFromStructureWithoutProtocol() {
    JsonStructure structure = JSONB.fromJson(RESPONSE_WITHOUT_PROTOCOL, JsonStructure.class);
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
      () -> SerializationUtils.extractResponseFrom(structure, int.class, JSONB));

    assertThat(illegalArgumentException.getMessage(), containsStringIgnoringCase("Protocol"));
  }

  public static final String RESPONSE_WITHOUT_ID = "{\"jsonrpc\":\"2.0\"" +
    ",\"result\":" + RESULT_OBJECT + "}";

  @Test
  void testExtractSuccessResponseFromStructureWithoutId() {
    JsonStructure structure = JSONB.fromJson(RESPONSE_WITHOUT_ID, JsonStructure.class);
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
      () -> SerializationUtils.extractResponseFrom(structure, int.class, JSONB));

    assertThat(illegalArgumentException.getMessage(), containsStringIgnoringCase("Id"));
  }

  public static class TestException extends RuntimeException {

  }

  public static final TestException TEST_EXCEPTION = new TestException();
  public static final ErrorResponse ERROR_RESPONSE = ErrorResponse.withNullId(TEST_EXCEPTION);

  @Test
  void testExtractErrorResponseFromCorrectErrorResponse() {

    JsonStructure structure = JSONB.fromJson(JSONB.toJson(ERROR_RESPONSE), JsonStructure.class);
    JsonRpcResponse jsonRpcResponse = assertDoesNotThrow(
      () -> SerializationUtils.extractResponseFrom(structure, int.class, JSONB));
    assertTrue(jsonRpcResponse instanceof ErrorResponse);
    assertThat(((ErrorResponse) jsonRpcResponse).getJsonRpcError().getMessage(),
      containsString(TestException.class.getName()));
  }

  public static final String TEST_ERROR_MESSAGE = "test-error-message";
  public static final int TEST_ERROR_CODE = -32000;
  public static final String ERROR_RESPONSE_JSON = "{\"jsonrpc\":\"2.0\"," +
    "\"id\":\"" + TEST_ID + "\"," +
    "\"error\":{\"code\":" + TEST_ERROR_CODE +
    ",\"message\":\"" + TEST_ERROR_MESSAGE + "\"}}";

  @Test
  void testExtractErrorResponseFromCorrectErrorResponseJson() {

    JsonStructure structure = JSONB.fromJson(ERROR_RESPONSE_JSON, JsonStructure.class);
    JsonRpcResponse jsonRpcResponse = assertDoesNotThrow(
      () -> SerializationUtils.extractResponseFrom(structure, int.class, JSONB));
    assertTrue(jsonRpcResponse instanceof ErrorResponse);
    assertThat(((ErrorResponse) jsonRpcResponse).getJsonRpcError().getMessage(),
      containsString(TEST_ERROR_MESSAGE));
  }

  public static final String ERROR_RESPONSE_WITHOUT_PROTOCOL =
    "{\"id\":\"" + TEST_ID + "\"," +
      "\"error\":{\"code\":" + TEST_ERROR_CODE +
      ",\"message\":\"" + TEST_ERROR_MESSAGE + "\"}}";

  @Test
  void testExtractErrorResponseFromErrorResponseWithoutProtocol() {
    JsonStructure structure = JSONB.fromJson(ERROR_RESPONSE_WITHOUT_PROTOCOL, JsonStructure.class);
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
      () -> SerializationUtils.extractResponseFrom(structure, int.class, JSONB));

    assertThat(illegalArgumentException.getMessage(), containsStringIgnoringCase("Protocol"));
  }

  public static final String ERROR_RESPONSE_WITHOUT_ID = "{\"jsonrpc\":\"2.0\"," +
    "\"error\":{\"code\":" + TEST_ERROR_CODE +
    ",\"message\":\"" + TEST_ERROR_MESSAGE + "\"}}";

  @Test
  void testExtractErrorResponseFromStructureWithoutId() {
    JsonStructure structure = JSONB.fromJson(ERROR_RESPONSE_WITHOUT_ID, JsonStructure.class);
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
      () -> SerializationUtils.extractResponseFrom(structure, int.class, JSONB));

    assertThat(illegalArgumentException.getMessage(), containsStringIgnoringCase("Id"));
  }

  public static final String RESPONSE_WITHOUT_RESULT = "{\"jsonrpc\":\"2.0\"" +
    ",\"id\":\"" + TEST_ID + "\"}";

  @Test
  void testExtractResponseFromStructureWithoutResult() {
    JsonStructure structure = JSONB.fromJson(RESPONSE_WITHOUT_RESULT, JsonStructure.class);
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
      () -> SerializationUtils.extractResponseFrom(structure, int.class, JSONB));

    assertThat(illegalArgumentException.getMessage(), containsStringIgnoringCase("Incorrect"));
  }

  @Test
  void testExtractErrorResponseFromIncorrectStructure() {
    JsonStructure structure = JSONB.fromJson(SUCCESS_RESPONSE_JSON, JsonStructure.class);
    JsonRpcResponse jsonRpcResponse = assertDoesNotThrow(
      () -> SerializationUtils.extractResponseFrom(structure, List.class, JSONB));

    assertTrue(jsonRpcResponse instanceof ErrorResponse);
    JsonRpcError jsonRpcError = ((ErrorResponse) jsonRpcResponse).getJsonRpcError();
    assertThat(jsonRpcError, is(Errors.INVALID_CHARACTER.getError()));
  }

  @Test
  void testExtractBatch() {
    JsonStructure batchStructure = JSONB.fromJson(JSONB.toJson(new Object[]{
      SUCCESS_RESPONSE_WITH_NULL_ID, SUCCESS_RESPONSE_WITH_NULL_ID
    }), JsonStructure.class);
    assertThrows(UnsupportedOperationException.class,
      () -> SerializationUtils.extractResponseFrom(batchStructure, int.class, JSONB));
  }
}
