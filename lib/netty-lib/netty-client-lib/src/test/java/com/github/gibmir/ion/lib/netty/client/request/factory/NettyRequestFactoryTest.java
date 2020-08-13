package com.github.gibmir.ion.lib.netty.client.request.factory;

import com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry.SimpleResponseListenerRegistry;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

class NettyRequestFactoryTest {

  @Test
  void smoke() {
    EventLoopGroup eventExecutors = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
    ResponseListenerRegistry responseListenerRegistry = new SimpleResponseListenerRegistry(new ConcurrentHashMap<>());

//    NettyRequest1<RequestDto, RequestDto> request = nettyRequestFactory.singleArg(TestProcedure.class);
//    System.out.println(request.positionalCall("someId", new RequestDto("some arg"))
//      .get());
  }

  @Test
  void demoProvider() {
//    Request1<RequestDto, RequestDto> testProcedure = requestFactory.singleArg(TestProcedure.class);
//    testProcedure.positionalCall("123213-321323-434", new RequestDto("argument"));

  }
}
