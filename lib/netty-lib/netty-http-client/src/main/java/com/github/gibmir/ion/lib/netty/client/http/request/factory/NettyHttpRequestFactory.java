package com.github.gibmir.ion.lib.netty.client.http.request.factory;

import com.github.gibmir.ion.api.client.batch.request.builder.BatchRequestBuilder;
import com.github.gibmir.ion.api.client.factory.RequestFactory;
import com.github.gibmir.ion.api.client.request.Request2;
import com.github.gibmir.ion.api.client.request.Request3;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest0;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest1;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest2;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest3;
import com.github.gibmir.ion.lib.netty.client.http.request.batch.NettyHttpBatchRequest;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;

import javax.json.bind.Jsonb;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

public class NettyHttpRequestFactory implements RequestFactory {
  private final NettyHttpJsonRpcSender defaultJsonRpcNettySender;
  private final URI defaultUri;
  private final Jsonb defaultJsonb;
  private final Charset defaultCharset;

  public NettyHttpRequestFactory(NettyHttpJsonRpcSender defaultJsonRpcNettySender, URI defaultUri,
                                 Jsonb defaultJsonb, Charset defaultCharset) {
    this.defaultJsonRpcNettySender = defaultJsonRpcNettySender;
    this.defaultUri = defaultUri;
    this.defaultJsonb = defaultJsonb;
    this.defaultCharset = defaultCharset;
  }

  @Override
  public <R> NettyHttpRequest0<R> noArg(Class<? extends JsonRemoteProcedure0<R>> procedure) {
    return new NettyHttpRequest0<>(defaultJsonRpcNettySender, defaultUri, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature0(procedure));
  }

  @Override
  public <T, R> NettyHttpRequest1<T, R> singleArg(Class<? extends JsonRemoteProcedure1<T, R>> procedure) {
    return new NettyHttpRequest1<>(defaultJsonRpcNettySender, defaultUri, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature1(procedure));
  }

  @Override
  public <T1, T2, R> Request2<T1, T2, R> twoArg(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedure) {
    return new NettyHttpRequest2<>(defaultJsonRpcNettySender, defaultUri, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature2(procedure));
  }

  @Override
  public <T1, T2, T3, R> Request3<T1, T2, T3, R> threeArg(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedure) {
    return new NettyHttpRequest3<>(defaultJsonRpcNettySender, defaultUri, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature3(procedure));
  }

  @Override
  public BatchRequestBuilder<?> batch() {
    return new NettyHttpBatchRequest.Builder(defaultJsonRpcNettySender, defaultUri, defaultJsonb,
      defaultCharset);
  }

  @Override
  public void close() {

  }
}
