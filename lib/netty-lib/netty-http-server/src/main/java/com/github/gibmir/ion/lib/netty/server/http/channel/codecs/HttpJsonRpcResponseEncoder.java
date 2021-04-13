package com.github.gibmir.ion.lib.netty.server.http.channel.codecs;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HttpJsonRpcResponseEncoder extends MessageToMessageEncoder<byte[]> {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpJsonRpcResponseEncoder.class);

  @Override
  protected void encode(ChannelHandlerContext ctx, byte[] msg, List<Object> out) {
    FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    fullHttpResponse.content().clear().writeBytes(msg);
    fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH, msg.length);
    fullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json");
    LOGGER.debug("Http message was successfully created");
    out.add(fullHttpResponse);
  }
}
