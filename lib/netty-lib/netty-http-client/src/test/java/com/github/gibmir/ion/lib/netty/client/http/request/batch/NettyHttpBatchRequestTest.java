package com.github.gibmir.ion.lib.netty.client.http.request.batch;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.BatchRequestAggregator;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.NettyBatch;
import com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TestProcedure0;
import com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TestProcedure1;
import com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TestProcedure2;
import com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TestProcedure3;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;

import java.net.URI;
import java.nio.charset.Charset;

import static com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TEST_CHARSET;
import static com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TEST_FIRST_ARG;
import static com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TEST_ID;
import static com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TEST_SECOND_ARG;
import static com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TEST_THIRD_ARG;
import static com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment.TEST_URI;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NettyHttpBatchRequestTest {

  private BatchRequestAggregator batchRequestAggregator;
  private Jsonb jsonb;
  private NettyHttpBatchRequest.Builder builder;
  private NettyHttpJsonRpcSender nettyHttpJsonRpcSender;

  @BeforeEach
  void beforeEach() {
    batchRequestAggregator = mock(BatchRequestAggregator.class);
    nettyHttpJsonRpcSender = mock(NettyHttpJsonRpcSender.class);
    jsonb = mock(Jsonb.class);
    builder = NettyHttpBatchRequest.builder(batchRequestAggregator,
      nettyHttpJsonRpcSender, TEST_URI, jsonb, TEST_CHARSET);
  }

  @Test
  void smoke() {
    NettyHttpBatchRequest request = builder.build();
    assertThat(request.uri(), is(TEST_URI));
    assertThat(request.charset(), is(TEST_CHARSET));
    assertThat(request.jsonb(), is(jsonb));
  }

  @Test
  void testUri() {
    NettyHttpBatchRequest request = builder.build();
    URI updatedURI = URI.create("http://localhost/test2");
    NettyHttpBatchRequest updatedRequest = request.uri(updatedURI);
    assertThat(updatedRequest.uri(), is(updatedURI));
    assertThat(request.uri(), is(TEST_URI));
  }

  @Test
  void testCharset() {
    NettyHttpBatchRequest request = builder.build();
    Charset updatedCharset = mock(Charset.class);
    NettyHttpBatchRequest updatedRequest = request.charset(updatedCharset);
    assertThat(updatedRequest.charset(), is(updatedCharset));
    assertThat(request.charset(), is(TEST_CHARSET));
  }

  @Test
  void testJsonb() {
    NettyHttpBatchRequest request = builder.build();
    Jsonb updatedJsonb = mock(Jsonb.class);
    NettyHttpBatchRequest updatedRequest = request.jsonb(updatedJsonb);
    assertThat(updatedRequest.jsonb(), is(updatedJsonb));
    assertThat(request.jsonb(), is(jsonb));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithNoArgPositionalRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addRequest(TEST_ID, TestProcedure0.class, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addRequest(TEST_ID, TestProcedure0.class, responseCallback);
  }

  @Test
  void testCreateWithNoArgPositionalNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNotification(TestProcedure0.class).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addNotification(TestProcedure0.class);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithOneArgPositionalRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addPositionalRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG, responseCallback);
  }

  @Test
  void testCreateWithOneArgPositionalNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalNotification(TestProcedure1.class, TEST_FIRST_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addPositionalNotification(TestProcedure1.class, TEST_FIRST_ARG);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithOneArgNamedRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNamedRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addNamedRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG, responseCallback);
  }

  @Test
  void testCreateWithOneArgNamedNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNamedNotification(TestProcedure1.class, TEST_FIRST_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addNamedNotification(TestProcedure1.class, TEST_FIRST_ARG);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithTwoArgPositionalRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addPositionalRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG, responseCallback);
  }

  @Test
  void testCreateWithTwoArgPositionalNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addPositionalNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithTwoArgNamedRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNamedRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addNamedRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG, responseCallback);
  }

  @Test
  void testCreateWithTwoArgNamedNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNamedNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addNamedNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithThreeArgPositionalRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addPositionalRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG, responseCallback);
  }

  @Test
  void testCreateWithThreeArgPositionalNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addPositionalNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addPositionalNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
  }

  @Test
  @SuppressWarnings("unchecked")
  void testCreateWithThreeArgNamedRequest() {
    ResponseCallback<String> responseCallback = mock(ResponseCallback.class);
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNamedRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG, responseCallback).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addNamedRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG, responseCallback);
  }

  @Test
  void testCreateWithThreeArgNamedNotification() {
    NettyHttpBatchRequest nettyHttpBatchRequest = builder
      .addNamedNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG).build();
    assertThat(nettyHttpBatchRequest.charset(), equalTo(TEST_CHARSET));
    assertThat(nettyHttpBatchRequest.jsonb(), equalTo(jsonb));
    verify(batchRequestAggregator)
      .addNamedNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
  }

  @Test
  void testCall() {
    builder.build().call();
    verify(nettyHttpJsonRpcSender).send(any(NettyBatch.class), eq(jsonb), eq(TEST_CHARSET), eq(TEST_URI));
  }
}
