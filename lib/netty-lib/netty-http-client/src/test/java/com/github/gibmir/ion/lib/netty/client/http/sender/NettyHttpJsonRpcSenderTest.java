package com.github.gibmir.ion.lib.netty.client.http.sender;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.future.ResponseFuture;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.NettyBatch;
import io.netty.channel.Channel;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelFuture;
import io.netty.channel.pool.ChannelPool;
import io.netty.channel.pool.ChannelPoolMap;
import io.netty.channel.pool.SimpleChannelPool;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.FailedFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.SucceededFuture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class NettyHttpJsonRpcSenderTest {

  public static final String TEST_ID = "test-id";
  public static final String TEST_PROCEDURE_NAME = "test-procedure";
  public static final URI TEST_URI = URI.create("http://test-uri:60000");
  public static final Charset TEST_CHARSET = Charset.defaultCharset();
  public static final Object[] TEST_POSITIONAL_ARGS = new Object[]{"test-arg"};
  public static final Map<String, Object> TEST_NAMED_ARGS = Map.of("some-arg", "test-arg");
  Logger logger;
  Channel channel;
  ChannelPoolMap<SocketAddress, SimpleChannelPool> channelPoolMap;
  ResponseListenerRegistry responseListenerRegistry;
  NettyHttpJsonRpcSender jsonRpcNettySender;
  Jsonb jsonb;

  private static class TestException extends RuntimeException {
  }

  @BeforeEach
  @SuppressWarnings("unchecked")
  void beforeEach() {
    jsonb = mock(Jsonb.class);
    logger = mock(Logger.class);
    channel = mock(Channel.class);
    channelPoolMap = mock(ChannelPoolMap.class);
    responseListenerRegistry = mock(ResponseListenerRegistry.class);
    jsonRpcNettySender = new NettyHttpJsonRpcSender(channelPoolMap, responseListenerRegistry, logger);
  }

  /*NOTIFICATIONS*/
  @Test
  @SuppressWarnings("unchecked")
  void testSendNotificationSuccessfully() {
    NotificationDto notificationDto = NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS);
    String notificationJson = notificationDto.toString();
    doAnswer(__ -> notificationJson).when(jsonb).toJson(notificationDto);
    Future<Channel> acquireFuture = mock(Future.class);
    doAnswer(invocation -> {
      GenericFutureListener<Future<Channel>> listener = invocation.getArgument(0, GenericFutureListener.class);
      SucceededFuture<Channel> channelSucceededFuture = new SucceededFuture<>(mock(EventExecutor.class), channel);
      //executes lambda for testing
      listener.operationComplete(channelSucceededFuture);
      return mock(Future.class);
    }).when(acquireFuture).addListener(any());
    ChannelPool channelPool = mock(ChannelPool.class);
    doAnswer(__ -> acquireFuture).when(channelPool).acquire();
    doAnswer(__ -> channelPool).when(channelPoolMap).get(any());

    jsonRpcNettySender.send(notificationDto, jsonb, TEST_CHARSET, TEST_URI);
    verify(jsonb).toJson(notificationDto);
    ArgumentCaptor<FullHttpRequest> captor = ArgumentCaptor.forClass(FullHttpRequest.class);
    verify(channel).writeAndFlush(captor.capture());
    FullHttpRequest request = captor.getValue();
    assertThat(request.headers().get(HttpHeaderNames.HOST), equalTo(TEST_URI.getHost()));
    assertThat(request.headers().get(HttpHeaderNames.CONTENT_TYPE), equalTo("application/json"));
    assertThat(request.headers().get(HttpHeaderNames.CONTENT_LENGTH), equalTo(String.valueOf(notificationJson.length())));
    assertThat(new String(request.content().array(), TEST_CHARSET), containsString(notificationJson));
  }

  /**
   * @implNote if exception occurred while notification sending - do nothing
   */
  @Test
  void testSendNotificationExceptionally() {
    NotificationDto notificationDto = NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS);
    doThrow(TestException.class).when(jsonb).toJson(any());
    jsonRpcNettySender.send(notificationDto, jsonb, TEST_CHARSET, TEST_URI);

    verify(channel, never()).writeAndFlush(any());
    verify(logger).error(contains("notification"), isA(TestException.class));
  }

  /*POSITIONAL REQUEST*/
  @Test
  @SuppressWarnings("unchecked")
  void testSendPositionalRequestSuccessfully() {
    RequestDto requestDto = RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS);
    String requestJson = requestDto.toString();
    doAnswer(__ -> mock(ChannelFuture.class)).when(channel).writeAndFlush(any());
    doAnswer(__ -> requestJson).when(jsonb).toJson(requestDto);
    Future<Channel> acquireFuture = mock(Future.class);
    doAnswer(invocation -> {
      GenericFutureListener<Future<Channel>> listener = invocation.getArgument(0, GenericFutureListener.class);
      //successfully write into channel
      SucceededFuture<Channel> channelSucceededFuture = new SucceededFuture<>(mock(EventExecutor.class), channel);
      //executes lambda for testing
      listener.operationComplete(channelSucceededFuture);
      return mock(Future.class);
    }).when(acquireFuture).addListener(any());
    ChannelPool channelPool = mock(ChannelPool.class);
    doAnswer(__ -> acquireFuture).when(channelPool).acquire();
    doAnswer(__ -> channelPool).when(channelPoolMap).get(any());

    jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET, String.class, TEST_URI);
    verify(jsonb).toJson(requestDto);
    ArgumentCaptor<FullHttpRequest> captor = ArgumentCaptor.forClass(FullHttpRequest.class);
    verify(channel).writeAndFlush(captor.capture());
    FullHttpRequest request = captor.getValue();
    assertThat(request.headers().get(HttpHeaderNames.HOST), equalTo(TEST_URI.getHost()));
    assertThat(request.headers().get(HttpHeaderNames.CONTENT_TYPE), equalTo("application/json"));
    assertThat(request.headers().get(HttpHeaderNames.CONTENT_LENGTH), equalTo(String.valueOf(requestJson.length())));
    assertThat(new String(request.content().array(), TEST_CHARSET), containsString(requestJson));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testSendPositionalRequestChannelWriteExceptionally() {
    RequestDto requestDto = RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS);
    String requestJson = requestDto.toString();
    doAnswer(__ -> requestJson).when(jsonb).toJson(requestDto);
    Future<Channel> acquireFuture = mock(Future.class);
    doAnswer(invocation -> {
      GenericFutureListener<Future<Channel>> listener = invocation.getArgument(0, GenericFutureListener.class);
      //fail to write into channel
      FailedFuture<Channel> future = new FailedFuture<>(mock(EventExecutor.class), new TestException());
      //executes lambda for testing
      listener.operationComplete(future);
      return mock(Future.class);
    }).when(acquireFuture).addListener(any());
    ChannelPool channelPool = mock(ChannelPool.class);
    doAnswer(__ -> acquireFuture).when(channelPool).acquire();
    doAnswer(__ -> channelPool).when(channelPoolMap).get(any());

    CompletableFuture<Object> future = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      String.class, TEST_URI);

    assertThat(future.isCompletedExceptionally(), is(true));
    ExecutionException executionException = assertThrows(ExecutionException.class, future::get);
    assertThat(executionException.getCause() instanceof ChannelException, is(true));
    assertThat(executionException.getCause().getMessage(), containsString(TEST_URI.toString()));
  }

  @Test
  void testSendPositionalRequestWithChannelFutureException() {
    RequestDto requestDto = RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS);
    doThrow(TestException.class).when(responseListenerRegistry).register(any());

    CompletableFuture<Object> futureResponse = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      String.class, TEST_URI);

    verify(channel, never()).writeAndFlush(any());
    ExecutionException executionException = assertThrows(ExecutionException.class, futureResponse::get);
    assertThat(executionException.getCause() instanceof TestException, is(true));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testSendPositionalRequestWithCompletion() {
    RequestDto requestDto = RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS);
    String requestJson = requestDto.toString();
    doAnswer(__ -> mock(ChannelFuture.class)).when(channel).writeAndFlush(any());
    doAnswer(__ -> requestJson).when(jsonb).toJson(requestDto);
    Future<Channel> acquireFuture = mock(Future.class);
    doAnswer(invocation -> {
      GenericFutureListener<Future<Channel>> listener = invocation.getArgument(0, GenericFutureListener.class);
      //successfully write into channel
      SucceededFuture<Channel> channelSucceededFuture = new SucceededFuture<>(mock(EventExecutor.class), channel);
      //executes lambda for testing
      listener.operationComplete(channelSucceededFuture);
      return mock(Future.class);
    }).when(acquireFuture).addListener(any());
    ChannelPool channelPool = mock(ChannelPool.class);
    doAnswer(__ -> acquireFuture).when(channelPool).acquire();
    doAnswer(__ -> channelPool).when(channelPoolMap).get(any());

    ArgumentCaptor<ResponseFuture> captor = ArgumentCaptor.forClass(ResponseFuture.class);
    doNothing().when(responseListenerRegistry).register(captor.capture());
    jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET, String.class, TEST_URI);

    CompletableFuture<Object> futureResponse = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      String.class, TEST_URI);
    assertThat(futureResponse.isDone(), is(false));
    ResponseFuture future = captor.getValue();
    future.complete("");
    assertThat(futureResponse.isDone(), is(true));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testSendPositionalRequestWithException() {
    RequestDto requestDto = RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS);
    String requestJson = requestDto.toString();
    doAnswer(__ -> mock(ChannelFuture.class)).when(channel).writeAndFlush(any());
    doAnswer(__ -> requestJson).when(jsonb).toJson(requestDto);
    Future<Channel> acquireFuture = mock(Future.class);
    doAnswer(invocation -> {
      GenericFutureListener<Future<Channel>> listener = invocation.getArgument(0, GenericFutureListener.class);
      //successfully write into channel
      SucceededFuture<Channel> channelSucceededFuture = new SucceededFuture<>(mock(EventExecutor.class), channel);
      //executes lambda for testing
      listener.operationComplete(channelSucceededFuture);
      return mock(Future.class);
    }).when(acquireFuture).addListener(any());
    ChannelPool channelPool = mock(ChannelPool.class);
    doAnswer(__ -> acquireFuture).when(channelPool).acquire();
    doAnswer(__ -> channelPool).when(channelPoolMap).get(any());

    ArgumentCaptor<ResponseFuture> captor = ArgumentCaptor.forClass(ResponseFuture.class);
    doNothing().when(responseListenerRegistry).register(captor.capture());
    jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET, String.class, TEST_URI);

    CompletableFuture<Object> futureResponse = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      String.class, TEST_URI);
    assertThat(futureResponse.isDone(), is(false));
    ResponseFuture future = captor.getValue();
    future.completeExceptionally(new TestException());
    assertThat(futureResponse.isDone(), is(true));
    assertThat(futureResponse.isCompletedExceptionally(), is(true));
  }

  /*NAMED REQUEST*/
  @Test
  @SuppressWarnings("unchecked")
  void testSendNamedRequestSuccessfully() {
    RequestDto requestDto = RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, TEST_NAMED_ARGS);
    String requestJson = requestDto.toString();
    doAnswer(__ -> mock(ChannelFuture.class)).when(channel).writeAndFlush(any());
    doAnswer(__ -> requestJson).when(jsonb).toJson(requestDto);
    Future<Channel> acquireFuture = mock(Future.class);
    doAnswer(invocation -> {
      GenericFutureListener<Future<Channel>> listener = invocation.getArgument(0, GenericFutureListener.class);
      //successfully write into channel
      SucceededFuture<Channel> channelSucceededFuture = new SucceededFuture<>(mock(EventExecutor.class), channel);
      //executes lambda for testing
      listener.operationComplete(channelSucceededFuture);
      return mock(Future.class);
    }).when(acquireFuture).addListener(any());
    ChannelPool channelPool = mock(ChannelPool.class);
    doAnswer(__ -> acquireFuture).when(channelPool).acquire();
    doAnswer(__ -> channelPool).when(channelPoolMap).get(any());

    jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET, String.class, TEST_URI);
    verify(jsonb).toJson(requestDto);
    ArgumentCaptor<FullHttpRequest> captor = ArgumentCaptor.forClass(FullHttpRequest.class);
    verify(channel).writeAndFlush(captor.capture());
    FullHttpRequest request = captor.getValue();
    assertThat(request.headers().get(HttpHeaderNames.HOST), equalTo(TEST_URI.getHost()));
    assertThat(request.headers().get(HttpHeaderNames.CONTENT_TYPE), equalTo("application/json"));
    assertThat(request.headers().get(HttpHeaderNames.CONTENT_LENGTH), equalTo(String.valueOf(requestJson.length())));
    assertThat(new String(request.content().array(), TEST_CHARSET), containsString(requestJson));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testSendNamedRequestChannelWriteExceptionally() {
    RequestDto requestDto = RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, TEST_NAMED_ARGS);
    String requestJson = requestDto.toString();
    doAnswer(__ -> requestJson).when(jsonb).toJson(requestDto);
    Future<Channel> acquireFuture = mock(Future.class);
    doAnswer(invocation -> {
      GenericFutureListener<Future<Channel>> listener = invocation.getArgument(0, GenericFutureListener.class);
      //fail to write into channel
      FailedFuture<Channel> future = new FailedFuture<>(mock(EventExecutor.class), new TestException());
      //executes lambda for testing
      listener.operationComplete(future);
      return mock(Future.class);
    }).when(acquireFuture).addListener(any());
    ChannelPool channelPool = mock(ChannelPool.class);
    doAnswer(__ -> acquireFuture).when(channelPool).acquire();
    doAnswer(__ -> channelPool).when(channelPoolMap).get(any());

    CompletableFuture<Object> future = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      String.class, TEST_URI);

    assertThat(future.isCompletedExceptionally(), is(true));
    ExecutionException executionException = assertThrows(ExecutionException.class, future::get);
    assertThat(executionException.getCause() instanceof ChannelException, is(true));
    assertThat(executionException.getCause().getMessage(), containsString(TEST_URI.toString()));
  }

  @Test
  void testSendNamedRequestWithChannelFutureException() {
    RequestDto requestDto = RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, TEST_NAMED_ARGS);
    doThrow(TestException.class).when(responseListenerRegistry).register(any());

    CompletableFuture<Object> futureResponse = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      String.class, TEST_URI);

    verify(channel, never()).writeAndFlush(any());
    ExecutionException executionException = assertThrows(ExecutionException.class, futureResponse::get);
    assertThat(executionException.getCause() instanceof TestException, is(true));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testSendNamedRequestWithCompletion() {
    RequestDto requestDto = RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, TEST_NAMED_ARGS);
    String requestJson = requestDto.toString();
    doAnswer(__ -> mock(ChannelFuture.class)).when(channel).writeAndFlush(any());
    doAnswer(__ -> requestJson).when(jsonb).toJson(requestDto);
    Future<Channel> acquireFuture = mock(Future.class);
    doAnswer(invocation -> {
      GenericFutureListener<Future<Channel>> listener = invocation.getArgument(0, GenericFutureListener.class);
      //successfully write into channel
      SucceededFuture<Channel> channelSucceededFuture = new SucceededFuture<>(mock(EventExecutor.class), channel);
      //executes lambda for testing
      listener.operationComplete(channelSucceededFuture);
      return mock(Future.class);
    }).when(acquireFuture).addListener(any());
    ChannelPool channelPool = mock(ChannelPool.class);
    doAnswer(__ -> acquireFuture).when(channelPool).acquire();
    doAnswer(__ -> channelPool).when(channelPoolMap).get(any());

    ArgumentCaptor<ResponseFuture> captor = ArgumentCaptor.forClass(ResponseFuture.class);
    doNothing().when(responseListenerRegistry).register(captor.capture());
    jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET, String.class, TEST_URI);

    CompletableFuture<Object> futureResponse = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      String.class, TEST_URI);
    assertThat(futureResponse.isDone(), is(false));
    ResponseFuture future = captor.getValue();
    future.complete("");
    assertThat(futureResponse.isDone(), is(true));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testSendNamedRequestWithException() {
    RequestDto requestDto = RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, TEST_NAMED_ARGS);
    String requestJson = requestDto.toString();
    doAnswer(__ -> mock(ChannelFuture.class)).when(channel).writeAndFlush(any());
    doAnswer(__ -> requestJson).when(jsonb).toJson(requestDto);
    Future<Channel> acquireFuture = mock(Future.class);
    doAnswer(invocation -> {
      GenericFutureListener<Future<Channel>> listener = invocation.getArgument(0, GenericFutureListener.class);
      //successfully write into channel
      SucceededFuture<Channel> channelSucceededFuture = new SucceededFuture<>(mock(EventExecutor.class), channel);
      //executes lambda for testing
      listener.operationComplete(channelSucceededFuture);
      return mock(Future.class);
    }).when(acquireFuture).addListener(any());
    ChannelPool channelPool = mock(ChannelPool.class);
    doAnswer(__ -> acquireFuture).when(channelPool).acquire();
    doAnswer(__ -> channelPool).when(channelPoolMap).get(any());

    ArgumentCaptor<ResponseFuture> captor = ArgumentCaptor.forClass(ResponseFuture.class);
    doNothing().when(responseListenerRegistry).register(captor.capture());
    jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET, String.class, TEST_URI);

    CompletableFuture<Object> futureResponse = jsonRpcNettySender.send(TEST_ID, requestDto, jsonb, TEST_CHARSET,
      String.class, TEST_URI);
    assertThat(futureResponse.isDone(), is(false));
    ResponseFuture future = captor.getValue();
    future.completeExceptionally(new TestException());
    assertThat(futureResponse.isDone(), is(true));
    assertThat(futureResponse.isCompletedExceptionally(), is(true));
  }

  /*BATCH*/
  @Test
  void testSendBatchSuccessfully() {
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS),
      NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS)
    );
    NettyBatch nettyBatch = new NettyBatch(batchRequests, Collections.emptyList());
    jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_URI);

    verify(jsonb).toJson(nettyBatch.getBatchRequestDto());
    verify(channel).writeAndFlush(nettyBatch.toString().getBytes(TEST_CHARSET));
  }

  @Test
  void testSendBatchSuccessfullyWithChannelFutureIsNotSuccess() {
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS),
      NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS)
    );
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);
    NettyBatch nettyBatch = new NettyBatch(batchRequests,
      Collections.singletonList(new NettyBatch.BatchPart<>("", responseCallback, void.class)));
    Jsonb jsonb = mock(Jsonb.class);
    assertThrows(ChannelException.class, () -> jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_URI));
  }

  @Test
  void testSendBatchExceptionallyBecauseChannelPoolThrewException() {
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS),
      NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS)
    );
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);
    NettyBatch nettyBatch = new NettyBatch(batchRequests, List.of(new NettyBatch.BatchPart<>(TEST_ID, responseCallback, int.class)));
    Jsonb jsonb = mock(Jsonb.class);
    assertThrows(TestException.class, () -> jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_URI));
  }

  @Test
  void testSendBatchExceptionallyBecauseResponseListenerRegistryThrewException() {
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS),
      NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS)
    );
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);
    List<NettyBatch.BatchPart<?>> batchParts = List.of(/*used with mock*/new NettyBatch.BatchPart<>(null,
      responseCallback, null));
    NettyBatch nettyBatch = new NettyBatch(batchRequests, batchParts);
    Jsonb jsonb = mock(Jsonb.class);
    assertThrows(TestException.class, () -> jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET,
      TEST_URI));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testSendBatchWithSuccessResponseFutureCorrectProcessing() {
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS),
      NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS)
    );
    ResponseCallback<Object> responseCallback = mock(ResponseCallback.class);
    List<NettyBatch.BatchPart<?>> batchParts = List.of(/*used with mock*/new NettyBatch.BatchPart<>(TEST_ID,
      responseCallback, Object.class));
    NettyBatch nettyBatch = new NettyBatch(batchRequests, batchParts);

    Jsonb jsonb = mock(Jsonb.class);
    doAnswer(invocation -> Arrays.toString(invocation.getArguments())).when(jsonb).toJson(any());
    jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET, TEST_URI);
    //    responseCache.get(TEST_ID).complete(expectedResult);

    verify(responseCallback).onResponse(any(), any());
  }

  @Test
  @SuppressWarnings("unchecked")
  void testSendBatchWithErrorResponseFutureCorrectProcessing() {
    List<JsonRpcRequest> batchRequests = List.of(
      RequestDto.named(TEST_ID, TEST_PROCEDURE_NAME, Collections.emptyMap()),
      RequestDto.positional(TEST_ID, TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS),
      NotificationDto.positional(TEST_PROCEDURE_NAME, TEST_POSITIONAL_ARGS)
    );
    ResponseCallback<?> firstBatchCallback = mock(ResponseCallback.class);
    ResponseCallback<Object> secondBatchCallback = mock(ResponseCallback.class);
    List<NettyBatch.BatchPart<?>> batchParts = List.of(
      /*used with mock*/new NettyBatch.BatchPart<>(TEST_ID, firstBatchCallback, String.class),
      new NettyBatch.BatchPart<>(TEST_ID + 2, secondBatchCallback, String.class));
    NettyBatch nettyBatch = new NettyBatch(batchRequests, batchParts);
    Jsonb jsonb = mock(Jsonb.class);
    doAnswer(invocation -> Arrays.toString(invocation.getArguments())).when(jsonb).toJson(any());
    jsonRpcNettySender.send(nettyBatch, jsonb, TEST_CHARSET, TEST_URI);

//    responsesCache.get(TEST_ID).completeExceptionally(new TestException());

    ArgumentCaptor<Throwable> exceptionCaptor = ArgumentCaptor.forClass(Throwable.class);
    verify(firstBatchCallback, times(1)).onResponse(any(), exceptionCaptor.capture());
    Throwable throwable = exceptionCaptor.getValue();
    assertTrue(throwable instanceof TestException);

    //    responsesCache.get(TEST_ID + 2).complete(expectedSecondFutureResult);

    //callback was invoked
    verify(secondBatchCallback, times(1))
      .onResponse(any(), any());
  }

}
