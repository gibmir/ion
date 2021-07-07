package com.github.gibmir.ion.lib.netty.client.http.request;

import com.github.gibmir.ion.scanner.signature.JsonRemoteProcedureSignature;
import com.github.gibmir.ion.lib.netty.client.http.sender.NettyHttpJsonRpcSender;

import javax.json.bind.Jsonb;
import java.net.URI;
import java.nio.charset.Charset;

public abstract class AbstractNettyHttpRequest<Request extends ConfigurableNettyHttpRequest<Request>>
  implements ConfigurableNettyHttpRequest<Request> {
  protected final NettyHttpJsonRpcSender defaultJsonRpcSender;
  protected final URI uri;
  protected final Jsonb jsonb;
  protected final Charset charset;
  protected final JsonRemoteProcedureSignature jsonRemoteProcedureSignature;


  protected AbstractNettyHttpRequest(final NettyHttpJsonRpcSender defaultJsonRpcSender, final URI defaultUri,
                                     final Jsonb jsonb, final Charset charset,
                                     final JsonRemoteProcedureSignature jsonRemoteProcedureSignature) {
    this.defaultJsonRpcSender = defaultJsonRpcSender;
    this.uri = defaultUri;
    this.jsonb = jsonb;
    this.charset = charset;
    this.jsonRemoteProcedureSignature = jsonRemoteProcedureSignature;
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
  public final URI uri() {
    return uri;
  }
}
