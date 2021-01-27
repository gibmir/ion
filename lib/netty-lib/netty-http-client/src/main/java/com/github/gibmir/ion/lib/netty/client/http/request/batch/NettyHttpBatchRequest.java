package com.github.gibmir.ion.lib.netty.client.http.request.batch;

import com.github.gibmir.ion.api.client.batch.request.BatchRequest;
import com.github.gibmir.ion.api.client.batch.request.builder.BatchRequestBuilder;
import com.github.gibmir.ion.api.client.batch.response.BatchResponse;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.request.transfer.RequestDto;
import com.github.gibmir.ion.api.dto.request.transfer.notification.NotificationDto;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.NettyBatch;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest0;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NettyHttpBatchRequest implements BatchRequest {
  private final NettyBatch nettyBatch;
  private final NettyHttpJsonRpcSender defaultJsonRpcNettySender;
  private final URI uri;
  private final Jsonb jsonb;
  private final Charset charset;

  public NettyHttpBatchRequest(NettyBatch nettyBatch, NettyHttpJsonRpcSender defaultJsonRpcNettySender,
                               URI uri, Jsonb jsonb, Charset charset) {
    this.nettyBatch = nettyBatch;
    this.defaultJsonRpcNettySender = defaultJsonRpcNettySender;
    this.uri = uri;
    this.jsonb = jsonb;
    this.charset = charset;
  }

  public NettyHttpBatchRequest uri(URI uri) {
    return new NettyHttpBatchRequest(nettyBatch, defaultJsonRpcNettySender, uri, jsonb, charset);
  }

  public NettyHttpBatchRequest jsonb(Jsonb jsonb) {
    return new NettyHttpBatchRequest(nettyBatch, defaultJsonRpcNettySender, uri, jsonb, charset);
  }

  public NettyHttpBatchRequest charset(Charset charset) {
    return new NettyHttpBatchRequest(nettyBatch, defaultJsonRpcNettySender, uri, jsonb, charset);
  }

  public Jsonb jsonb() {
    return jsonb;
  }

  public Charset charset() {
    return charset;
  }

  public URI uri() {
    return uri;
  }

  @Override
  public CompletableFuture<BatchResponse> call() {
    return defaultJsonRpcNettySender.send(nettyBatch, jsonb, charset, uri);
  }
  public static class Builder implements BatchRequestBuilder<Builder> {
    private final List<JsonRpcRequest> requests = new ArrayList<>();
    private final List<NettyBatch.AwaitBatchPart> awaitBatchParts = new ArrayList<>();
    private final NettyHttpJsonRpcSender defaultJsonRpcSender;
    private final URI defaultUri;
    private final Jsonb jsonb;
    private final Charset charset;

    public Builder(NettyHttpJsonRpcSender defaultJsonRpcSender, URI defaultUri,
                   Jsonb jsonb, Charset charset) {
      this.defaultJsonRpcSender = defaultJsonRpcSender;
      this.defaultUri = defaultUri;
      this.jsonb = jsonb;
      this.charset = charset;
    }

    @Override
    public <R> Builder add(String id, Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0) {
      JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature0(jsonRemoteProcedure0);
      RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
        NettyHttpRequest0.EMPTY_PAYLOAD);
      requests.add(requestDto);
      awaitBatchParts.add(new NettyBatch.AwaitBatchPart(id, jsonRemoteProcedureSignature.getReturnType()));
      return this;
    }

    @Override
    public <T, R> Builder addPositional(String id,
                                        Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1,
                                        T arg) {
      JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature1(jsonRemoteProcedure1);
      RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
        new Object[]{arg});
      requests.add(requestDto);
      awaitBatchParts.add(new NettyBatch.AwaitBatchPart(id, jsonRemoteProcedureSignature.getReturnType()));
      return this;
    }

    @Override
    public <T1, T2, R> Builder addPositional(String id,
                                             Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2,
                                             T1 arg1, T2 arg2) {
      JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature2(jsonRemoteProcedure2);
      RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
        new Object[]{arg1, arg2});
      requests.add(requestDto);
      awaitBatchParts.add(new NettyBatch.AwaitBatchPart(id, jsonRemoteProcedureSignature.getReturnType()));
      return this;
    }

    @Override
    public <T1, T2, T3, R> Builder addPositional(String id,
                                                 Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3,
                                                 T1 arg1, T2 arg2, T3 arg3) {
      JsonRemoteProcedureSignature jsonRemoteProcedureSignature = ProcedureScanner.resolveSignature3(jsonRemoteProcedure3);
      RequestDto requestDto = RequestDto.positional(id, jsonRemoteProcedureSignature.getProcedureName(),
        new Object[]{arg1, arg2, arg3});
      requests.add(requestDto);
      awaitBatchParts.add(new NettyBatch.AwaitBatchPart(id, jsonRemoteProcedureSignature.getReturnType()));
      return this;
    }

    @Override
    public <R> Builder addNotification(Class<? extends JsonRemoteProcedure0<R>> jsonRemoteProcedure0) {
      requests.add(new NotificationDto(jsonRemoteProcedure0.getName(), NettyHttpRequest0.EMPTY_PAYLOAD));
      return this;
    }

    @Override
    public <T, R> Builder addNotification(Class<? extends JsonRemoteProcedure1<T, R>> jsonRemoteProcedure1, T arg) {
      requests.add(new NotificationDto(jsonRemoteProcedure1.getName(), new Object[]{arg}));
      return this;
    }

    @Override
    public <T1, T2, R> Builder addNotification(Class<? extends JsonRemoteProcedure2<T1, T2, R>> jsonRemoteProcedure2, T1 arg1, T2 arg2) {
      requests.add(new NotificationDto(jsonRemoteProcedure2.getName(), new Object[]{arg1, arg2}));
      return this;
    }

    @Override
    public <T1, T2, T3, R> Builder addNotification(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> jsonRemoteProcedure3, T1 arg1, T2 arg2, T3 arg3) {
      requests.add(new NotificationDto(jsonRemoteProcedure3.getName(), new Object[]{arg1, arg2, arg3}));
      return this;
    }

    @Override
    public NettyHttpBatchRequest build() {
      NettyBatch nettyBatch = new NettyBatch(requests, awaitBatchParts);
      return new NettyHttpBatchRequest(nettyBatch, defaultJsonRpcSender, defaultUri, jsonb, charset);
    }
  }
}
