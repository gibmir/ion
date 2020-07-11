package com.github.gibmir.ion.lib.netty.client.request.factory;

import com.github.gibmir.ion.api.client.request.factory.RequestFactory;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure0;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure1;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure2;
import com.github.gibmir.ion.api.core.procedure.JsonRemoteProcedure3;
import com.github.gibmir.ion.lib.netty.client.request.NettyRequest0;
import com.github.gibmir.ion.lib.netty.client.request.NettyRequest1;
import com.github.gibmir.ion.lib.netty.client.request.NettyRequest2;
import com.github.gibmir.ion.lib.netty.client.request.NettyRequest3;
import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;

import javax.json.bind.Jsonb;
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
  public <R> NettyRequest0<R> noArg(Class<? extends JsonRemoteProcedure0<R>> procedure, Class<R> returnType) {
    return new NettyRequest0<>(returnType, procedure.getName(), defaultJsonRpcNettySender, defaultSocketAddress,
      defaultJsonb, defaultCharset);
  }

  @Override
  public <T, R> NettyRequest1<T, R> singleArg(Class<? extends JsonRemoteProcedure1<T, R>> procedure, Class<R> returnType) {
    return new NettyRequest1<>(returnType, procedure.getName(), defaultJsonRpcNettySender, defaultSocketAddress,
      defaultJsonb, defaultCharset);
  }

  @Override
  public <T1, T2, R> NettyRequest2<T1, T2, R> twoArg(Class<? extends JsonRemoteProcedure2<T1, T2, R>> procedure,
                                                     Class<R> returnType) {
    return new NettyRequest2<>(returnType, procedure.getName(), defaultJsonRpcNettySender, defaultSocketAddress,
      defaultJsonb, defaultCharset);
  }

  @Override
  public <T1, T2, T3, R> NettyRequest3<T1, T2, T3, R> threeArg(Class<? extends JsonRemoteProcedure3<T1, T2, T3, R>> procedure,
                                                               Class<R> returnType) {
    return new NettyRequest3<>(returnType, procedure.getName(), defaultJsonRpcNettySender, defaultSocketAddress,
      defaultJsonb, defaultCharset);
  }
}
