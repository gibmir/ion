package com.github.gibmir.ion.lib.netty.client.request.factory;

import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gibmir.ion.api.client.request.factory.RequestFactory;
import com.github.gibmir.ion.api.client.request.factory.provider.RequestFactoryProvider;
import com.github.gibmir.ion.lib.netty.client.request.NettyRequest1;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;
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
import java.util.concurrent.ExecutionException;

class NettyRequestFactoryTest {

  @Test
  void smoke() throws ExecutionException, InterruptedException {
    EventLoopGroup eventExecutors = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(LogLevel.TRACE, NioSocketChannel.class, eventExecutors);

    Jsonb jsonb = JsonbBuilder.create();
    NettyRequestFactory nettyRequestFactory = new NettyRequestFactory(jsonRpcNettySender,
      InetSocketAddress.createUnresolved("localhost", 52222), jsonb, StandardCharsets.UTF_8);

    NettyRequest1<RequestDto, RequestDto> request = nettyRequestFactory.singleArg(TestProcedure.class, RequestDto.class);
    RequestDto result = request.positionalCall("someid", new RequestDto("some arg")).get();
    System.out.println(result);
    System.out.println(request.positionalCall("second", new RequestDto("second" + result)).get());
  }

  @Test
  void demoProvider() {
    RequestFactory requestFactory = RequestFactoryProvider.load().provide();
    Request1<RequestDto, RequestDto> testProcedure = requestFactory.singleArg(TestProcedure.class, RequestDto.class);
    testProcedure.positionalCall("123213-321323-434", new RequestDto("argument"));

  }
}
