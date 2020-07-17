package com.github.gibmir.ion.lib.netty.server.codecs.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import javax.json.JsonStructure;
import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.List;

public class JsonRpcRequestDecoder extends ReplayingDecoder<JsonStructure> {
  private final Jsonb jsonb;
  private final Charset charset;

  public JsonRpcRequestDecoder(Jsonb jsonb, Charset charset) {
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
    byte[] bytes = new byte[in.writerIndex()];
    in.readBytes(bytes);
    JsonStructure jsonStructure = jsonb.fromJson(new String(bytes, charset), JsonStructure.class);
    out.add(jsonStructure);
  }
}
