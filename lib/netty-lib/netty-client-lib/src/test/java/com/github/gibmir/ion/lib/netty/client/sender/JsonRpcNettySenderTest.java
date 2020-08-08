package com.github.gibmir.ion.lib.netty.client.sender;

import com.github.gibmir.ion.api.client.batch.response.BatchResponse;
import com.github.gibmir.ion.api.client.batch.response.element.BatchElement;
import com.github.gibmir.ion.api.client.batch.response.element.type.BatchElementType;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ChannelMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ChannelPoolMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.JsonbMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ResponseListenerRegistryMock;
import com.github.gibmir.ion.lib.netty.client.request.batch.NettyBatch;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.sender.pool.ChannelPool;
import io.netty.channel.Channel;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_ARGS;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_CHARSET;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_ID;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_PROCEDURE_NAME;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_RESPONSE_LISTENER_REGISTRY;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_SOCKET_ADDRESS;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TestException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class JsonRpcNettySenderTest {
  /*NOTIFICATIONS*/

  /**
   * @see JsonbMock#newMock(Object)
   */
  @Test
  void testSendNotificationSuccessfully() {
    Channel channel = ChannelMock.emptyMock();
    ChannelPool channelPool = ChannelPoolMock.newMock(channel);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);
    NotificationDto notificationDto = new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS);
    Jsonb jsonb = JsonbMock.newMock(notificationDto);
    jsonRpcNettySender.send(notificationDto, jsonb, TEST_CHARSET, TEST_SOCKET_ADDRESS);

    verify(jsonb).toJson(notificationDto);
    verify(channel).writeAndFlush(notificationDto.toString().getBytes(TEST_CHARSET));
  }

  /**
   * @implNote if exception occurred while notification sending - do nothing
   */
  @Test
  void testSendNotificationExceptionally() throws IOException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPool channelPool = ChannelPoolMock.newMock(TestException.class);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);
    NotificationDto notificationDto = new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS);
    Jsonb jsonb = JsonbMock.newMock(notificationDto);
    jsonRpcNettySender.send(notificationDto, jsonb, TEST_CHARSET, TEST_SOCKET_ADDRESS);

    verify(jsonb, never()).toJson(any());
    verify(channel, never()).writeAndFlush(any());
  }

  /*POSITIONAL REQUEST*/
  @Test
  void testSendPositionalRequestSuccessfully() {
    Channel channel = ChannelMock.emptyMock();
    ChannelPool channelPool = ChannelPoolMock.newMock(channel);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);
    RequestDto requestDto = RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS);
    Jsonb jsonb = JsonbMock.newMock(requestDto);
    CompletableFuture<Object> futureResult = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb,
      TEST_CHARSET, Object.class, TEST_SOCKET_ADDRESS);

    verify(jsonb).toJson(requestDto);
    verify(channel).writeAndFlush(requestDto.toString().getBytes(TEST_CHARSET));
    assertNotNull(futureResult);
  }

  @Test
  void testSendPositionalRequestExceptionally() throws IOException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPool channelPool = ChannelPoolMock.newMock(TestException.class);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);
    RequestDto requestDto = RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS);
    Jsonb jsonb = JsonbMock.newMock(requestDto);
    CompletableFuture<Object> futureResult = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb,
      TEST_CHARSET, Object.class, TEST_SOCKET_ADDRESS);

    verify(jsonb, never()).toJson(any());
    verify(channel, never()).writeAndFlush(any());
    assertNotNull(futureResult);
    assertTrue(futureResult.isCompletedExceptionally());
  }

  /*NAMED REQUEST*/
  @Test
  void testSendNamedRequestSuccessfully() {
    Channel channel = ChannelMock.emptyMock();
    ChannelPool channelPool = ChannelPoolMock.newMock(channel);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);
    RequestDto requestDto = RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap());
    Jsonb jsonb = JsonbMock.newMock(requestDto);
    CompletableFuture<Object> futureResult = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      Object.class, TEST_SOCKET_ADDRESS);

    verify(jsonb).toJson(requestDto);
    verify(channel).writeAndFlush(requestDto.toString().getBytes(TEST_CHARSET));
    assertNotNull(futureResult);
  }

  @Test
  void testSendNamedRequestExceptionally() throws IOException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPool channelPool = ChannelPoolMock.newMock(TestException.class);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);
    RequestDto requestDto = RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap());
    Jsonb jsonb = JsonbMock.newMock(requestDto);
    CompletableFuture<Object> futureResult = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      Object.class, TEST_SOCKET_ADDRESS);

    verify(jsonb, never()).toJson(any());
    verify(channel, never()).writeAndFlush(any());
    assertNotNull(futureResult);
    assertTrue(futureResult.isCompletedExceptionally());
  }

  /*BATCH*/
  @Test
  void testSendBatchSuccessfully() {
    Channel channel = ChannelMock.emptyMock();
    ChannelPool channelPool = ChannelPoolMock.newMock(channel);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);
    JsonRpcRequest[] batchRequests = {
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS)
    };
    NettyBatch nettyBatch = new NettyBatch(batchRequests, new ResponseFuture[0], new CompletableFuture[0]);
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);
    CompletableFuture<BatchResponse> futureResult = jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_SOCKET_ADDRESS);

    verify(jsonb).toJson(nettyBatch.getBatchRequestDto());
    verify(channel).writeAndFlush(nettyBatch.toString().getBytes(TEST_CHARSET));
    assertNotNull(futureResult);
  }

  @Test
  void testSendBatchExceptionallyBecauseChannelPoolThrewException() throws IOException {
    ChannelPool channelPool = ChannelPoolMock.newMock(TestException.class);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);
    JsonRpcRequest[] batchRequests = {
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS)
    };
    NettyBatch nettyBatch = new NettyBatch(batchRequests, new ResponseFuture[0], new CompletableFuture[0]);
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);

    assertThrows(TestException.class, () -> jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_SOCKET_ADDRESS));
  }

  @Test
  void testSendBatchExceptionallyBecauseResponseListenerRegistryThrewException() {
    Channel channel = ChannelMock.emptyMock();
    ChannelPool channelPool = ChannelPoolMock.newMock(channel);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool,
      ResponseListenerRegistryMock.newMock(TestException.class));
    JsonRpcRequest[] batchRequests = {
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS)
    };
    ResponseFuture[] responseFutures = {/*used with mock*/new ResponseFuture(null, null, null)};
    NettyBatch nettyBatch = new NettyBatch(batchRequests, responseFutures, new CompletableFuture[0]);
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);

    assertThrows(TestException.class, () -> jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_SOCKET_ADDRESS));
  }

  @Test
  void testSendBatchWithSuccessResponseFutureCorrectProcessing() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPool channelPool = ChannelPoolMock.newMock(channel);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);
    JsonRpcRequest[] batchRequests = {
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS)
    };
    CompletableFuture<Object> completableFuture = new CompletableFuture<>();
    ResponseFuture[] responseFutures = {/*used with mock*/new ResponseFuture(TEST_ID, Object.class,
      completableFuture)};
    NettyBatch nettyBatch = new NettyBatch(batchRequests, responseFutures, new CompletableFuture[]{completableFuture});
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);
    CompletableFuture<BatchResponse> batchResponseCompletableFuture = jsonRpcNettySender.send(nettyBatch, jsonb,
      TEST_CHARSET, TEST_SOCKET_ADDRESS);
    String expectedResult = "object";
    completableFuture.complete(expectedResult);
    BatchResponse batchResponse = batchResponseCompletableFuture.get();
    List<BatchElement> batchResponseElements = batchResponse.getBatchResponseElements();
    BatchElement batchElement = batchResponseElements.get(0);

    assertEquals(BatchElementType.SUCCESS, batchElement.resolveType());
    assertEquals(TEST_ID, batchElement.getId());
    assertEquals(expectedResult, batchElement.getResponseObject());
  }

  @Test
  void testSendBatchWithErrorResponseFutureCorrectProcessing() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPool channelPool = ChannelPoolMock.newMock(channel);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);
    JsonRpcRequest[] batchRequests = {
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS)
    };
    CompletableFuture<Object> completableFuture = new CompletableFuture<>();
    CompletableFuture<Object> secondFuture = new CompletableFuture<>();
    ResponseFuture[] responseFutures = {/*used with mock*/new ResponseFuture(TEST_ID, Object.class,
      completableFuture), new ResponseFuture(TEST_ID + 2, Object.class, secondFuture)};
    NettyBatch nettyBatch = new NettyBatch(batchRequests, responseFutures, new CompletableFuture[]{completableFuture});
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);
    CompletableFuture<BatchResponse> batchResponseCompletableFuture = jsonRpcNettySender.send(nettyBatch, jsonb,
      TEST_CHARSET, TEST_SOCKET_ADDRESS);
    completableFuture.completeExceptionally(new TestException());
    String expectedSecondFutureResult = "pog";
    secondFuture.complete(expectedSecondFutureResult);
    BatchResponse batchResponse = batchResponseCompletableFuture.get();
    List<BatchElement> batchResponseElements = batchResponse.getBatchResponseElements();

    BatchElement firstElement = batchResponseElements.get(0);
    assertEquals(BatchElementType.ERROR, firstElement.resolveType());
    assertEquals(TEST_ID, firstElement.getId());
    assertTrue(firstElement.getResponseObject() instanceof TestException);

    BatchElement secondElement = batchResponseElements.get(1);
    assertEquals(BatchElementType.SUCCESS, secondElement.resolveType());
    assertEquals(TEST_ID + 2, secondElement.getId());
    assertEquals(expectedSecondFutureResult, secondElement.getResponseObject());
  }

  /*CLOSE*/
  @Test
  void testCloseSuccessfully() throws IOException {
    ChannelPool channelPool = ChannelPoolMock.emptyMock();
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);

    assertDoesNotThrow(jsonRpcNettySender::close);
    verify(channelPool, times(1)).close();
  }

  @Test
  void testCloseExceptionally() throws IOException {
    ChannelPool channelPool = ChannelPoolMock.newMock(TestException.class);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);

    assertThrows(TestException.class, jsonRpcNettySender::close);
    verify(channelPool, times(1)).close();
  }

  @Test
  void testCloseNull() {
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(null, TEST_RESPONSE_LISTENER_REGISTRY);

    assertDoesNotThrow(jsonRpcNettySender::close);
  }
}
