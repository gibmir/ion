package com.github.gibmir.ion.lib.netty.client.sender;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.NettyBatch;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ChannelMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ChannelPoolMapMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ChannelPoolStub;
import com.github.gibmir.ion.lib.netty.client.environment.mock.FutureChannelMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.JsonbMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ResponseListenerRegistryMock;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolMap;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_ARGS;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_CHARSET;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_EMPTY_RESPONSE_LISTENER_REGISTRY;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_ID;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_PROCEDURE_NAME;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_SOCKET_ADDRESS;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TestException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class JsonRpcSenderTest {
  /*NOTIFICATIONS*/

  /**
   * @see JsonbMock#newMock(Object)
   */
  @Test
  void testSendNotificationSuccessfully() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, true)));
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    NotificationDto notificationDto = NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_ARGS);
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
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    NotificationDto notificationDto = NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_ARGS);
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
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
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
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
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
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
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
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
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
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
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
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
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
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_ARGS)
    );
    NettyBatch nettyBatch = new NettyBatch(batchRequests, Collections.emptyList());
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);
    jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_SOCKET_ADDRESS);

    verify(jsonb).toJson(nettyBatch.getBatchRequestDto());
    verify(channel).writeAndFlush(nettyBatch.toString().getBytes(TEST_CHARSET));
  }

  @Test
  void testSendBatchSuccessfullyWithChannelFutureIsNotSuccess() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, false)));
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_ARGS)
    );
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);
    NettyBatch nettyBatch = new NettyBatch(batchRequests,
      Collections.singletonList(new NettyBatch.BatchPart<>("", responseCallback, void.class)));
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);
    assertThrows(ChannelException.class, () -> jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_SOCKET_ADDRESS));
  }

  @Test
  void testSendBatchExceptionallyBecauseChannelPoolThrewException() {
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock.newMock(TestException.class);
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_ARGS)
    );
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);
    NettyBatch nettyBatch = new NettyBatch(batchRequests, List.of(new NettyBatch.BatchPart<>(TEST_ID, responseCallback, int.class)));
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);
    assertThrows(TestException.class, () -> jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_SOCKET_ADDRESS));
  }

  @Test
  void testSendBatchExceptionallyBecauseResponseListenerRegistryThrewException() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, true)));
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(
      channelPoolMap, ResponseListenerRegistryMock.newMock(TestException.class));
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_ARGS)
    );
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);
    List<NettyBatch.BatchPart<?>> batchParts = List.of(/*used with mock*/new NettyBatch.BatchPart<>(null,
      responseCallback, null));
    NettyBatch nettyBatch = new NettyBatch(batchRequests, batchParts);
    Jsonb jsonb = JsonbMock.newMock(nettyBatch);
    assertThrows(TestException.class, () -> jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_SOCKET_ADDRESS));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testSendBatchWithSuccessResponseFutureCorrectProcessing() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, true)));
    Map<String, ResponseFuture> responseCache = new HashMap<>();
    ResponseListenerRegistry testResponseListenerRegistry = ResponseListenerRegistryMock.newStub(responseCache);
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, testResponseListenerRegistry);
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_ARGS)
    );
    ResponseCallback<Object> responseCallback = mock(ResponseCallback.class);
    List<NettyBatch.BatchPart<?>> batchParts = List.of(/*used with mock*/new NettyBatch.BatchPart<>(TEST_ID,
      responseCallback, Object.class));
    NettyBatch nettyBatch = new NettyBatch(batchRequests, batchParts);

    Jsonb jsonb = mock(Jsonb.class);
    doAnswer(invocation -> Arrays.toString(invocation.getArguments())).when(jsonb).toJson(any());
    jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET, TEST_SOCKET_ADDRESS);
    String expectedResult = "object";
    responseCache.get(TEST_ID).complete(expectedResult);

    verify(responseCallback).onResponse(any(), any());
  }

  @Test
  @SuppressWarnings("unchecked")
  void testSendBatchWithErrorResponseFutureCorrectProcessing() throws ExecutionException, InterruptedException {
    Channel channel = ChannelMock.emptyMock();
    ChannelPoolMap<SocketAddress, ? extends ChannelPool> channelPoolMap = ChannelPoolMapMock
      .newMock(ChannelPoolStub.createWith(FutureChannelMock.newMock(channel, true)));
    Map<String, ResponseFuture> responsesCache = new HashMap<>();
    ResponseListenerRegistry testEmptyResponseListenerRegistry = ResponseListenerRegistryMock.newStub(responsesCache);
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(channelPoolMap, testEmptyResponseListenerRegistry);
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_ARGS),
      NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_ARGS)
    );
    ResponseCallback<?> firstBatchCallback = mock(ResponseCallback.class);
    ResponseCallback<Object> secondBatchCallback = mock(ResponseCallback.class);
    List<NettyBatch.BatchPart<?>> batchParts = List.of(
      /*used with mock*/new NettyBatch.BatchPart<>(TEST_ID, firstBatchCallback, String.class),
      new NettyBatch.BatchPart<>(TEST_ID + 2, secondBatchCallback, String.class));
    NettyBatch nettyBatch = new NettyBatch(batchRequests, batchParts);
    Jsonb jsonb = mock(Jsonb.class);
    doAnswer(invocation -> Arrays.toString(invocation.getArguments())).when(jsonb).toJson(any());
    jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET, TEST_SOCKET_ADDRESS);

    responsesCache.get(TEST_ID).completeExceptionally(new TestException());

    ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
    verify(firstBatchCallback, times(1)).onResponse(any(), exceptionCaptor.capture());
    Throwable throwable = exceptionCaptor.getValue();
    assertTrue(throwable instanceof TestException);

    String expectedSecondFutureResult = "{\"secondResult\": 1}";
    responsesCache.get(TEST_ID + 2).complete(expectedSecondFutureResult);

    //callback was invoked
    verify(secondBatchCallback, times(1))
      .onResponse(any(), any());
  }

  /*CLOSE*/

  @Test
  void testCloseNull() {
    NettyTcpJsonRpcSender jsonRpcNettySender = new NettyTcpJsonRpcSender(null, TEST_EMPTY_RESPONSE_LISTENER_REGISTRY);

    assertDoesNotThrow(jsonRpcNettySender::close);
  }
}
