package com.github.gibmir.ion.lib.netty.client.tcp.request.batch;

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

public final class NettyTcpBatchRequest implements BatchRequest {
  private final NettyBatch nettyBatch;
  private final JsonRpcSender defaultJsonRpcNettySender;
  private final SocketAddress defaultSocketAddress;
  private final Jsonb jsonb;
  private final Charset charset;

  private NettyTcpBatchRequest(final NettyBatch nettyBatch, final JsonRpcSender defaultJsonRpcSender,
                               final SocketAddress defaultSocketAddress, final Jsonb jsonb, final Charset charset) {
    this.nettyBatch = nettyBatch;
    this.defaultJsonRpcNettySender = defaultJsonRpcSender;
    this.defaultSocketAddress = defaultSocketAddress;
    this.jsonb = jsonb;
    this.charset = charset;
  }

  /**
   * Creates batch builder.
   *
   * @param batchRequestAggregator batch request aggregator
   * @param defaultJsonRpcSender   sender
   * @param defaultSocketAddress   server address
   * @param jsonb                  serializer
   * @param charset                encoding
   * @return builder
   */
  public static Builder builder(final BatchRequestAggregator batchRequestAggregator, final JsonRpcSender defaultJsonRpcSender,
                                final SocketAddress defaultSocketAddress, final Jsonb jsonb, final Charset charset) {
    return new Builder(batchRequestAggregator, defaultJsonRpcSender, defaultSocketAddress, jsonb, charset);
  }

  @Override
  public void call() {
    defaultJsonRpcNettySender.send(nettyBatch, jsonb, charset, defaultSocketAddress);
  }

  /**
   * Sets server socket address.
   *
   * @param socketAddress server socket address
   * @return this
   */
  public NettyTcpBatchRequest socketAddress(final SocketAddress socketAddress) {
    return new NettyTcpBatchRequest(nettyBatch, defaultJsonRpcNettySender, socketAddress, jsonb, charset);
  }

  /**
   * Sets json serializer.
   *
   * @param jsonb serializer
   * @return this
   */
  public NettyTcpBatchRequest jsonb(final Jsonb jsonb) {
    return new NettyTcpBatchRequest(nettyBatch, defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset);
  }

  /**
   * Sets encoding.
   *
   * @param charset encoding
   * @return this
   */
  public NettyTcpBatchRequest charset(final Charset charset) {
    return new NettyTcpBatchRequest(nettyBatch, defaultJsonRpcNettySender, defaultSocketAddress, jsonb, charset);
  }

  /**
   * @return serializer
   */
  public Jsonb jsonb() {
    return jsonb;
  }

  /**
   * @return encoding
   */
  public Charset charset() {
    return charset;
  }

  /**
   * @return server socket address
   */
  public SocketAddress socketAddress() {
    return defaultSocketAddress;
  }

  public static final class Builder implements BatchRequestBuilder<Builder> {
    private final BatchRequestAggregator batchRequestAggregator;
    private final JsonRpcSender defaultJsonRpcSender;
    private final SocketAddress defaultSocketAddress;
    private final Jsonb jsonb;
    private final Charset charset;

    private Builder(final BatchRequestAggregator batchRequestAggregator, final JsonRpcSender defaultJsonRpcSender,
                    final SocketAddress defaultSocketAddress, final Jsonb jsonb, final Charset charset) {
      this.batchRequestAggregator = batchRequestAggregator;
      this.defaultJsonRpcSender = defaultJsonRpcSender;
      this.defaultSocketAddress = defaultSocketAddress;
      this.jsonb = jsonb;
      this.charset = charset;
    }

    @Override
    public <R> Builder addRequest(final String id, final Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0,
                                  final ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addRequest(id, jsonRemoteProcedure0, responseCallback);
      return this;
    }

    @Override
    public <T, R> Builder addPositionalRequest(final String id,
                                               final Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1,
                                               final T arg, final ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addPositionalRequest(id, jsonRemoteProcedure1, arg, responseCallback);
      return this;
    }

    @Override
    public <T1, T2, R> Builder addPositionalRequest(final String id,
                                                    final Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                                    final T1 arg1, final T2 arg2, final ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addPositionalRequest(id, jsonRemoteProcedure2, arg1, arg2, responseCallback);
      return this;
    }

    @Override
    public <T1, T2, T3, R> Builder addPositionalRequest(final String id,
                                                        final Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                        final T1 arg1, final T2 arg2, final T3 arg3,
                                                        final ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addPositionalRequest(id, jsonRemoteProcedure3, arg1, arg2, arg3, responseCallback);
      return this;
    }

    @Override
    public <T, R> Builder addNamedRequest(final String id,
                                          final Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1,
                                          final T arg, final ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addNamedRequest(id, jsonRemoteProcedure1, arg, responseCallback);
      return this;
    }

    @Override
    public <T1, T2, R> Builder addNamedRequest(final String id,
                                               final Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                               final T1 arg1, final T2 arg2, final ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addNamedRequest(id, jsonRemoteProcedure2, arg1, arg2, responseCallback);
      return this;
    }

    @Override
    public <T1, T2, T3, R> Builder addNamedRequest(final String id,
                                                   final Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                   final T1 arg1, final T2 arg2, final T3 arg3,
                                                   final ResponseCallback<R> responseCallback) {
      batchRequestAggregator.addNamedRequest(id, jsonRemoteProcedure3, arg1, arg2, arg3, responseCallback);
      return this;
    }

    @Override
    public <R> Builder addNotification(final Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0) {
      batchRequestAggregator.addNotification(jsonRemoteProcedure0);
      return this;
    }

    @Override
    public <T, R> Builder addPositionalNotification(final Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1,
                                                    final T arg) {
      batchRequestAggregator.addPositionalNotification(jsonRemoteProcedure1, arg);
      return this;
    }

    @Override
    public <T1, T2, R> Builder addPositionalNotification(final Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                                         final T1 arg1, final T2 arg2) {
      batchRequestAggregator.addPositionalNotification(jsonRemoteProcedure2, arg1, arg2);
      return this;
    }

    @Override
    public <T1, T2, T3, R> Builder addPositionalNotification(final Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                             final T1 arg1, final T2 arg2, final T3 arg3) {
      batchRequestAggregator.addPositionalNotification(jsonRemoteProcedure3, arg1, arg2, arg3);
      return this;
    }

    @Override
    public <T, R> Builder addNamedNotification(final Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1,
                                               final T arg) {
      batchRequestAggregator.addNamedNotification(jsonRemoteProcedure1, arg);
      return this;
    }

    @Override
    public <T1, T2, R> Builder addNamedNotification(final Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                                    final T1 arg1, final T2 arg2) {
      batchRequestAggregator.addNamedNotification(jsonRemoteProcedure2, arg1, arg2);
      return this;
    }

    @Override
    public <T1, T2, T3, R> Builder addNamedNotification(final Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                        final T1 arg1, final T2 arg2, final T3 arg3) {
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
