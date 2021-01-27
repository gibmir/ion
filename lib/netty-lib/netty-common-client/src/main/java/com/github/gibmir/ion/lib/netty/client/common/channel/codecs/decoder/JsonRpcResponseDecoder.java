package com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.List;

public class JsonRpcResponseDecoder extends ReplayingDecoder<JsonValue> {
  private final Jsonb jsonb;
  private final Charset charset;

  public JsonRpcResponseDecoder(Jsonb jsonb, Charset charset) {
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
    byte[] bytes = new byte[in.writerIndex()];
    in.readBytes(bytes);
    out.add(jsonb.fromJson(new String(bytes, charset), JsonValue.class));
  }
}