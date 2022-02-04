package com.github.gibmir.ion.lib.netty.server.common.channel.codecs.encoder;

import com.github.gibmir.ion.api.dto.response.transfer.batch.BatchResponseDto;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.error.JsonRpcError;
import com.github.gibmir.ion.api.dto.response.transfer.notification.NotificationResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NettyJsonRpcResponseProcessorTest {

  public static final String TEST_ID = "test-id";
  public static final String TEST_RESULT = "test-result";
  private Logger logger;
  private Jsonb jsonb;
  private List<Object> out;

  @BeforeEach
  @SuppressWarnings("unchecked")
  void beforeEach() {
    logger = mock(Logger.class);
    jsonb = mock(Jsonb.class);
    out = mock(List.class);
  }

  @Test
  void testSuccessProcess() {
    SuccessResponse successResponse = new SuccessResponse(TEST_ID, TEST_RESULT);
    doAnswer(__ -> TEST_RESULT).when(jsonb).toJson(eq(successResponse));
    Charset currentCharset = StandardCharsets.UTF_8;
    NettyJsonRpcResponseProcessor processor = new NettyJsonRpcResponseProcessor(logger, jsonb, currentCharset, out);
    processor.process(successResponse);

    verify(logger).debug(anyString(), eq(TEST_ID));
    verify(jsonb).toJson(eq(successResponse));
    verify(out).add(eq(TEST_RESULT.getBytes(currentCharset)));
  }

  @Test
  void testNotificationProcess() {
    doAnswer(__ -> TEST_RESULT).when(jsonb).toJson(eq(NotificationResponse.INSTANCE));
    Charset currentCharset = StandardCharsets.UTF_8;
    NettyJsonRpcResponseProcessor processor = new NettyJsonRpcResponseProcessor(logger, jsonb, currentCharset, out);
    processor.process(NotificationResponse.INSTANCE);

    verify(logger).trace(anyString());
  }

  @Test
  void testErrorResponse() {
    ErrorResponse errorResponse = new ErrorResponse(TEST_ID, Errors.INTERNAL_RPC_ERROR.getError());
    doAnswer(__ -> TEST_RESULT).when(jsonb).toJson(eq(errorResponse));
    Charset currentCharset = StandardCharsets.UTF_8;
    NettyJsonRpcResponseProcessor processor = new NettyJsonRpcResponseProcessor(logger, jsonb, currentCharset, out);
    processor.process(errorResponse);

    verify(logger).debug(anyString(), eq(TEST_ID));
    verify(jsonb).toJson(eq(errorResponse));
    verify(out).add(eq(TEST_RESULT.getBytes(currentCharset)));
  }

  @Test
  void testBatchResponse() {
    BatchResponseDto batch = new BatchResponseDto();
    doAnswer(__ -> TEST_RESULT).when(jsonb).toJson(eq(batch.getJsonRpcResponses()));
    Charset currentCharset = StandardCharsets.UTF_8;
    NettyJsonRpcResponseProcessor processor = new NettyJsonRpcResponseProcessor(logger, jsonb, currentCharset, out);
    processor.process(batch);

    verify(jsonb).toJson(eq(batch.getJsonRpcResponses()));
    verify(out).add(eq(TEST_RESULT.getBytes(currentCharset)));
  }
}
