package com.github.gibmir.ion.lib.netty.client.http.request;

import com.github.gibmir.ion.api.dto.AbstractJsonRpcBody;
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
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NettyHttpRequest2Test {
  public static final URI TEST_URI = URI.create("http://localhost/test");
  private NettyHttpJsonRpcSender sender;
  private Jsonb jsonb;
  private Charset charset;
  private NettyHttpRequest2<Object, Object, ?> request2;
  private JsonRemoteProcedureSignature signature;

  @BeforeEach
  void beforeEach() {
    sender = mock(NettyHttpJsonRpcSender.class);
    jsonb = mock(Jsonb.class);
    charset = mock(Charset.class);
    signature = mock(JsonRemoteProcedureSignature.class);
    request2 = new NettyHttpRequest2<>(sender, TEST_URI, jsonb, charset, signature);
  }

  @Test
  void smoke() {
    assertThat(request2.charset(), equalTo(charset));
    assertThat(request2.uri(), equalTo(TEST_URI));
    assertThat(request2.jsonb(), equalTo(jsonb));
  }

  @Test
  void testPositionalCall() {
    String testId = "testId";
    request2.positionalCall(testId, "testObject", "testObject2");
    verify(signature).getReturnType();
    ArgumentCaptor<RequestDto> requestCaptor = ArgumentCaptor.forClass(RequestDto.class);
    verify(sender).send(eq(testId), requestCaptor.capture(), eq(jsonb), eq(charset), any(), eq(TEST_URI));
    RequestDto request = requestCaptor.getValue();
    assertThat(request.getId(), equalTo(testId));
    assertThat(request.getJsonRpcProtocolVersion(), equalTo(AbstractJsonRpcBody.JSON_RPC_PROTOCOL_VERSION));
    assertThat(request.getArgs(), instanceOf(Object[].class));
  }

  @Test
  void testPositionalNotificationCall() {
    request2.positionalNotificationCall("testObject", "testObject2");
    ArgumentCaptor<NotificationDto> requestCaptor = ArgumentCaptor.forClass(NotificationDto.class);
    verify(sender).send(requestCaptor.capture(), eq(jsonb), eq(charset), eq(TEST_URI));
    NotificationDto request = requestCaptor.getValue();
    assertThat(request.getJsonRpcProtocolVersion(), equalTo(AbstractJsonRpcBody.JSON_RPC_PROTOCOL_VERSION));
    assertThat(request.getArgs(), instanceOf(Object[].class));
  }

  @Test
  void testNamedCall() {
    doAnswer(__ -> new String[]{"", ""}).when(signature).getParameterNames();
    String testId = "testId";
    request2.namedCall(testId, "testObject", "testObject2");
    verify(signature).getReturnType();
    ArgumentCaptor<RequestDto> requestCaptor = ArgumentCaptor.forClass(RequestDto.class);
    verify(sender).send(eq(testId), requestCaptor.capture(), eq(jsonb), eq(charset), any(), eq(TEST_URI));
    RequestDto request = requestCaptor.getValue();
    assertThat(request.getId(), equalTo(testId));
    assertThat(request.getJsonRpcProtocolVersion(), equalTo(AbstractJsonRpcBody.JSON_RPC_PROTOCOL_VERSION));
    assertThat(request.getArgs(), instanceOf(Map.class));
  }

  @Test
  void testNamedNotificationCall() {
    doAnswer(__ -> new String[]{"", ""}).when(signature).getParameterNames();
    request2.namedNotificationCall("testObject", "testObject2");
    ArgumentCaptor<NotificationDto> requestCaptor = ArgumentCaptor.forClass(NotificationDto.class);
    verify(sender).send(requestCaptor.capture(), eq(jsonb), eq(charset), eq(TEST_URI));
    NotificationDto request = requestCaptor.getValue();
    assertThat(request.getJsonRpcProtocolVersion(), equalTo(AbstractJsonRpcBody.JSON_RPC_PROTOCOL_VERSION));
    assertThat(request.getArgs(), instanceOf(Map.class));
  }

  @Test
  void testJsonb() {
    Jsonb newJsonb = mock(Jsonb.class);
    NettyHttpRequest2<?, ?, ?> updated = request2.jsonb(newJsonb);
    assertThat(updated.jsonb(), not(this.jsonb));
    assertThat(updated.jsonb(), equalTo(newJsonb));
  }

  @Test
  void testSocketAddress() {
    URI newUri = URI.create("http://localhost-new/test");
    NettyHttpRequest2<?, ?, ?> updated = request2.uri(newUri);
    assertThat(updated.uri(), not(TEST_URI));
    assertThat(updated.uri(), equalTo(newUri));
  }

  @Test
  void testCharset() {
    Charset newCharset = mock(Charset.class);
    NettyHttpRequest2<?, ?, ?> updated = request2.charset(newCharset);
    assertThat(updated.charset(), equalTo(newCharset));
  }
}
