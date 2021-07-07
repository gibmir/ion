package com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.List;

public final class JsonRpcResponseDecoder extends MessageToMessageDecoder<byte[]> {
  private final Jsonb jsonb;
  private final Charset charset;

  public JsonRpcResponseDecoder(final Jsonb jsonb, final Charset charset) {
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  protected void decode(final ChannelHandlerContext ctx, final byte[] msg, final List<Object> out) {
    out.add(jsonb.fromJson(new String(msg, charset), JsonValue.class));
  }
}
