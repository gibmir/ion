package com.github.gibmir.ion.lib.nio.client.sender;

import com.github.gibmir.ion.lib.nio.common.pool.ByteBufferPool;
import com.github.gibmir.ion.lib.nio.common.pool.reader.ByteBufferReader;
import com.github.gibmir.ion.lib.nio.common.pool.writer.ByteBufferWriter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class NioJsonRpcRequestSenderTest {
  private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);
  public static final InetSocketAddress SOCKET_ADDRESS = new InetSocketAddress(55_370);

  @Test
  void smoke() throws IOException, InterruptedException {
    ByteBufferPool byteBufferPool = new ByteBufferPool(10, 20);
    ByteBufferWriter byteBufferWriter = new ByteBufferWriter(byteBufferPool);
    ByteBufferReader byteBufferReader = new ByteBufferReader(byteBufferPool);
//    Processor processor = new Processor();
//    NioServer nioServer = NioServer.create(SOCKET_ADDRESS, processor, byteBufferReader, byteBufferWriter);
//    EXECUTOR_SERVICE.submit(nioServer);
//
//    IonClientSelector selector = IonClientSelector.open(new LinkedBlockingDeque<>(), byteBufferReader, byteBufferWriter,
//      new ConcurrentHashMap<>());
//    SocketChannel channel = SocketChannel.open(SOCKET_ADDRESS);
//    selector.register(channel);
//    ChannelCache channelCache = new ChannelCache(Map.of("method", List.of(channel)));
//    ChannelResolver channelResolver = new ChannelResolver(channelCache);
//    JsonRpcRequestSender jsonRpcRequestSender = new NioJsonRpcRequestSender(selector, channelResolver);
//
//
//    EXECUTOR_SERVICE.submit(selector);
//    RequestContext requestContext = RequestContext.defaultContext("method", "asd".getBytes(), "some id");
//    jsonRpcRequestSender.sendAsync(requestContext,
//      (responsePayload, exception) -> System.out.println(Arrays.toString(responsePayload) + ":" + exception));
//
//    TimeUnit.MINUTES.sleep(1);

  }


  @AfterAll
  static void afterAll() {
    EXECUTOR_SERVICE.shutdownNow();
  }
}
