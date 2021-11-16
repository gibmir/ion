package com.github.gibmir.ion.lib.netty.client.http.request.factory;

import com.github.gibmir.ion.lib.netty.client.http.environment.TestEnvironment;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest0;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest1;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest2;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest3;
import com.github.gibmir.ion.lib.netty.client.http.request.batch.NettyHttpBatchRequest;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

class NettyHttpRequestFactoryTest {

  public NettyHttpJsonRpcSender jsonRpcSender;
  public URI uri;
  public Jsonb jsonb;
  private Charset charset;
  private NettyHttpRequestFactory factory;

  @BeforeEach
  void beforeEach() {
    jsonRpcSender = mock(NettyHttpJsonRpcSender.class);
    uri = URI.create("test-uri");
    jsonb = mock(Jsonb.class);
    charset = Charset.defaultCharset();
    factory = new NettyHttpRequestFactory(jsonRpcSender, uri, jsonb, charset);
  }

  @Test
  void testNoArg() {
    NettyHttpRequest0<String> request0 = factory.noArg(TestEnvironment.TestProcedure0.class);
    assertThat(request0.charset(), equalTo(charset));
    assertThat(request0.jsonb(), equalTo(jsonb));
    assertThat(request0.uri(), equalTo(uri));
  }


  @Test
  void testSingleArg() {
    NettyHttpRequest1<String, String> request1 = factory.singleArg(TestEnvironment.TestProcedure1.class);
    assertThat(request1.charset(), equalTo(charset));
    assertThat(request1.jsonb(), equalTo(jsonb));
    assertThat(request1.uri(), equalTo(uri));
  }

  @Test
  void testTwoArg() {
    NettyHttpRequest2<String, String, String> request2 = factory.twoArg(TestEnvironment.TestProcedure2.class);
    assertThat(request2.charset(), equalTo(charset));
    assertThat(request2.jsonb(), equalTo(jsonb));
    assertThat(request2.uri(), equalTo(uri));
  }

  @Test
  void testThreeArg() {
    NettyHttpRequest3<String, String, String, String> request3 = factory.threeArg(TestEnvironment.TestProcedure3.class);
    assertThat(request3.charset(), equalTo(charset));
    assertThat(request3.jsonb(), equalTo(jsonb));
    assertThat(request3.uri(), equalTo(uri));
  }

  @Test
  void testBatch() {
    NettyHttpBatchRequest batchRequest = factory.batch().build();

    assertThat(batchRequest.charset(), equalTo(charset));
    assertThat(batchRequest.jsonb(), equalTo(jsonb));
    assertThat(batchRequest.uri(), equalTo(uri));
  }
}
