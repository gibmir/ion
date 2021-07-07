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
  /**
   * Sends json-rpc request.
   *
   * @param id            request id
   * @param request       request dto
   * @param jsonb         serializer
   * @param charset       request charset
   * @param returnType    request return type
   * @param socketAddress address
   * @param <R>           return type
   * @return future response
   */
  <R> CompletableFuture<R> send(String id, RequestDto request, Jsonb jsonb, Charset charset,
                                Type returnType, SocketAddress socketAddress);

  /**
   * Sends json-rpc notification.
   *
   * @param request       request dto
   * @param jsonb         serializer
   * @param charset       request charset
   * @param socketAddress address
   */
  void send(NotificationDto request, Jsonb jsonb, Charset charset,
            SocketAddress socketAddress);

  /**
   * Sends json-rpc batch.
   *
   * @param nettyBatch    batch
   * @param jsonb         serializer
   * @param charset       request charset
   * @param socketAddress address
   */
  void send(NettyBatch nettyBatch, Jsonb jsonb, Charset charset,
            SocketAddress socketAddress);
}
