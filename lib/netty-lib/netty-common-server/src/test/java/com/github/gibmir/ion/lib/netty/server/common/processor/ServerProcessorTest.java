package com.github.gibmir.ion.lib.netty.server.common.processor;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.batch.BatchResponseDto;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.Errors;
import com.github.gibmir.ion.api.dto.response.transfer.notification.NotificationResponse;
import com.github.gibmir.ion.api.message.BatchMessage;
import com.github.gibmir.ion.api.message.ExceptionMessage;
import com.github.gibmir.ion.api.message.Message;
import com.github.gibmir.ion.api.message.MessageType;
import com.github.gibmir.ion.api.message.NotificationMessage;
import com.github.gibmir.ion.api.message.RequestMessage;
import com.github.gibmir.ion.api.server.processor.request.JsonRpcRequestProcessor;
import com.github.gibmir.ion.api.server.processor.request.registry.ProcedureProcessorRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;
import static org.hamcrest.collection.IsArrayWithSize.emptyArray;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ServerProcessorTest {

  private ProcedureProcessorRegistry registry;
  private Logger logger;
  private ServerProcessor serverProcessor;

  @BeforeEach
  void beforeEach() {
    registry = mock(ProcedureProcessorRegistry.class);
    logger = mock(Logger.class);
    serverProcessor = new ServerProcessor(logger, registry);
  }

  //empty request
  @Test
  void testProcessWithEmptyRequest() {
    Message message = mock(Message.class);
    JsonRpcResponse response = serverProcessor.process(message);

    assertThat(response, instanceOf(ErrorResponse.class));
    ErrorResponse errorResponse = (ErrorResponse) response;
    assertThat(errorResponse.getJsonRpcError().getCode(), equalTo(Errors.INTERNAL_RPC_ERROR.getError().getCode()));
    assertThat(errorResponse.getJsonRpcError().getMessage(), containsString(Errors.INTERNAL_RPC_ERROR.getError().getMessage()));

    verify(message).resolveType();
  }

  //notification
  @Test
  void testProcessWithNotificationWithoutProcessor() {
    NotificationMessage message = mock(NotificationMessage.class);
    doAnswer(__ -> MessageType.NOTIFICATION).when(message).resolveType();
    String testMethodName = "testMethodName";
    doAnswer(__ -> testMethodName).when(message).getMethodName();
    doAnswer(__ -> message).when(message).asNotification();
    JsonRpcResponse response = serverProcessor.process(message);

    assertThat(response, is(NotificationResponse.INSTANCE));

    verify(logger).error(anyString(), eq(testMethodName));
  }

  @Test
  void testProcessWithNotificationAndProcessor() {
    String testProcedure = "testProcedure";
    JsonRpcRequestProcessor processor = mock(JsonRpcRequestProcessor.class);
    doAnswer(__ -> processor).when(registry).getProcedureProcessorFor(testProcedure);
    NotificationMessage message = mock(NotificationMessage.class);
    doAnswer(__ -> MessageType.NOTIFICATION).when(message).resolveType();
    doAnswer(__ -> testProcedure).when(message).getMethodName();
    doAnswer(__ -> message).when(message).asNotification();
    JsonRpcResponse response = serverProcessor.process(message);

    assertThat(response, is(NotificationResponse.INSTANCE));

    verify(processor).processNotification(eq(testProcedure), any());
    verify(logger).debug(any(), eq(MessageType.NOTIFICATION));
  }

  //request
  @Test
  void testProcessWithRequestAndProcessor() {
    String testProcedure = "testProcedure";
    JsonRpcRequestProcessor processor = mock(JsonRpcRequestProcessor.class);
    JsonRpcResponse testResponse = mock(JsonRpcResponse.class);
    doAnswer(__ -> testResponse).when(processor).processRequest(any(), anyString(), anyString());
    doAnswer(__ -> processor).when(registry).getProcedureProcessorFor(testProcedure);
    RequestMessage message = mock(RequestMessage.class);
    doAnswer(__ -> MessageType.REQUEST).when(message).resolveType();
    String testId = "testId";
    doAnswer(__ -> testId).when(message).getId();
    doAnswer(__ -> testProcedure).when(message).getMethodName();
    String argsJson = "args";
    doAnswer(__ -> argsJson).when(message).getArgumentsJson();
    doAnswer(__ -> message).when(message).asRequest();
    JsonRpcResponse response = serverProcessor.process(message);

    assertThat(response, is(testResponse));

    verify(processor).processRequest(eq(testId), eq(testProcedure), any());
    verify(logger).debug(any(), eq(MessageType.REQUEST));
  }

  @Test
  void testProcessWithRequestAndWithoutProcessor() {
    JsonRpcRequestProcessor processor = mock(JsonRpcRequestProcessor.class);
    JsonRpcResponse testResponse = mock(JsonRpcResponse.class);
    doAnswer(__ -> testResponse).when(processor).processRequest(any(), anyString(), anyString());
    RequestMessage message = mock(RequestMessage.class);
    doAnswer(__ -> MessageType.REQUEST).when(message).resolveType();
    String testId = "testId";
    doAnswer(__ -> testId).when(message).getId();
    String testProcedure = "testProcedure";
    doAnswer(__ -> testProcedure).when(message).getMethodName();
    String argsJson = "args";
    doAnswer(__ -> argsJson).when(message).getArgumentsJson();
    doAnswer(__ -> message).when(message).asRequest();
    ErrorResponse response = (ErrorResponse) serverProcessor.process(message);

    assertThat(response.getId(), is(testId));
    assertThat(response.getJsonRpcError().getCode(), is(Errors.REQUEST_METHOD_NOT_FOUND.getError().getCode()));
    assertThat(response.getJsonRpcError().getMessage(), containsString(Errors.REQUEST_METHOD_NOT_FOUND.getError().getMessage()));
    assertThat(response.getJsonRpcError().getMessage(), containsString(testProcedure));

    verify(logger).debug(any(), eq(MessageType.REQUEST));
  }

  //batch
  @Test
  void testProcessWithEmptyBatch() {
    BatchMessage message = mock(BatchMessage.class);
    doAnswer(__ -> MessageType.BATCH).when(message).resolveType();
    doAnswer(__ -> message).when(message).asBatch();

    Message[] emptyBatch = new Message[]{};
    doAnswer(__ -> emptyBatch).when(message).getMessages();
    doAnswer(__ -> message).when(message).asRequest();
    BatchResponseDto response = (BatchResponseDto) serverProcessor.process(message);

    verify(logger).debug(any(), eq(MessageType.BATCH));
    assertThat(response.getJsonRpcResponses(), is(emptyArray()));
  }

  @Test
  void testProcessWithRequestBatchWithoutProcessor() {
    BatchMessage batch = mock(BatchMessage.class);
    doAnswer(__ -> MessageType.BATCH).when(batch).resolveType();
    doAnswer(__ -> batch).when(batch).asBatch();

    RequestMessage request = mock(RequestMessage.class);
    doAnswer(__ -> MessageType.REQUEST).when(request).resolveType();
    String testId = "testId";
    doAnswer(__ -> testId).when(request).getId();
    String testProcedure = "testProcedure";
    doAnswer(__ -> testProcedure).when(request).getMethodName();
    String argsJson = "args";
    doAnswer(__ -> argsJson).when(request).getArgumentsJson();
    doAnswer(__ -> request).when(request).asRequest();
    Message[] batchMessages = new Message[]{request};

    doAnswer(__ -> batchMessages).when(batch).getMessages();
    doAnswer(__ -> batch).when(batch).asRequest();
    BatchResponseDto response = (BatchResponseDto) serverProcessor.process(batch);

    verify(logger).debug(any(), eq(MessageType.BATCH));
    assertThat(response.getJsonRpcResponses(), is(arrayWithSize(batchMessages.length)));
  }

  @Test
  void testProcessWithIncorrectBatchWithoutProcessor() {
    BatchMessage batch = mock(BatchMessage.class);
    doAnswer(__ -> MessageType.BATCH).when(batch).resolveType();
    doAnswer(__ -> batch).when(batch).asBatch();

    RequestMessage request = mock(RequestMessage.class);
    doAnswer(__ -> request).when(request).asRequest();
    Message[] batchMessages = new Message[]{request};

    doAnswer(__ -> batchMessages).when(batch).getMessages();
    doAnswer(__ -> batch).when(batch).asRequest();
    BatchResponseDto response = (BatchResponseDto) serverProcessor.process(batch);

    verify(logger).debug(any(), eq(MessageType.BATCH));
    //batch part without type
    assertThat(response.getJsonRpcResponses(), is(emptyArray()));
  }

  @Test
  void testProcessWithExceptionBatchWithoutProcessor() {
    BatchMessage batch = mock(BatchMessage.class);
    doAnswer(__ -> MessageType.BATCH).when(batch).resolveType();
    doAnswer(__ -> batch).when(batch).asBatch();

    ExceptionMessage message = mock(ExceptionMessage.class);
    doAnswer(__ -> MessageType.EXCEPTION).when(message).resolveType();
    doAnswer(__ -> message).when(message).asException();
    String testExceptionMessage = "testExceptionMessage";
    doAnswer(__ -> testExceptionMessage).when(message).getMessage();
    String testExceptionId = "testId";
    doAnswer(__ -> testExceptionId).when(message).getId();
    int testExceptionCode = 1;
    doAnswer(__ -> testExceptionCode).when(message).getCode();
    Message[] batchMessages = new Message[]{message};

    doAnswer(__ -> batchMessages).when(batch).getMessages();
    doAnswer(__ -> batch).when(batch).asRequest();
    BatchResponseDto response = (BatchResponseDto) serverProcessor.process(batch);

    verify(logger).debug(any(), eq(MessageType.BATCH));
    assertThat(response.getJsonRpcResponses(), is(arrayWithSize(batchMessages.length)));
  }

  @Test
  void testProcessWithNotificationBatchWithoutProcessor() {
    BatchMessage batch = mock(BatchMessage.class);
    doAnswer(__ -> MessageType.BATCH).when(batch).resolveType();
    doAnswer(__ -> batch).when(batch).asBatch();

    NotificationMessage message = mock(NotificationMessage.class);
    doAnswer(__ -> MessageType.NOTIFICATION).when(message).resolveType();
    String testProcedure = "testProcedure";
    doAnswer(__ -> testProcedure).when(message).getMethodName();
    doAnswer(__ -> message).when(message).asNotification();
    Message[] batchMessages = new Message[]{message};

    doAnswer(__ -> batchMessages).when(batch).getMessages();
    doAnswer(__ -> batch).when(batch).asRequest();
    BatchResponseDto response = (BatchResponseDto) serverProcessor.process(batch);

    verify(logger).debug(any(), eq(MessageType.BATCH));
    // There is no response for notification
    assertThat(response.getJsonRpcResponses(), is(emptyArray()));
  }

  // exceptions
  @Test
  void testProcessWithException() {
    ExceptionMessage message = mock(ExceptionMessage.class);
    doAnswer(__ -> MessageType.EXCEPTION).when(message).resolveType();
    doAnswer(__ -> message).when(message).asException();
    String testExceptionMessage = "testExceptionMessage";
    doAnswer(__ -> testExceptionMessage).when(message).getMessage();
    String testExceptionId = "testId";
    doAnswer(__ -> testExceptionId).when(message).getId();
    int testExceptionCode = 1;
    doAnswer(__ -> testExceptionCode).when(message).getCode();
    ErrorResponse response = (ErrorResponse) serverProcessor.process(message);

    assertThat(response.getId(), is(testExceptionId));
    assertThat(response.getJsonRpcError().getCode(), is(testExceptionCode));
    assertThat(response.getJsonRpcError().getMessage(), is(testExceptionMessage));

    verify(logger).debug(any(), eq(MessageType.EXCEPTION));
  }
}
