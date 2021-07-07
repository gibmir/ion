package com.github.gibmir.ion.lib.netty.client.http.request.batch;

import com.github.gibmir.ion.api.client.batch.request.BatchRequest;
import com.github.gibmir.ion.api.client.batch.request.builder.BatchRequestBuilder;
import com.github.gibmir.ion.api.client.batch.request.builder.ResponseCallback;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.BatchRequestAggregator;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.NettyBatch;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.nio.charset.Charset;

public final class NettyHttpBatchRequest implements BatchRequest {
  private final NettyBatch nettyBatch;
  private final NettyHttpJsonRpcSender defaultJsonRpcNettySender;
  private final URI uri;
  private final Jsonb jsonb;
  private final Charset charset;

  private NettyHttpBatchRequest(final NettyBatch nettyBatch, final NettyHttpJsonRpcSender defaultJsonRpcNettySender,
                                final URI uri, final Jsonb jsonb, final Charset charset) {
    this.nettyBatch = nettyBatch;
    this.defaultJsonRpcNettySender = defaultJsonRpcNettySender;
    this.uri = uri;
    this.jsonb = jsonb;
    this.charset = charset;
  }

  /**
   * Creates http batch builder.
   *
   * @param batchRequestAggregator batch aggregator
   * @param defaultJsonRpcSender   sender
   * @param defaultUri             server uri
   * @param jsonb                  serializer
   * @param charset                encoding
   * @return batch builder
   */
  public static NettyHttpBatchRequest.Builder builder(final BatchRequestAggregator batchRequestAggregator,
                                                      final NettyHttpJsonRpcSender defaultJsonRpcSender,
                                                      final URI defaultUri, final Jsonb jsonb, final Charset charset) {
    return new Builder(batchRequestAggregator, defaultJsonRpcSender, defaultUri, jsonb, charset);
  }

  /**
   * Sets server uri.
   *
   * @param uri server uri
   * @return this
   */
  public NettyHttpBatchRequest uri(final URI uri) {
    return new NettyHttpBatchRequest(nettyBatch, defaultJsonRpcNettySender, uri, jsonb, charset);
  }

  /**
   * Sets serializer.
   *
   * @param jsonb serializer
   * @return this
   */
  public NettyHttpBatchRequest jsonb(final Jsonb jsonb) {
    return new NettyHttpBatchRequest(nettyBatch, defaultJsonRpcNettySender, uri, jsonb, charset);
  }

  /**
   * Sets encoding.
   *
   * @param charset encoding
   * @return this
   */
  public NettyHttpBatchRequest charset(final Charset charset) {
    return new NettyHttpBatchRequest(nettyBatch, defaultJsonRpcNettySender, uri, jsonb, charset);
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
   * @return server uri
   */
  public URI uri() {
    return uri;
  }

  @Override
  public void call() {
    defaultJsonRpcNettySender.send(nettyBatch, jsonb, charset, uri);
  }

  public static final class Builder implements BatchRequestBuilder<Builder> {
    private final BatchRequestAggregator batchRequestAggregator;
    private final NettyHttpJsonRpcSender defaultJsonRpcSender;
    private final URI defaultUri;
    private final Jsonb jsonb;
    private final Charset charset;

    private Builder(final BatchRequestAggregator batchRequestAggregator,
                    final NettyHttpJsonRpcSender defaultJsonRpcSender,
                    final URI defaultUri, final Jsonb jsonb, final Charset charset) {
      this.batchRequestAggregator = batchRequestAggregator;
      this.defaultJsonRpcSender = defaultJsonRpcSender;
      this.defaultUri = defaultUri;
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
    public NettyHttpBatchRequest build() {
      NettyBatch nettyBatch = new NettyBatch(batchRequestAggregator.getRequests(), batchRequestAggregator.getAwaitBatchParts());
      return new NettyHttpBatchRequest(nettyBatch, defaultJsonRpcSender, defaultUri, jsonb, charset);
    }
  }
}
