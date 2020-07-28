package com.github.gibmir.ion.lib.netty.client.request.batch;

import com.github.gibmir.ion.api.client.batch.request.BatchRequest;
import com.github.gibmir.ion.api.client.batch.request.builder.BatchRequestBuilder;
import com.github.gibmir.ion.api.client.batch.response.BatchResponse;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.request.NettyRequest0;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;
import com.github.gibmir.ion.lib.netty.client.sender.handler.response.future.ResponseFuture;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NettyBatchRequest implements BatchRequest {
  protected final NettyBatch nettyBatch;
  protected final JsonRpcNettySender defaultJsonRpcNettySender;
  protected final SocketAddress defaultSocketAddress;
  protected final Jsonb jsonb;
  protected final Charset charset;

  private NettyBatchRequest(NettyBatch nettyBatch, JsonRpcNettySender defaultJsonRpcNettySender,
                            SocketAddress defaultSocketAddress, Jsonb jsonb, Charset charset) {
    this.nettyBatch = nettyBatch;
    this.defaultJsonRpcNettySender = defaultJsonRpcNettySender;
    this.defaultSocketAddress = defaultSocketAddress;
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  public CompletableFuture<BatchResponse> call() {
    return defaultJsonRpcNettySender.send(nettyBatch, jsonb, charset, defaultSocketAddress);
  }

  public NettyBatchRequest socketAddress(SocketAddress socketAddress) {
    return new NettyBatchRequest(nettyBatch, defaultJsonRpcNettySender, socketAddress, jsonb, charset);
  }

  public NettyBatchRequest jsonb(Jsonb jsonb) {
    return new NettyBatchRequest(nettyBatch, defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset);
  }

  public NettyBatchRequest charset(Charset charset) {
    return new NettyBatchRequest(nettyBatch, defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset);
  }

  public Jsonb jsonb() {
    return jsonb;
  }

  public Charset charset() {
    return charset;
  }

  public SocketAddress socketAddress() {
    return defaultSocketAddress;
  }

  public static class NettyBatchRequestBuilder implements BatchRequestBuilder<NettyBatchRequestBuilder> {
    private final List<JsonRpcRequest> requests = new ArrayList<>();
    private final List<CompletableFuture<?>> responseCompletableFutures = new ArrayList<>();
    private final List<ResponseFuture> responseFutures = new ArrayList<>();
    private final JsonRpcNettySender defaultJsonRpcNettySender;
    private final SocketAddress defaultSocketAddress;
    private final Jsonb jsonb;
    private final Charset charset;

    public NettyBatchRequestBuilder(JsonRpcNettySender defaultJsonRpcNettySender, SocketAddress defaultSocketAddress,
                                    Jsonb jsonb, Charset charset) {
      this.defaultJsonRpcNettySender = defaultJsonRpcNettySender;
      this.defaultSocketAddress = defaultSocketAddress;
      this.jsonb = jsonb;
      this.charset = charset;
    }

    @Override
    public <R> NettyBatchRequestBuilder add(String id, Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0,
                                            Class<R> returnType) {
      CompletableFuture<Object> completableFuture = new CompletableFuture<>();
      ResponseFuture responseFuture = new ResponseFuture(id, returnType, completableFuture);
      RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedure0.getName(),
        NettyRequest0.EMPTY_PAYLOAD);
      requests.add(requestDto);
      responseCompletableFutures.add(completableFuture);
      responseFutures.add(responseFuture);
      return this;
    }

    @Override
    public <T, R> NettyBatchRequestBuilder addPositional(String id,
                                                         Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1,
                                                         T arg, Class<R> returnType) {
      CompletableFuture<Object> completableFuture = new CompletableFuture<>();
      ResponseFuture responseFuture = new ResponseFuture(id, returnType, completableFuture);
      RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedure1.getName(),
        new Object[]{arg});
      requests.add(requestDto);
      responseCompletableFutures.add(completableFuture);
      responseFutures.add(responseFuture);
      return this;
    }

    @Override
    public <T1, T2, R> NettyBatchRequestBuilder addPositional(String id,
                                                              Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                                              T1 arg1, T2 arg2, Class<R> returnType) {
      CompletableFuture<Object> completableFuture = new CompletableFuture<>();
      ResponseFuture responseFuture = new ResponseFuture(id, returnType, completableFuture);
      RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedure2.getName(),
        new Object[]{arg1, arg2});
      requests.add(requestDto);
      responseCompletableFutures.add(completableFuture);
      responseFutures.add(responseFuture);
      return this;
    }

    @Override
    public <T1, T2, T3, R> NettyBatchRequestBuilder addPositional(String id,
                                                                  Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                                  T1 arg1, T2 arg2, T3 arg3, Class<R> returnType) {
      CompletableFuture<Object> completableFuture = new CompletableFuture<>();
      ResponseFuture responseFuture = new ResponseFuture(id, returnType, completableFuture);
      RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedure3.getName(),
        new Object[]{arg1, arg2, arg3});
      requests.add(requestDto);
      responseCompletableFutures.add(completableFuture);
      responseFutures.add(responseFuture);
      return this;
    }

    @Override
    public <R> NettyBatchRequestBuilder addNotification(Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0) {
      requests.add(new NotificationDto(jsonRemoteProcedure0.getName(), NettyRequest0.EMPTY_PAYLOAD));
      return this;
    }

    @Override
    public <T, R> NettyBatchRequestBuilder addNotification(Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1, T arg) {
      requests.add(new NotificationDto(jsonRemoteProcedure1.getName(), new Object[]{arg}));
      return this;
    }

    @Override
    public <T1, T2, R> NettyBatchRequestBuilder addNotification(Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2, T1 arg1, T2 arg2) {
      requests.add(new NotificationDto(jsonRemoteProcedure2.getName(), new Object[]{arg1, arg2}));
      return this;
    }

    @Override
    public <T1, T2, T3, R> NettyBatchRequestBuilder addNotification(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3, T1 arg1, T2 arg2, T3 arg3) {
      requests.add(new NotificationDto(jsonRemoteProcedure3.getName(), new Object[]{arg1, arg2, arg3}));
      return this;
    }

    @Override
    public NettyBatchRequest build() {
      JsonRpcRequest[] jsonRpcRequests = new JsonRpcRequest[requests.size()];
      ResponseFuture[] responses = new ResponseFuture[responseFutures.size()];
      CompletableFuture<?>[] completableFutures = new CompletableFuture[responseCompletableFutures.size()];
      NettyBatch nettyBatch = new NettyBatch(requests.toArray(jsonRpcRequests), responseFutures.toArray(responses),
        responseCompletableFutures.toArray(completableFutures));
      return new NettyBatchRequest(nettyBatch, defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset);
    }
  }

}
