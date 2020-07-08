package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.lib.netty.client.sender.JsonRpcNettySender;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public abstract class AbstractNettyRequest<T, Request extends ConfigurableNettyRequest<Request>>
  implements ConfigurableNettyRequest<Request> {
  protected final Class<T> returnType;
  protected final String procedureName;
  protected final JsonRpcNettySender defaultJsonRpcNettySender;
  protected SocketAddress defaultSocketAddress;
  protected Jsonb jsonb;
  protected Charset charset;

  public AbstractNettyRequest(Class<T> returnType, String procedureName, JsonRpcNettySender defaultJsonRpcNettySender,
                              SocketAddress defaultSocketAddress, Jsonb defaultJsonb, Charset defaultCharset) {
    this.defaultJsonRpcNettySender = defaultJsonRpcNettySender;
    this.returnType = returnType;
    this.procedureName = procedureName;
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
