package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.api.core.procedure.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.lib.netty.client.common.sender.JsonRpcSender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public abstract class AbstractNettyTcpRequest<Request extends ConfigurableNettyTcpRequest<Request>>
  implements ConfigurableNettyTcpRequest<Request> {
  protected final JsonRpcSender defaultJsonRpcSender;
  protected final SocketAddress defaultSocketAddress;
  protected final Jsonb jsonb;
  protected final Charset charset;
  protected final JsonRemoteProcedureSignature jsonRemoteProcedureSignature;

  public AbstractNettyTcpRequest(JsonRpcSender defaultJsonRpcSender, SocketAddress defaultSocketAddress,
                                 Jsonb defaultJsonb, Charset defaultCharset,
                                 JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    this.jsonRemoteProcedureSignature = jsonRemoteProcedureSignature;
    this.defaultJsonRpcSender = defaultJsonRpcSender;
    this.defaultSocketAddress = defaultSocketAddress;
    this.jsonb = defaultJsonb;
    this.charset = defaultCharset;
  }

  @Override
  public Jsonb jsonb() {
    return jsonb;
  }

  @Override
  public Charset charset() {
    return charset;
  }

  @Override
  public SocketAddress socketAddress() {
    return defaultSocketAddress;
  }
}
