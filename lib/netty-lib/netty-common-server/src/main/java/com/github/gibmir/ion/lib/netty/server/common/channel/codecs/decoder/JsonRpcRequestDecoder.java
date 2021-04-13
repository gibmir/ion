package com.github.gibmir.ion.lib.netty.server.common.channel.codecs.decoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Decodes {@code byte[]} into {@link JsonValue} with configured {@link Jsonb}
 */
public class JsonRpcRequestDecoder extends MessageToMessageDecoder<byte[]> {
  private final Jsonb jsonb;
  private final Charset charset;

  public JsonRpcRequestDecoder(Jsonb jsonb, Charset charset) {
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) {
    out.add(jsonb.fromJson(new String(msg, charset), JsonValue.class));
  }
}
