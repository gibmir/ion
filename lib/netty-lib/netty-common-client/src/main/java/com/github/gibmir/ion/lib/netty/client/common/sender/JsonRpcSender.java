package com.github.gibmir.ion.lib.netty.client.common.sender;

import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.NettyBatch;

import javax.json.bind.Jsonb;
import java.io.Closeable;
import java.lang.reflect.Type;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public interface JsonRpcSender extends Closeable {
  <R> CompletableFuture<R> send(String id, RequestDto request, Jsonb jsonb, Charset charset,
                                Type returnType, SocketAddress socketAddress);

  void send(NotificationDto request, Jsonb jsonb, Charset charset,
            SocketAddress socketAddress);

  void send(NettyBatch nettyBatch, Jsonb jsonb, Charset charset,
                                        SocketAddress socketAddress);
}
