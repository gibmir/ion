package com.github.gibmir.ion.lib.netty.client.request;

import com.github.gibmir.ion.lib.netty.client.JsonRpcNettyClient;

import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;

public abstract class AbstractNettyRequest<T, Request extends ConfigurableNettyRequest<Request>>
  implements ConfigurableNettyRequest<Request> {
  protected final Class<T> returnType;
  protected final String methodName;
  protected final JsonRpcNettyClient defaultJsonRpcNettyClient;
  protected SocketAddress socketAddress;
  protected Jsonb jsonb;
  protected Charset charset;

  public AbstractNettyRequest(Class<T> returnType, String methodName, JsonRpcNettyClient defaultJsonRpcNettyClient,
                              SocketAddress socketAddress, Jsonb defaultJsonb, Charset defaultCharset) {
    this.defaultJsonRpcNettyClient = defaultJsonRpcNettyClient;
    this.returnType = returnType;
    this.methodName = methodName;
    this.socketAddress = socketAddress;
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
    return socketAddress;
  }
}
