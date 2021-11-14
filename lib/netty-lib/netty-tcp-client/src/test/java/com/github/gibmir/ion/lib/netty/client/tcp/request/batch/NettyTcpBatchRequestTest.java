package com.github.gibmir.ion.lib.netty.client.tcp.request.batch;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.BatchRequestAggregator;
import com.github.gibmir.ion.lib.netty.client.tcp.environment.TestEnvironment;
import com.github.gibmir.ion.lib.netty.client.tcp.request.batch.NettyTcpBatchRequest;
import com.github.gibmir.ion.lib.netty.client.tcp.sender.NettyTcpJsonRpcSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NettyTcpBatchRequestTest {
  private BatchRequestAggregator batchRequestAggregator;
  private Jsonb jsonb;
  private NettyTcpBatchRequest.Builder builder;

  @BeforeEach
  void beforeEach() {
    batchRequestAggregator = mock(BatchRequestAggregator.class);
    NettyTcpJsonRpcSender nettyTcpJsonRpcSender = mock(NettyTcpJsonRpcSender.class);
    jsonb = mock(Jsonb.class);
    builder = NettyTcpBatchRequest.builder(batchRequestAggregator, nettyTcpJsonRpcSender, TestEnvironment.TEST_SOCKET_ADDRESS, jsonb,
      TestEnvironment.TEST_CHARSET);
  }


  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithNoArgPositionalRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure0.class, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure0.class, responseCallback);
  }

  @Test
  void testCreateWithNoArgPositionalNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNotification(TestEnvironment.TestProcedure0.class).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNotification(TestEnvironment.TestProcedure0.class);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithOneArgPositionalRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure1.class, TestEnvironment.TEST_FIRST_ARG, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure1.class, TestEnvironment.TEST_FIRST_ARG, responseCallback);
  }

  @Test
  void testCreateWithOneArgPositionalNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalNotification(TestEnvironment.TestProcedure1.class, TestEnvironment.TEST_FIRST_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalNotification(TestEnvironment.TestProcedure1.class, TestEnvironment.TEST_FIRST_ARG);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithOneArgNamedRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNamedRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure1.class, TestEnvironment.TEST_FIRST_ARG, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure1.class, TestEnvironment.TEST_FIRST_ARG, responseCallback);
  }

  @Test
  void testCreateWithOneArgNamedNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNamedNotification(TestEnvironment.TestProcedure1.class, TestEnvironment.TEST_FIRST_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedNotification(TestEnvironment.TestProcedure1.class, TestEnvironment.TEST_FIRST_ARG);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithTwoArgPositionalRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure2.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure2.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG, responseCallback);
  }

  @Test
  void testCreateWithTwoArgPositionalNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalNotification(TestEnvironment.TestProcedure2.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalNotification(TestEnvironment.TestProcedure2.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithTwoArgNamedRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNamedRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure2.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure2.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG, responseCallback);
  }

  @Test
  void testCreateWithTwoArgNamedNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNamedNotification(TestEnvironment.TestProcedure2.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedNotification(TestEnvironment.TestProcedure2.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithThreeArgPositionalRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure3.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG, TestEnvironment.TEST_THIRD_ARG, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure3.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG, TestEnvironment.TEST_THIRD_ARG,
        responseCallback);
  }

  @Test
  void testCreateWithThreeArgPositionalNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalNotification(TestEnvironment.TestProcedure3.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG, TestEnvironment.TEST_THIRD_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalNotification(TestEnvironment.TestProcedure3.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG, TestEnvironment.TEST_THIRD_ARG);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithThreeArgNamedRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNamedRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure3.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG, TestEnvironment.TEST_THIRD_ARG, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedRequest(TestEnvironment.TEST_ID, TestEnvironment.TestProcedure3.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG, TestEnvironment.TEST_THIRD_ARG, responseCallback);
  }

  @Test
  void testCreateWithThreeArgNamedNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNamedNotification(TestEnvironment.TestProcedure3.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG, TestEnvironment.TEST_THIRD_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TestEnvironment.TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedNotification(TestEnvironment.TestProcedure3.class, TestEnvironment.TEST_FIRST_ARG, TestEnvironment.TEST_SECOND_ARG, TestEnvironment.TEST_THIRD_ARG);
  }
}
