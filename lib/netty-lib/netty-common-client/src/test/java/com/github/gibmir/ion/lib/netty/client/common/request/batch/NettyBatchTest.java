package com.github.gibmir.ion.lib.netty.client.common.request.batch;

import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

class NettyBatchTest {

  @Test
  void smoke() {
    List<JsonRpcRequest> requestDto = Collections.emptyList();
    List<NettyBatch.BatchPart<?>> batchParts = Collections.emptyList();
    NettyBatch nettyBatch = new NettyBatch(requestDto, batchParts);

    assertThat(nettyBatch.getBatchRequestDto(), is(requestDto));
    assertThat(nettyBatch.getBatchParts(), is(batchParts));
  }

  @Test
  void smokePart() {
    String testId = "testId";
    ResponseCallback<?> responseCallback = mock(ResponseCallback.class);
    NettyBatch.BatchPart<?> batchPart = new NettyBatch.BatchPart<>(testId, responseCallback, String.class);

    assertThat(batchPart.getId(), is(testId));
    assertThat(batchPart.getResponseCallback(), is(responseCallback));
    assertThat(batchPart.getReturnType(), is(String.class));
  }
}
