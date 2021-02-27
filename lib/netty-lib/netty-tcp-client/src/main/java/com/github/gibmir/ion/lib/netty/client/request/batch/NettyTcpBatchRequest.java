package com.github.gibmir.ion.lib.netty.client.request.batch;

import com.github.gibmir.ion.api.client.batch.request.BatchRequest;
import com.github.gibmir.ion.api.client.batch.request.builder.BatchRequestBuilder;
import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.BatchRequestAggregator;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.NettyBatch;
import com.github.gibmir.ion.lib.netty.client.common.sender.JsonRpcSender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public class NettyTcpBatchRequest implements BatchRequest {
  protected final NettyBatch nettyBatch;
  protected final JsonRpcSender defaultJsonRpcNettySender;
  protected final SocketAddress defaultSocketAddress;
  protected final Jsonb jsonb;
  protected final Charset charset;

  private NettyTcpBatchRequest(NettyBatch nettyBatch, JsonRpcSender defaultJsonRpcSender,
                               SocketAddress defaultSocketAddress, Jsonb jsonb, Charset charset) {
    this.nettyBatch = nettyBatch;
    this.defaultJsonRpcNettySender = defaultJsonRpcSender;
    this.defaultSocketAddress = defaultSocketAddress;
    this.jsonb = jsonb;
    this.charset = charset;
  }

  public static Builder builder(BatchRequestAggregator batchRequestAggregator, JsonRpcSender defaultJsonRpcSender,
                                SocketAddress defaultSocketAddress, Jsonb jsonb, Charset charset) {
    return new Builder(batchRequestAggregator, defaultJsonRpcSender, defaultSocketAddress, jsonb, charset);
  }

  @Override
  public void call() {
    defaultJsonRpcNettySender.send(nettyBatch, jsonb, charset, defaultSocketAddress);
  }

  public NettyTcpBatchRequest socketAddress(SocketAddress socketAddress) {
    return new NettyTcpBatchRequest(nettyBatch, defaultJsonRpcNettySender, socketAddress, jsonb, charset);
  }

  public NettyTcpBatchRequest jsonb(Jsonb jsonb) {
    return new NettyTcpBatchRequest(nettyBatch, defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset);
  }

  public NettyTcpBatchRequest charset(Charset charset) {
    return new NettyTcpBatchRequest(nettyBatch, defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset);
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

  public static class Builder implements BatchRequestBuilder<Builder> {
    private final BatchRequestAggregator batchRequestAggregator;
    private final JsonRpcSender defaultJsonRpcSender;
    private final SocketAddress defaultSocketAddress;
    private final Jsonb jsonb;
    private final Charset charset;

    private Builder(BatchRequestAggregator batchRequestAggregator, JsonRpcSender defaultJsonRpcSender,
                    SocketAddress defaultSocketAddress, Jsonb jsonb, Charset charset) {
      this.batchRequestAggregator = batchRequestAggregator;
      this.defaultJsonRpcSender = defaultJsonRpcSender;
      this.defaultSocketAddress = defaultSocketAddress;
      this.jsonb = jsonb;
      this.charset = charset;
    }

    @Override
    public <R> Builder addRequest(String id, Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0,
                                  ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addRequest(id, jsonRemoteProcedure0, responseCallback);
      return this;
    }

    @Override
    public <T, R> Builder addPositionalRequest(String id,
                                               Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1,
                                               T arg, ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addPositionalRequest(id, jsonRemoteProcedure1, arg, responseCallback);
      return this;
    }

    @Override
    public <T1, T2, R> Builder addPositionalRequest(String id,
                                                    Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                                    T1 arg1, T2 arg2, ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addPositionalRequest(id, jsonRemoteProcedure2, arg1, arg2, responseCallback);
      return this;
    }

    @Override
    public <T1, T2, T3, R> Builder addPositionalRequest(String id,
                                                        Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                        T1 arg1, T2 arg2, T3 arg3, ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addPositionalRequest(id, jsonRemoteProcedure3, arg1, arg2, arg3, responseCallback);
      return this;
    }

    @Override
    public <T, R> Builder addNamedRequest(String id, Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1,
                                          T arg, ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addNamedRequest(id, jsonRemoteProcedure1, arg, responseCallback);
      return this;
    }

    @Override
    public <T1, T2, R> Builder addNamedRequest(String id, Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                               T1 arg1, T2 arg2, ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addNamedRequest(id, jsonRemoteProcedure2, arg1, arg2, responseCallback);
      return this;
    }

    @Override
    public <T1, T2, T3, R> Builder addNamedRequest(String id, Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                   T1 arg1, T2 arg2, T3 arg3, ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addNamedRequest(id, jsonRemoteProcedure3, arg1, arg2, arg3, responseCallback);
      return this;
    }

    @Override
    public <R> Builder addNotification(Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0) {
      batchRequestAggregator.addNotification(jsonRemoteProcedure0);
      return this;
    }

    @Override
    public <T, R> Builder addPositionalNotification(Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1, T arg) {
      batchRequestAggregator.addPositionalNotification(jsonRemoteProcedure1, arg);
      return this;
    }

    @Override
    public <T1, T2, R> Builder addPositionalNotification(Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                                         T1 arg1, T2 arg2) {
      batchRequestAggregator.addPositionalNotification(jsonRemoteProcedure2, arg1, arg2);
      return this;
    }

    @Override
    public <T1, T2, T3, R> Builder addPositionalNotification(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                             T1 arg1, T2 arg2, T3 arg3) {
      batchRequestAggregator.addPositionalNotification(jsonRemoteProcedure3, arg1, arg2, arg3);
      return this;
    }

    @Override
    public <T, R> Builder addNamedNotification(Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1, T arg) {
      batchRequestAggregator.addNamedNotification(jsonRemoteProcedure1, arg);
      return this;
    }

    @Override
    public <T1, T2, R> Builder addNamedNotification(Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2, T1 arg1, T2 arg2) {
      batchRequestAggregator.addNamedNotification(jsonRemoteProcedure2, arg1, arg2);
      return this;
    }

    @Override
    public <T1, T2, T3, R> Builder addNamedNotification(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3, T1 arg1, T2 arg2, T3 arg3) {
      batchRequestAggregator.addNamedNotification(jsonRemoteProcedure3, arg1, arg2, arg3);
      return this;
    }

    @Override
    public NettyTcpBatchRequest build() {
      NettyBatch nettyBatch = new NettyBatch(batchRequestAggregator.getRequests(),
        batchRequestAggregator.getAwaitBatchParts());
      return new NettyTcpBatchRequest(nettyBatch, defaultJsonRpcSender, defaultSocketAddress, jsonb, charset);
    }
  }

}
