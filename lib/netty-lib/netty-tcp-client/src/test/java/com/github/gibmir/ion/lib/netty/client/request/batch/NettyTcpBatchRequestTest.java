package com.github.gibmir.ion.lib.netty.client.request.batch;

import com.github.gibmir.ion.lib.netty.client.common.request.batch.BatchRequestAggregator;
import com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TestProcedure2;
import com.github.gibmir.ion.lib.netty.client.sender.NettyTcpJsonRpcSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;

import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_CHARSET;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_FIRST_ARG;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_ID;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_SECOND_ARG;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_SOCKET_ADDRESS;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TEST_THIRD_ARG;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TestProcedure0;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TestProcedure1;
import static com.github.gibmir.ion.lib.netty.client.environment.TestEnvironment.TestProcedure3;
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
    builder = NettyTcpBatchRequest.builder(batchRequestAggregator, nettyTcpJsonRpcSender, TEST_SOCKET_ADDRESS, jsonb,
      TEST_CHARSET);
  }


  @Test
  void testCreateWithNoArgPositionalRequest() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addRequest(TEST_ID, TestProcedure0.class).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addRequest(TEST_ID, TestProcedure0.class);
  }

  @Test
  void testCreateWithNoArgPositionalNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNotification(TestProcedure0.class).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNotification(TestProcedure0.class);
  }

  @Test
  void testCreateWithOneArgPositionalRequest() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG);
  }

  @Test
  void testCreateWithOneArgPositionalNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalNotification(TestProcedure1.class, TEST_FIRST_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalNotification(TestProcedure1.class, TEST_FIRST_ARG);
  }

  @Test
  void testCreateWithOneArgNamedRequest() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNamedRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG);
  }

  @Test
  void testCreateWithOneArgNamedNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNamedNotification(TestProcedure1.class, TEST_FIRST_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedNotification(TestProcedure1.class, TEST_FIRST_ARG);
  }

  @Test
  void testCreateWithTwoArgPositionalRequest() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
  }

  @Test
  void testCreateWithTwoArgPositionalNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
  }

  @Test
  void testCreateWithTwoArgNamedRequest() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNamedRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
  }

  @Test
  void testCreateWithTwoArgNamedNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNamedNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
  }

  @Test
  void testCreateWithThreeArgPositionalRequest() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
  }

  @Test
  void testCreateWithThreeArgPositionalNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
  }

  @Test
  void testCreateWithThreeArgNamedRequest() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNamedRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
  }

  @Test
  void testCreateWithThreeArgNamedNotification() {
    NettyTcpBatchRequest nettyHttpBatchRequest = builder
      .addNamedNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
  }
}
