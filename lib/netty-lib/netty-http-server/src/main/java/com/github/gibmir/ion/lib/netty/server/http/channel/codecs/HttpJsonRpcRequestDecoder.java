package com.github.gibmir.ion.lib.netty.server.http.channel.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpContent;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.List;

public class HttpJsonRpcRequestDecoder extends MessageToMessageDecoder<HttpContent> {
  private final Jsonb jsonb;
  private final Charset charset;

  public HttpJsonRpcRequestDecoder(Jsonb jsonb, Charset charset) {
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, HttpContent msg, List<Object> out) {
    ByteBuf body = msg.content();
    int readableBytes = body.readableBytes();
    byte[] bodyPayload = new byte[readableBytes];
    body.readBytes(bodyPayload);
    out.add(jsonb.fromJson(new String(bodyPayload, charset), JsonValue.class));
  }
}
