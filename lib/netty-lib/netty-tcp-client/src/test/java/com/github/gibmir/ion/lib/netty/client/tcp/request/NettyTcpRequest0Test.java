package com.github.gibmir.ion.lib.netty.client.tcp.request;

import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.common.sender.JsonRpcSender;
import com.github.gibmir.ion.scanner.signature.JsonRemoteProcedureSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NettyTcpRequest0Test {

  private JsonRpcSender sender;
  private SocketAddress socketAddress;
  private Jsonb jsonb;
  private Charset charset;
  private NettyTcpRequest0<Object> request0;

  @BeforeEach
  void beforeEach() {
    sender = mock(JsonRpcSender.class);
    socketAddress = mock(SocketAddress.class);
    jsonb = mock(Jsonb.class);
    charset = mock(Charset.class);
    request0 = new NettyTcpRequest0<>(sender, socketAddress, jsonb, charset, mock(JsonRemoteProcedureSignature.class));
  }

  @Test
  void smoke() {
    assertThat(request0.charset(), equalTo(charset));
    assertThat(request0.socketAddress(), equalTo(socketAddress));
    assertThat(request0.jsonb(), equalTo(jsonb));
  }

  @Test
  void testCall() {
    String testId = "testId";
    request0.call(testId);
    ArgumentCaptor<RequestDto> dtoCaptor = ArgumentCaptor.forClass(RequestDto.class);
    verify(sender).send(eq(testId), dtoCaptor.capture(), eq(jsonb), eq(charset), any(), eq(socketAddress));
    assertThat(dtoCaptor.getValue().getId(), equalTo(testId));
  }

  @Test
  void testNotificationCall() {
    request0.notificationCall();
    verify(sender).send(isA(NotificationDto.class), eq(jsonb), eq(charset), eq(socketAddress));
  }

  @Test
  void testJsonb() {
    Jsonb newJsonb = mock(Jsonb.class);
    NettyTcpRequest0<Object> updated = request0.jsonb(newJsonb);
    assertThat(updated.jsonb(), not(this.jsonb));
    assertThat(updated.jsonb(), equalTo(newJsonb));
  }

  @Test
  void testSocketAddress() {
    SocketAddress newAddress = mock(SocketAddress.class);
    NettyTcpRequest0<Object> updated = request0.socketAddress(newAddress);
    assertThat(updated.socketAddress(), not(this.socketAddress));
    assertThat(updated.socketAddress(), equalTo(newAddress));
  }

  @Test
  void testCharset() {
    Charset newCharset = mock(Charset.class);
    NettyTcpRequest0<Object> updated = request0.charset(newCharset);
    assertThat(updated.charset(), equalTo(newCharset));
  }
}
