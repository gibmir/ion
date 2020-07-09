package com.github.gibmir.ion.lib.netty.client.sender.codecs.encoder;

import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class JsonRpcRequestEncoder extends MessageToByteEncoder<JsonRpcRequest> {

  private final Jsonb jsonb;
  private final Charset charset;

  public JsonRpcRequestEncoder(Jsonb jsonb, Charset charset) {
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, JsonRpcRequest msg, ByteBuf out) {
    byte[] payload = jsonb.toJson(msg).getBytes(charset);
    out.writeBytes(payload);

//    ctx.flush();
  }
}
