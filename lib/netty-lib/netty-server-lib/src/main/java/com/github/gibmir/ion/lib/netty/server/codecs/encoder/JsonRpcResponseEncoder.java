package com.github.gibmir.ion.lib.netty.server.codecs.encoder;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class JsonRpcResponseEncoder extends MessageToByteEncoder<JsonRpcResponse> {
  private final Jsonb jsonb;
  private final Charset charset;

  public JsonRpcResponseEncoder(Jsonb jsonb, Charset charset) {
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, JsonRpcResponse msg, ByteBuf out) {
    out.writeBytes(jsonb.toJson(msg).getBytes(charset));
  }
}
