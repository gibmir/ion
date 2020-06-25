package com.github.gibmir.ion.api.core.serialization;

import com.github.gibmir.ion.api.core.environment.mock.TestJsonRpcRequestProcessor;
import com.github.gibmir.ion.api.core.environment.mock.TestJsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.method.signature.Signature;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.positional.PositionalRequest;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import com.github.gibmir.ion.api.dto.serialization.SerializationUtils;
import org.junit.jupiter.api.Test;

import javax.json.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.github.gibmir.ion.api.core.environment.IonTestEnvironment.JSONB;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.equalTo;

public class SerializationUtilsTest {

  public static final String TEST_RESULT = "test-result";
  public static final String TEST_ID = "test-id";
  public static final String TEST_METHOD_NAME = "test-method-name";
  public static final Object[] TEST_ARGS = {"arg1", "arg2"};

  @Test
  void testExtractResponseFromCorrectJsonObject() {
    JsonRpcResponse correctResponse = new SuccessResponse(TEST_ID, TEST_RESULT);
    JsonRpcResponse jsonRpcResponse = SerializationUtils
      .extractResponseFrom(JSONB.fromJson(JSONB.toJson(correctResponse), JsonObject.class), String.class, JSONB);

    TestJsonRpcResponseProcessor testJsonRpcResponseProcessor = new TestJsonRpcResponseProcessor();
    jsonRpcResponse.processWith(testJsonRpcResponseProcessor);


    assertThat(testJsonRpcResponseProcessor.getErrorResponse().isPresent(), equalTo(false));
    Optional<SuccessResponse> optionalResponse = testJsonRpcResponseProcessor.getSuccessResponse();
    assertThat(optionalResponse.isPresent(), equalTo(true));
    SuccessResponse successResponse = optionalResponse.get();
    assertThat(successResponse.getId(), equalTo(TEST_ID));
    assertThat(successResponse.getResult(), equalTo(TEST_RESULT));
  }

  @Test
  void testExtractRequestFromCorrectJsonObject() {
    PositionalRequest positionalRequest = new PositionalRequest(TEST_ID, TEST_METHOD_NAME, TEST_ARGS);
    JsonObject jsonObject = JSONB.fromJson(JSONB.toJson(positionalRequest), JsonObject.class);
    Map<String, Signature> methodSignature = new HashMap<>();
    methodSignature.put(TEST_METHOD_NAME, new Signature(String.class, String.class));
    JsonRpcRequest jsonRpcRequest = SerializationUtils.extractRequestFrom(jsonObject, methodSignature, JSONB);
    TestJsonRpcRequestProcessor testJsonRpcRequestProcessor = new TestJsonRpcRequestProcessor();
    jsonRpcRequest.processWith(testJsonRpcRequestProcessor);
    Optional<PositionalRequest> optionalRequest = testJsonRpcRequestProcessor.getPositionalRequest();
    assertThat(optionalRequest.isPresent(), equalTo(true));
    PositionalRequest request = optionalRequest.get();
    assertThat(request.getId(), equalTo(TEST_ID));
    assertThat(request.getMethodName(), equalTo(TEST_METHOD_NAME));
    assertThat(request.getArgs(), arrayContaining(TEST_ARGS));
  }
}
