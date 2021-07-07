package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.scanner.signature.JsonRemoteProcedureSignature;
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

  public AbstractNettyTcpRequest(final JsonRpcSender defaultJsonRpcSender, final SocketAddress defaultSocketAddress,
                                 final Jsonb defaultJsonb, final Charset defaultCharset,
                                 final JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    this.jsonRemoteProcedureSignature = jsonRemoteProcedureSignature;
    this.defaultJsonRpcSender = defaultJsonRpcSender;
    this.defaultSocketAddress = defaultSocketAddress;
    this.jsonb = defaultJsonb;
    this.charset = defaultCharset;
  }

  @Override
  public final Jsonb jsonb() {
    return jsonb;
  }

  @Override
  public final Charset charset() {
    return charset;
  }

  @Override
  public final SocketAddress socketAddress() {
    return defaultSocketAddress;
  }
}
