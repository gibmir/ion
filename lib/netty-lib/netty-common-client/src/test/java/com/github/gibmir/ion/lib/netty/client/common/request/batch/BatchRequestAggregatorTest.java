package com.github.gibmir.ion.lib.netty.client.common.request.batch;

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

class BatchRequestAggregatorTest {

  private BatchRequestAggregator batchRequestAggregator;

  @BeforeEach
  void beforeEach() {
    batchRequestAggregator = new BatchRequestAggregator();
  }

  @Test
  void testAdd() {
    batchRequestAggregator.addRequest(TEST_ID, TestProcedure0.class);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(1));
    assertThat(awaitBatchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  void testAddPositionalWithOneArg() {
    batchRequestAggregator.addPositionalRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(1));
    assertThat(awaitBatchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  void testAddPositionalWithTwoArg() {
    batchRequestAggregator.addPositionalRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(1));
    assertThat(awaitBatchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  void testAddPositionalWithThreeArg() {
    batchRequestAggregator.addPositionalRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(1));
    assertThat(awaitBatchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  void testAddNamedWithOneArg() {
    batchRequestAggregator.addNamedRequest(TEST_ID, TestProcedure1.class, TEST_FIRST_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(1));
    assertThat(awaitBatchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  void testAddNamedWithTwoArg() {
    batchRequestAggregator.addNamedRequest(TEST_ID, TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(1));
    assertThat(awaitBatchParts, hasItem(allOf(
      awaitBatchPartWithId(TEST_ID),
      awaitBatchPartWithReturnType(String.class)
    )));
  }

  @Test
  void testAddNamedWithThreeArg() {
    batchRequestAggregator.addNamedRequest(TEST_ID, TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(RequestDto.class)));
    //check await parts
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(1));
    assertThat(awaitBatchParts, hasItem(allOf(
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
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(0));
  }

  @Test
  void testAddPositionalNotificationWithTwoArg() {
    batchRequestAggregator.addPositionalNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(NotificationDto.class)));
    //check await parts
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(0));
  }

  @Test
  void testAddPositionalNotificationWithThreeArg() {
    batchRequestAggregator.addPositionalNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(NotificationDto.class)));
    //check await parts
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(0));
  }

  @Test
  void testAddNamedNotificationWithOneArg() {
    batchRequestAggregator.addNamedNotification(TestProcedure1.class, TEST_FIRST_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(NotificationDto.class)));
    //check await parts
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(0));
  }

  @Test
  void testAddNamedNotificationWithTwoArg() {
    batchRequestAggregator.addNamedNotification(TestProcedure2.class, TEST_FIRST_ARG, TEST_SECOND_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(NotificationDto.class)));
    //check await parts
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(0));
  }

  @Test
  void testAddNamedNotificationWithThreeArg() {
    batchRequestAggregator.addPositionalNotification(TestProcedure3.class, TEST_FIRST_ARG, TEST_SECOND_ARG, TEST_THIRD_ARG);
    //check requests
    List<JsonRpcRequest> requests = batchRequestAggregator.getRequests();
    assertThat(requests, hasSize(1));
    assertThat(requests, hasItem(instanceOf(NotificationDto.class)));
    //check await parts
    List<NettyBatch.AwaitBatchPart> awaitBatchParts = batchRequestAggregator.getAwaitBatchParts();
    assertThat(awaitBatchParts, hasSize(0));
  }
}
