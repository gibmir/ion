package com.github.gibmir.ion.lib.netty.client.http.request.batch;

import com.github.gibmir.ion.lib.netty.client.common.request.batch.BatchRequestAggregator;
import com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TestProcedure0;
import com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TestProcedure1;
import com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TestProcedure2;
import com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TestProcedure3;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;

import static com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TEST_CHARSET;
import static com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TEST_FIRST_ARG;
import static com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TEST_ID;
import static com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TEST_SECOND_ARG;
import static com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TEST_THIRD_ARG;
import static com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TEST_URI;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NettyHttpBatchRequestTest {

  private BatchRequestAggregator batchRequestAggregator;
  private NettyHttpJsonRpcSender nettyHttpJsonRpcSender;
  private Jsonb jsonb;
  private NettyHttpBatchRequest.Builder builder;

  @BeforeEach
  void beforeEach() {
    batchRequestAggregator = mock(BatchRequestAggregator.class);
    nettyHttpJsonRpcSender = mock(NettyHttpJsonRpcSender.class);
    jsonb = mock(Jsonb.class);
    builder = NettyHttpBatchRequest.builder(batchRequestAggregator,
      nettyHttpJsonRpcSender, TEST_URI, jsonb, TEST_CHARSET);
  }

  @Test
  void testCreateWithNoArgPositionalRequest() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addRequest(TEST_ID, TestProcedure0.class).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addRequest(TEST_ID, TestProcedure0.class);
  }

  @Test
  void testCreateWithNoArgPositionalNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNotification(TestProcedure0.class).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNotification(TestProcedure0.class);
  }

  @Test
  void testCreateWithOneArgPositionalRequest() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG);
  }

  @Test
  void testCreateWithOneArgPositionalNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalNotification(TestProcedure1.class, TEST_FIRST_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalNotification(TestProcedure1.class, TEST_FIRST_ARG);
  }

  @Test
  void testCreateWithOneArgNamedRequest() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNamedRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG);
  }

  @Test
  void testCreateWithOneArgNamedNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNamedNotification(TestProcedure1.class, TEST_FIRST_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedNotification(TestProcedure1.class, TEST_FIRST_ARG);
  }

  @Test
  void testCreateWithTwoArgPositionalRequest() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
  }

  @Test
  void testCreateWithTwoArgPositionalNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
  }

  @Test
  void testCreateWithTwoArgNamedRequest() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNamedRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
  }

  @Test
  void testCreateWithTwoArgNamedNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNamedNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
  }

  @Test
  void testCreateWithThreeArgPositionalRequest() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
  }

  @Test
  void testCreateWithThreeArgPositionalNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addPositionalNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
  }

  @Test
  void testCreateWithThreeArgNamedRequest() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNamedRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
  }

  @Test
  void testCreateWithThreeArgNamedNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNamedNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator, atMostOnce())
      .addNamedNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
  }
}
