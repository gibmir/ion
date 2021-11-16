package com.github.gibmir.ion.lib.netty.client.tcp.request.factory;

import com.github.gibmir.ion.lib.netty.client.tcp.environment.TestEnvironment;
import com.github.gibmir.ion.lib.netty.client.tcp.request.NettyTcpRequest0;
import com.github.gibmir.ion.lib.netty.client.tcp.request.NettyTcpRequest1;
import com.github.gibmir.ion.lib.netty.client.tcp.request.NettyTcpRequest2;
import com.github.gibmir.ion.lib.netty.client.tcp.request.NettyTcpRequest3;
import com.github.gibmir.ion.lib.netty.client.tcp.request.batch.NettyTcpBatchRequest;
import com.github.gibmir.ion.lib.netty.client.tcp.sender.NettyTcpJsonRpcSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class NettyTcpRequestFactoryTest {
  public Logger logger = mock(Logger.class);
  public NettyTcpJsonRpcSender jsonRpcSender;
  public SocketAddress socketAddress;
  public Jsonb jsonb;
  private Charset charset;
  private NettyTcpRequestFactory factory;

  @BeforeEach
  void beforeEach() {
    jsonRpcSender = mock(NettyTcpJsonRpcSender.class);
    socketAddress = mock(SocketAddress.class);
    jsonb = mock(Jsonb.class);
    charset = Charset.defaultCharset();
    factory = new NettyTcpRequestFactory(jsonRpcSender, socketAddress, jsonb, charset, logger);
  }

  @Test
  void testNoArg() {
    NettyTcpRequest0<String> request0 = factory.noArg(TestEnvironment.TestProcedure0.class);
    assertThat(request0.charset(), equalTo(charset));
    assertThat(request0.jsonb(), equalTo(jsonb));
    assertThat(request0.socketAddress(), equalTo(socketAddress));
  }


  @Test
  void testSingleArg() {
    NettyTcpRequest1<String, String> request1 = factory.singleArg(TestEnvironment.TestProcedure1.class);
    assertThat(request1.charset(), equalTo(charset));
    assertThat(request1.jsonb(), equalTo(jsonb));
    assertThat(request1.socketAddress(), equalTo(socketAddress));
  }

  @Test
  void testTwoArg() {
    NettyTcpRequest2<String, String, String> request2 = factory.twoArg(TestEnvironment.TestProcedure2.class);
    assertThat(request2.charset(), equalTo(charset));
    assertThat(request2.jsonb(), equalTo(jsonb));
    assertThat(request2.socketAddress(), equalTo(socketAddress));
  }

  @Test
  void testThreeArg() {
    NettyTcpRequest3<String, String, String, String> request3 = factory.threeArg(TestEnvironment.TestProcedure3.class);
    assertThat(request3.charset(), equalTo(charset));
    assertThat(request3.jsonb(), equalTo(jsonb));
    assertThat(request3.socketAddress(), equalTo(socketAddress));
  }

  @Test
  void testBatch() {
    NettyTcpBatchRequest batchRequest = factory.batch().build();

    assertThat(batchRequest.charset(), equalTo(charset));
    assertThat(batchRequest.jsonb(), equalTo(jsonb));
    assertThat(batchRequest.socketAddress(), equalTo(socketAddress));
  }
}
