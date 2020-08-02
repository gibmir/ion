package com.github.gibmir.ion.lib.netty.client.factory;

import com.github.gibmir.ion.api.client.batch.request.builder.BatchRequestBuilder;
import com.github.gibmir.ion.api.client.factory.RequestFactory;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.api.core.procedure.scan.ProcedureScanner;
import com.github.gibmir.ion.lib.netty.client.request.NettyRequest0;
import com.github.gibmir.ion.lib.netty.client.request.NettyRequest1;
import com.github.gibmir.ion.lib.netty.client.request.NettyRequest2;
import com.github.gibmir.ion.lib.netty.client.request.NettyRequest3;
import com.github.gibmir.ion.lib.netty.client.request.batch.NettyBatchRequest;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;

import javax.json.bind.Jsonb;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public class NettyRequestFactory implements RequestFactory {
  private final JsonRpcNettySender defaultJsonRpcNettySender;
  private final SocketAddress defaultSocketAddress;
  private final Jsonb defaultJsonb;
  private final Charset defaultCharset;

  public NettyRequestFactory(JsonRpcNettySender defaultJsonRpcNettySender, SocketAddress defaultSocketAddress,
                             Jsonb defaultJsonb, Charset defaultCharset) {
    this.defaultJsonRpcNettySender = defaultJsonRpcNettySender;
    this.defaultSocketAddress = defaultSocketAddress;
    this.defaultJsonb = defaultJsonb;
    this.defaultCharset = defaultCharset;
  }

  @Override
  public <R> NettyRequest0<R> noArg(Class<? extends JsonRemoteProcedure0<R>> procedure) {
    return new NettyRequest0<>(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature0(procedure));
  }

  @Override
  public <T, R> NettyRequest1<T, R> singleArg(Class<? extends JsonRemoteProcedure1<T, R>> procedure) {
    return new NettyRequest1<>(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature1(procedure));
  }

  @Override
  public <T1, T2, R> NettyRequest2<T1, T2, R> twoArg(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedure) {
    return new NettyRequest2<>(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature2(procedure));
  }

  @Override
  public <T1, T2, T3, R> NettyRequest3<T1, T2, T3, R> threeArg(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedure) {
    return new NettyRequest3<>(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature3(procedure));
  }

  @Override
  public BatchRequestBuilder<?> batch() {
    return new NettyBatchRequest.NettyBatchRequestBuilder(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb,
      defaultCharset);
  }

  @Override
  public void close() throws IOException {

  }
}
