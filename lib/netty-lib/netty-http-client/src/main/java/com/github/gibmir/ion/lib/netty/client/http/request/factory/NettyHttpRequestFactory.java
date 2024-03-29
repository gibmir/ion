package com.github.gibmir.ion.lib.netty.client.http.request.factory;

import com.github.gibmir.ion.api.client.factory.RequestFactory;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.BatchRequestAggregator;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest0;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest1;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest2;
import com.github.gibmir.ion.lib.netty.client.http.request.NettyHttpRequest3;
import com.github.gibmir.ion.lib.netty.client.http.request.batch.NettyHttpBatchRequest;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;
import com.github.gibmir.ion.scanner.ProcedureScanner;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.nio.charset.Charset;

public final class NettyHttpRequestFactory implements RequestFactory {
  private final NettyHttpJsonRpcSender defaultJsonRpcNettySender;
  private final URI defaultUri;
  private final Jsonb defaultJsonb;
  private final Charset defaultCharset;

  public NettyHttpRequestFactory(final NettyHttpJsonRpcSender defaultJsonRpcNettySender, final URI defaultUri,
                                 final Jsonb defaultJsonb, final Charset defaultCharset) {
    this.defaultJsonRpcNettySender = defaultJsonRpcNettySender;
    this.defaultUri = defaultUri;
    this.defaultJsonb = defaultJsonb;
    this.defaultCharset = defaultCharset;
  }

  @Override
  public <R> NettyHttpRequest0<R> noArg(final Class<? extends JsonRemoteProcedure0<R>> procedure) {
    return new NettyHttpRequest0<>(defaultJsonRpcNettySender, defaultUri, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature0(procedure));
  }

  @Override
  public <T, R> NettyHttpRequest1<T, R> singleArg(final Class<? extends JsonRemoteProcedure1<T, R>> procedure) {
    return new NettyHttpRequest1<>(defaultJsonRpcNettySender, defaultUri, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature1(procedure));
  }

  @Override
  public <T1, T2, R> NettyHttpRequest2<T1, T2, R> twoArg(final Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedure) {
    return new NettyHttpRequest2<>(defaultJsonRpcNettySender, defaultUri, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature2(procedure));
  }

  @Override
  public <T1, T2, T3, R> NettyHttpRequest3<T1, T2, T3, R> threeArg(final Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedure) {
    return new NettyHttpRequest3<>(defaultJsonRpcNettySender, defaultUri, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature3(procedure));
  }

  @Override
  public NettyHttpBatchRequest.Builder batch() {
    return NettyHttpBatchRequest.builder(new BatchRequestAggregator(), defaultJsonRpcNettySender, defaultUri,
      defaultJsonb, defaultCharset);
  }
}
