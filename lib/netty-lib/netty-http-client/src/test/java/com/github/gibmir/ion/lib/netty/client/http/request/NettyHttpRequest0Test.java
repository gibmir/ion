package com.github.gibmir.ion.lib.netty.client.http.request;

import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;
import com.github.gibmir.ion.scanner.signature.JsonRemoteProcedureSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NettyHttpRequest0Test {
  public static final URI TEST_URI = URI.create("http://localhost/test");
  private NettyHttpJsonRpcSender sender;
  private Jsonb jsonb;
  private Charset charset;
  private NettyHttpRequest0<Object> request0;

  @BeforeEach
  void beforeEach() {
    sender = mock(NettyHttpJsonRpcSender.class);
    jsonb = mock(Jsonb.class);
    charset = mock(Charset.class);
    request0 = new NettyHttpRequest0<>(sender, TEST_URI, jsonb, charset,
      mock(JsonRemoteProcedureSignature.class));
  }

  @Test
  void smoke() {
    assertThat(request0.charset(), equalTo(charset));
    assertThat(request0.uri(), equalTo(TEST_URI));
    assertThat(request0.jsonb(), equalTo(jsonb));
  }

  @Test
  void testCall() {
    String testId = "testId";
    request0.call(testId);
    ArgumentCaptor<RequestDto> dtoCaptor = ArgumentCaptor.forClass(RequestDto.class);
    verify(sender).send(eq(testId), dtoCaptor.capture(), eq(jsonb), eq(charset), any(), eq(TEST_URI));
    assertThat(dtoCaptor.getValue().getId(), equalTo(testId));
  }

  @Test
  void testNotificationCall() {
    request0.notificationCall();
    verify(sender).send(isA(NotificationDto.class), eq(jsonb), eq(charset), eq(TEST_URI));
  }

  @Test
  void testJsonb() {
    Jsonb newJsonb = mock(Jsonb.class);
    NettyHttpRequest0<Object> updated = request0.jsonb(newJsonb);
    assertThat(updated.jsonb(), not(this.jsonb));
    assertThat(updated.jsonb(), equalTo(newJsonb));
  }

  @Test
  void testSocketAddress() {
    URI newUri = URI.create("http://localhost-new/test");
    NettyHttpRequest0<Object> updated = request0.uri(newUri);
    assertThat(updated.uri(), not(TEST_URI));
    assertThat(updated.uri(), equalTo(newUri));
  }

  @Test
  void testCharset() {
    Charset newCharset = mock(Charset.class);
    NettyHttpRequest0<Object> updated = request0.charset(newCharset);
    assertThat(updated.charset(), equalTo(newCharset));
  }
}
