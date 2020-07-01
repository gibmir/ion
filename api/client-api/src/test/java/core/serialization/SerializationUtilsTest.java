package core.serialization;

import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import core.environment.mock.TestJsonRpcRequestProcessor;
import core.environment.mock.TestJsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.method.signature.Signature;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import com.github.gibmir.ion.api.dto.serialization.SerializationUtils;
import core.environment.IonTestEnvironment;
import org.junit.jupiter.api.Test;

import javax.json.JsonObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
      .extractResponseFrom(IonTestEnvironment.JSONB.fromJson(IonTestEnvironment.JSONB.toJson(correctResponse), JsonObject.class), String.class, IonTestEnvironment.JSONB);

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
    RequestDto positionalRequestDto = new RequestDto(TEST_ID, TEST_METHOD_NAME, TEST_ARGS);
    JsonObject jsonObject = IonTestEnvironment.JSONB.fromJson(IonTestEnvironment.JSONB.toJson(positionalRequestDto), JsonObject.class);
    Map<String, Signature> methodSignature = new HashMap<>();
    methodSignature.put(TEST_METHOD_NAME, new Signature(String.class, String.class));
    JsonRpcRequest jsonRpcRequest = SerializationUtils.extractRequestFrom(jsonObject, methodSignature, IonTestEnvironment.JSONB);
    TestJsonRpcRequestProcessor testJsonRpcRequestProcessor = new TestJsonRpcRequestProcessor();
    jsonRpcRequest.processWith(testJsonRpcRequestProcessor);
    Optional<RequestDto> optionalRequest = testJsonRpcRequestProcessor.getRequestDto();
    assertThat(optionalRequest.isPresent(), equalTo(true));
    RequestDto requestDto = optionalRequest.get();
    assertThat(requestDto.getMethodName(), equalTo(TEST_METHOD_NAME));
    assertThat(requestDto.getArgs(), arrayContaining(TEST_ARGS));
  }
}
