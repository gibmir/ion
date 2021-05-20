package com.github.gibmir.ion.lib.netty.client.request.factory;

import com.github.gibmir.ion.api.client.factory.RequestFactory;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.scanner.ProcedureScanner;
import com.github.gibmir.ion.lib.netty.client.common.request.batch.BatchRequestAggregator;
import com.github.gibmir.ion.lib.netty.client.common.sender.JsonRpcSender;
import com.github.gibmir.ion.lib.netty.client.request.NettyTcpRequest0;
import com.github.gibmir.ion.lib.netty.client.request.NettyTcpRequest1;
import com.github.gibmir.ion.lib.netty.client.request.NettyTcpRequest2;
import com.github.gibmir.ion.lib.netty.client.request.NettyTcpRequest3;
import com.github.gibmir.ion.lib.netty.client.request.batch.NettyTcpBatchRequest;

import javax.json.bind.Jsonb;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public class NettyTcpRequestFactory implements RequestFactory {
  private final JsonRpcSender defaultJsonRpcNettySender;
  private final SocketAddress defaultSocketAddress;
  private final Jsonb defaultJsonb;
  private final Charset defaultCharset;

  public NettyTcpRequestFactory(JsonRpcSender defaultJsonRpcSender, SocketAddress defaultSocketAddress,
                                Jsonb defaultJsonb, Charset defaultCharset) {
    this.defaultJsonRpcNettySender = defaultJsonRpcSender;
    this.defaultSocketAddress = defaultSocketAddress;
    this.defaultJsonb = defaultJsonb;
    this.defaultCharset = defaultCharset;
  }

  @Override
  public <R> NettyTcpRequest0<R> noArg(Class<? extends JsonRemoteProcedure0<R>> procedure) {
    return new NettyTcpRequest0<>(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature0(procedure));
  }

  @Override
  public <T, R> NettyTcpRequest1<T, R> singleArg(Class<? extends JsonRemoteProcedure1<T, R>> procedure) {
    return new NettyTcpRequest1<>(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature1(procedure));
  }

  @Override
  public <T1, T2, R> NettyTcpRequest2<T1, T2, R> twoArg(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedure) {
    return new NettyTcpRequest2<>(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature2(procedure));
  }

  @Override
  public <T1, T2, T3, R> NettyTcpRequest3<T1, T2, T3, R> threeArg(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedure) {
    return new NettyTcpRequest3<>(defaultJsonRpcNettySender, defaultSocketAddress, defaultJsonb, defaultCharset,
      ProcedureScanner.resolveSignature3(procedure));
  }

  @Override
  public NettyTcpBatchRequest.Builder batch() {
    return NettyTcpBatchRequest.builder(new BatchRequestAggregator(), defaultJsonRpcNettySender,
      defaultSocketAddress, defaultJsonb, defaultCharset);
  }

  @Override
  public void close() throws IOException {
    if (defaultJsonRpcNettySender != null) {
      defaultJsonRpcNettySender.close();
    }
  }
}
