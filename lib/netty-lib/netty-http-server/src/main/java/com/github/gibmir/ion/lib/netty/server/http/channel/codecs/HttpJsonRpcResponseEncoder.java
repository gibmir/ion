package com.github.gibmir.ion.lib.netty.server.http.channel.codecs;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

import java.util.List;

public class HttpJsonRpcResponseEncoder extends MessageToMessageEncoder<byte[]> {

  @Override
  protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) {
    FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    fullHttpResponse.content().clear().writeBytes(msg);
    fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, msg.length);
    fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
    out.add(fullHttpResponse);
  }
}
