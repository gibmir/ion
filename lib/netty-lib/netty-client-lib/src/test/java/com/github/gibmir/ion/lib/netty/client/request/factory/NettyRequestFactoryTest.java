package com.github.gibmir.ion.lib.netty.client.request.factory;

import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gibmir.ion.api.client.request.factory.RequestFactory;
import com.github.gibmir.ion.api.client.request.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.lib.netty.client.request.NettyRequest1;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.sender.pool.ChannelPool;
import com.github.gibmir.ion.lib.netty.common.configuration.group.TestProcedure;
import com.github.gibmir.ion.lib.netty.common.configuration.group.dto.RequestDto;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

class NettyRequestFactoryTest {

  @Test
  void smoke() throws ExecutionException, InterruptedException {
    EventLoopGroup eventExecutors = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
    ChannelPool channelPool = new ChannelPool(new ConcurrentHashMap<>(), eventExecutors, NioSocketChannel.class,
      LogLevel.INFO, new ResponseListenerRegistry(new ConcurrentHashMap<>()));
    ResponseListenerRegistry responseListenerRegistry = new ResponseListenerRegistry(new ConcurrentHashMap<>());
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(channelPool, responseListenerRegistry);

    Jsonb jsonb = JsonbBuilder.create();
    NettyRequestFactory nettyRequestFactory = new NettyRequestFactory(jsonRpcNettySender,
      InetSocketAddress.createUnresolved("localhost", 52222), jsonb, StandardCharsets.UTF_8);

    NettyRequest1<RequestDto, RequestDto> request = nettyRequestFactory.singleArg(TestProcedure.class, RequestDto.class);
    System.out.println(request.positionalCall("someId", new RequestDto("some arg"))
      .thenApply(requestDto -> request.positionalCall("second",
        new RequestDto("second" + requestDto.getRequestString())))
      .whenComplete((requestDtoCompletableFuture, throwable) -> System.out.println(requestDtoCompletableFuture + ":" + throwable))
      .get());
  }

  @Test
  void demoProvider() {
    RequestFactory requestFactory = RequestFactoryProvider.load().provide();
    Request1<RequestDto, RequestDto> testProcedure = requestFactory.singleArg(TestProcedure.class, RequestDto.class);
    testProcedure.positionalCall("123213-321323-434", new RequestDto("argument"));

  }
}
