package com.github.gibmir.ion.lib.netty.client.sender.codecs.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class JsonRpcRequestEncoder extends MessageToByteEncoder<byte[]> {

  public JsonRpcRequestEncoder() {
  }

  @Override
  protected void encode(ChannelHandlerContext ctx, byte[] msg, ByteBuf out) {
    out.writeBytes(msg);
  }
}
