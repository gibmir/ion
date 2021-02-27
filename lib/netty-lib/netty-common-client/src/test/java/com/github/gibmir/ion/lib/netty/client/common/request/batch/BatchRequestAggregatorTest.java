package com.github.gibmir.ion.lib.netty.client.common.request.batch;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.common.environment.TestEnvironment.TestProcedure0;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.github.gibmir.ion.lib.netty.client.common.environment.TestEnvironment.AwaitBatchPartWithId.awaitBatchPartWithId;
import static com.github.gibmir.ion.lib.netty.client.common.environment.TestEnvironment.AwaitBatchPartWithReturnType.awaitBatchPartWithReturnType;
import static com.github.gibmir.ion.lib.netty.client.common.environment.TestEnvironment.TEST_FIRST_ARG;
import static com.github.gibmir.ion.lib.netty.client.common.environment.TestEnvironment.TEST_ID;
import static com.github.gibmir.ion.lib.netty.client.common.environment.TestEnvironment.TEST_SECOND_ARG;
import static com.github.gibmir.ion.lib.netty.client.common.environment.TestEnvironment.TEST_THIRD_ARG;
import static com.github.gibmir.ion.lib.netty.client.common.environment.TestEnvironment.TestProcedure1;
import static com.github.gibmir.ion.lib.netty.client.common.environment.TestEnvironment.TestProcedure2;
import static com.github.gibmir.ion.lib.netty.client.common.environment.TestEnvironment.TestProcedure3;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.mockito.Mockito.mock;

class BatchRequestAggregatorTest {

  private BatchRequestAggregator batchRequestAggregator;

  @BeforeEach
  void beforeEach() {
    batchRequestAggregator = new BatchRequestAggregator();
  }

  @Test
  @SuppressWarnings("unchecked")
  void testAdd() {
    batchRequestAggregator.addRequest(TEST_ID, TestProcedure0.class, mock(ResponseCallback.class));
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(1));
    assertThat(batchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testAddPositionalWithOneArg() {
    batchRequestAggregator.addPositionalRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG,
      mock(ResponseCallback.class));
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(1));
    assertThat(batchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testAddPositionalWithTwoArg() {
    batchRequestAggregator.addPositionalRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG,
      mock(ResponseCallback.class));
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(1));
    assertThat(batchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testAddPositionalWithThreeArg() {
    batchRequestAggregator.addPositionalRequest(TEST_ID, TestProcedure3.class,
      TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG, mock(ResponseCallback.class));
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(1));
    assertThat(batchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testAddNamedWithOneArg() {
    batchRequestAggregator.addNamedRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG, mock(ResponseCallback.class));
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(1));
    assertThat(batchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testAddNamedWithTwoArg() {
    batchRequestAggregator.addNamedRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG,
      mock(ResponseCallback.class));
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(1));
    assertThat(batchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  @SuppressWarnings("unchecked")
  void testAddNamedWithThreeArg() {
    batchRequestAggregator.addNamedRequest(TEST_ID, TestProcedure3.class,
      TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG, mock(ResponseCallback.class));
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(1));
    assertThat(batchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  void testAddPositionalNotificationWithOneArg() {
    batchRequestAggregator.addPositionalNotification(TestProcedure1.class, TEST_FIRST_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(NotificationDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(0));
  }

  @Test
  void testAddPositionalNotificationWithTwoArg() {
    batchRequestAggregator.addPositionalNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(NotificationDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(0));
  }

  @Test
  void testAddPositionalNotificationWithThreeArg() {
    batchRequestAggregator.addPositionalNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(NotificationDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(0));
  }

  @Test
  void testAddNamedNotificationWithOneArg() {
    batchRequestAggregator.addNamedNotification(TestProcedure1.class, TEST_FIRST_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(NotificationDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(0));
  }

  @Test
  void testAddNamedNotificationWithTwoArg() {
    batchRequestAggregator.addNamedNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(NotificationDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(0));
  }

  @Test
  void testAddNamedNotificationWithThreeArg() {
    batchRequestAggregator.addPositionalNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(NotificationDto.class)));
    //check await parts
    List<NettyBatch.BatchPart<?>> batchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(batchParts, hasSize(0));
  }
}
