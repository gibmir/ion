package com.github.gibmir.ion.lib.netty.client.request.factory;

import com.github.gibmir.ion.api.client.request.Request1;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;
import com.github.gibmir.ion.lib.netty.common.configuration.group.TestApi;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
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
    JsonRpcNettySender jsonRpcNettySender = new JsonRpcNettySender(NioSocketChannel.class, eventExecutors);

    Jsonb jsonb = JsonbBuilder.create();
    NettyRequestFactory nettyRequestFactory = new NettyRequestFactory(jsonRpcNettySender,
      InetSocketAddress.createUnresolved("localhost", 52222), jsonb, StandardCharsets.UTF_8);

    Request1<String, String> request = nettyRequestFactory.singleArg(TestApi.class, String.class);
    String result = request.positionalCall("someid", "some arg").get();
    System.out.println(result);
    System.out.println(request.positionalCall("second", result + "secong").get());
  }
}
