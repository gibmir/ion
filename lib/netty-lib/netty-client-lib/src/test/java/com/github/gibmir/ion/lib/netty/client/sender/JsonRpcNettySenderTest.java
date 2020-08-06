package com.github.gibmir.ion.lib.netty.client.sender;

import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ChannelMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.ChannelPoolMock;
import com.github.gibmir.ion.lib.netty.client.environment.mock.JsonbMock;
import com.github.gibmir.ion.lib.netty.client.sender.pool.ChannelPool;
import io.netty.channel.Channel;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_ARGS;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_CHARSET;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_ID;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_PROCEDURE_NAME;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_RESPONSE_LISTENER_REGISTRY;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TEST_SOCKET_ADDRESS;
import static com.github.gibmir.ion.lib.netty.client.environment.NettyClientTestEnvironment.TestException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class JsonRpcNettySenderTest {

  /**
   * @see JsonbMock#newMock(Object)
   */
  @Test
  void testSendNotificationSuccessfully() {
    Channel channel = ChannelMock.newMock();
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
    Channel channel = ChannelMock.newMock();
    ChannelPool channelPool = ChannelPoolMock.newMock(TestException.class);
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, TEST_RESPONSE_LISTENER_REGISTRY);
    NotificationDto notificationDto = new NotificationDto(TEST_PROCEDURE_NAME, TEST_ARGS);
    Jsonb jsonb = JsonbMock.newMock(notificationDto);
    jsonRpcNettySender.send(notificationDto, jsonb, TEST_CHARSET, TEST_SOCKET_ADDRESS);

    verify(jsonb, never()).toJson(any());
    verify(channel, never()).writeAndFlush(any());
  }

  @Test
  void testSendPositionalRequestSuccessfully() {
    Channel channel = ChannelMock.newMock();
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
    Channel channel = ChannelMock.newMock();
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

  @Test
  void testSendNamedRequestSuccessfully() {
    Channel channel = ChannelMock.newMock();
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
    Channel channel = ChannelMock.newMock();
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
