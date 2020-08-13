package com.github.gibmir.ion.lib.netty.client.sender;

import com.github.gibmir.ion.api.client.batch.response.BatchResponse;
import com.github.gibmir.ion.api.client.batch.response.element.BatchElement;
import com.github.gibmir.ion.api.client.batch.response.element.type.BatchElementType;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ChannelMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ChannelPoolMapMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ChannelPoolStub;
import com.github.gibmir.ion.lib.netty.client.environment.mock.FutureChannelMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.JsonbMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ResponseListenerRegistryMock;
import com.github.gibmir.ion.lib.netty.client.request.batch.NettyBatch;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry.ResponseListenerRegistry;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolMap;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_ARGS;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_CHARSET;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_EMPTY_RESPONSE_LISTENER_REGISTRY;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_ID;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_PROCEDURE_NAME;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_SOCKET_ADDRESS;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TestException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class JsonRpcNettySenderTest {
  /*NOTIFICATIONS*/

  /**
   * @see JsonbMock#newMock(Object)
   */
  @Test
  void testSendNotificationSuccessfully() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, true)));
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
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
  void testSendNotificationExceptionally() {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock.newMock(TestException.class);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    NotificationDto notificationDto = new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS);
    Jsonb jsonb = JsonbMock.newMock(notificationDto);
    jsonRpcNettySender.send(notificationDto, jsonb, TEST_CHARSET, TEST_SOCKET_ADDRESS);

    verify(channel, never()).writeAndFlush(any());
  }

  /*POSITIONAL REQUEST*/
  @Test
  void testSendPositionalRequestSuccessfully() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, true)));
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    RequestDto requestDto = RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS);
    Jsonb jsonb = JsonbMock.newMock(requestDto);
    CompletableFuture<Object> futureResult = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb,
      TEST_CHARSET, Object.class, TEST_SOCKET_ADDRESS);

    verify(jsonb).toJson(requestDto);
    verify(channel).writeAndFlush(requestDto.toString().getBytes(TEST_CHARSET));
    assertNotNull(futureResult);
  }

  @Test
  void testSendPositionalRequestWithChannelFutureIsNotSuccess() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, /*is not success*/false)));
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    RequestDto requestDto = RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS);
    Jsonb jsonb = JsonbMock.newMock(requestDto);
    CompletableFuture<Object> futureResult = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb,
      TEST_CHARSET, Object.class, TEST_SOCKET_ADDRESS);

    verify(jsonb).toJson(requestDto);
    verify(channel, never()).writeAndFlush(any());
    assertNotNull(futureResult);
    ExecutionException executionException = assertThrows(ExecutionException.class, futureResult::get);
    assertTrue(executionException.getCause() instanceof ChannelException);
    assertTrue(executionException.getCause().getMessage().contains(/*can't */"acquire"));
  }

  @Test
  void testSendPositionalRequestExceptionally() {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock.newMock(TestException.class);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    RequestDto requestDto = RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS);
    Jsonb jsonb = JsonbMock.newMock(requestDto);
    CompletableFuture<Object> futureResult = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb,
      TEST_CHARSET, Object.class, TEST_SOCKET_ADDRESS);

    verify(channel, never()).writeAndFlush(any());
    assertNotNull(futureResult);
    assertTrue(futureResult.isCompletedExceptionally());
  }

  /*NAMED REQUEST*/
  @Test
  void testSendNamedRequestSuccessfully() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPool simpleChannelPool = ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, true));
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock.newMock(simpleChannelPool);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    RequestDto requestDto = RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap());
    Jsonb jsonb = JsonbMock.newMock(requestDto);
    CompletableFuture<Object> futureResult = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      Object.class, TEST_SOCKET_ADDRESS);

    verify(channel).writeAndFlush(requestDto.toString().getBytes(TEST_CHARSET));
    assertNotNull(futureResult);
  }

  @Test
  void testSendNamedRequestWithChannelFutureIsNotSuccess() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPool simpleChannelPool = ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, false));
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock.newMock(simpleChannelPool);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    RequestDto requestDto = RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap());
    Jsonb jsonb = JsonbMock.newMock(requestDto);
    CompletableFuture<Object> futureResult = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      Object.class, TEST_SOCKET_ADDRESS);

    verify(jsonb).toJson(requestDto);
    verify(channel, never()).writeAndFlush(any());
    assertNotNull(futureResult);
    ExecutionException executionException = assertThrows(ExecutionException.class, futureResult::get);
    assertTrue(executionException.getCause() instanceof ChannelException);
    assertTrue(executionException.getCause().getMessage().contains(/*can't */"acquire"));
  }

  @Test
  void testSendNamedRequestExceptionally() {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock.newMock(TestException.class);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    RequestDto requestDto = RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap());
    Jsonb jsonb = JsonbMock.newMock(requestDto);
    CompletableFuture<Object> futureResult = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      Object.class, TEST_SOCKET_ADDRESS);

    verify(channel, never()).writeAndFlush(any());
    assertNotNull(futureResult);
    assertTrue(futureResult.isCompletedExceptionally());
  }

  /*BATCH*/
  @Test
  void testSendBatchSuccessfully() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, true)));
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS)
    );
    NettyBatch nettyBatch = new NettyBatch(batchRequests, Collections.emptyList());
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);
    CompletableFuture<BatchResponse> futureResult = jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_SOCKET_ADDRESS);

    verify(jsonb).toJson(nettyBatch.getBatchRequestDto());
    verify(channel).writeAndFlush(nettyBatch.toString().getBytes(TEST_CHARSET));
    assertNotNull(futureResult);
  }

  @Test
  void testSendBatchSuccessfullyWithChannelFutureIsNotSuccess() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, false)));
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS)
    );
    NettyBatch nettyBatch = new NettyBatch(batchRequests,
      Collections.singletonList(new NettyBatch.AwaitBatchPart("", void.class)));
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);
    CompletableFuture<BatchResponse> futureResult = jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_SOCKET_ADDRESS);

    verify(jsonb).toJson(nettyBatch.getBatchRequestDto());
    verify(channel, never()).writeAndFlush(any());
    assertNotNull(futureResult);
    ExecutionException executionException = assertThrows(ExecutionException.class, futureResult::get);
    assertTrue(executionException.getCause() instanceof ChannelException);
    assertTrue(executionException.getCause().getMessage().contains(/*can't */"acquire"));
  }

  @Test
  void testSendBatchExceptionallyBecauseChannelPoolThrewException() {
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock.newMock(TestException.class);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS)
    );
    NettyBatch nettyBatch = new NettyBatch(batchRequests, List.of(new NettyBatch.AwaitBatchPart(TEST_ID, int.class)));
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);

    ExecutionException executionException = assertThrows(ExecutionException.class, () -> jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_SOCKET_ADDRESS).get());
    assertTrue(executionException.getCause() instanceof TestException);
  }

  @Test
  void testSendBatchExceptionallyBecauseResponseListenerRegistryThrewException() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, true)));
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(
      channelPoolMap, ResponseListenerRegistryMock.newMock(TestException.class));
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS)
    );
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = List.of(/*used with mock*/new NettyBatch.AwaitBatchPart(null, null));
    NettyBatch nettyBatch = new NettyBatch(batchRequests, awaitBatchParts);
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);

    assertThrows(TestException.class, () -> jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_SOCKET_ADDRESS));
  }

  @Test
  void testSendBatchWithSuccessResponseFutureCorrectProcessing() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, true)));
    Map<String, ResponseFuture> responseCache = new HashMap<>();
    ResponseListenerRegistry testResponseListenerRegistry = ResponseListenerRegistryMock.newStub(responseCache);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, testResponseListenerRegistry);
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS)
    );
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = List.of(/*used with mock*/new NettyBatch.AwaitBatchPart(TEST_ID, Object.class));
    NettyBatch nettyBatch = new NettyBatch(batchRequests, awaitBatchParts);
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);
    CompletableFuture<BatchResponse> batchResponseCompletableFuture = jsonRpcNettySender.send(nettyBatch, jsonb,
      TEST_CHARSET, TEST_SOCKET_ADDRESS);
    String expectedResult = "object";
    responseCache.get(TEST_ID).complete(expectedResult);
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
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, true)));
    Map<String, ResponseFuture> responsesCache = new HashMap<>();
    ResponseListenerRegistry testEmptyResponseListenerRegistry = ResponseListenerRegistryMock.newStub(responsesCache);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPoolMap, testEmptyResponseListenerRegistry);
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS)
    );
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = List.of(
      /*used with mock*/new NettyBatch.AwaitBatchPart(TEST_ID, Object.class),
      new NettyBatch.AwaitBatchPart(TEST_ID + 2, Object.class));
    NettyBatch nettyBatch = new NettyBatch(batchRequests, awaitBatchParts);
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);
    CompletableFuture<BatchResponse> batchResponseCompletableFuture = jsonRpcNettySender.send(nettyBatch, jsonb,
      TEST_CHARSET, TEST_SOCKET_ADDRESS);
    responsesCache.get(TEST_ID).completeExceptionally(new TestException());
    String expectedSecondFutureResult = "pog";
    responsesCache.get(TEST_ID + 2).complete(expectedSecondFutureResult);
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
  void testCloseNull() {
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(null, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);

    assertDoesNotThrow(jsonRpcNettySender::close);
  }
}
