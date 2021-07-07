package com.github.gibmir.ion.lib.netty.client.common.channel.codecs.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public final class JsonRpcRequestEncoder extends MessageToByteEncoder<byte[]> {

  @Override
  protected void encode(final ChannelHandlerContext ctx, final byte[] msg, final ByteBuf out) {
    int writerIndex = out.writerIndex();
    out.setBytes(writerIndex, msg);
    out.writerIndex(writerIndex + msg.length);
  }
}
