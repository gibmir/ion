package com.github.gibmir.ion.lib.netty.server.http.channel.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class HttpBodyDecoder extends MessageToMessageDecoder<FullHttpRequest> {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpBodyDecoder.class);

  @Override
  protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out) {
    ByteBuf body = msg.content();
    int readableBytes = body.readableBytes();
    if (readableBytes > 0) {
      byte[] bodyPayload = new byte[readableBytes];
      body.readBytes(bodyPayload);
      LOGGER.debug("HTTP request body with size [{}] was read", readableBytes);
      out.add(bodyPayload);
    } else {
      LOGGER.debug("HTTP request without body");
    }
  }
}
