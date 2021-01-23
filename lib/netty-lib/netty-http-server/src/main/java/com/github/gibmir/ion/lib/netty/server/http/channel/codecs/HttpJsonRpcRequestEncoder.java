package com.github.gibmir.ion.lib.netty.server.http.channel.codecs;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.util.List;

public class HttpJsonRpcRequestEncoder extends MessageToMessageEncoder<byte[]> {

  @Override
  protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) {
    FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK,
      Unpooled.wrappedBuffer(msg));
    out.add(fullHttpResponse);
  }
}
