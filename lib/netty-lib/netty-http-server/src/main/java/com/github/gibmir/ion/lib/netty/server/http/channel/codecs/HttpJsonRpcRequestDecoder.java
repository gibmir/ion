package com.github.gibmir.ion.lib.netty.server.http.channel.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.HttpContent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.List;

public class HttpJsonRpcRequestDecoder extends MessageToMessageDecoder<HttpContent> {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpJsonRpcRequestDecoder.class);
  private final Jsonb jsonb;
  private final Charset charset;

  public HttpJsonRpcRequestDecoder(Jsonb jsonb, Charset charset) {
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, HttpContent msg, List<Object> out) {
    ByteBuf body = msg.content();
    int readableBytes = body.readableBytes();
    if (readableBytes > 0) {
      byte[] bodyPayload = new byte[readableBytes];
      body.readBytes(bodyPayload);
      LOGGER.debug("HTTP request body with size [{}] was read", readableBytes);
      out.add(jsonb.fromJson(new String(bodyPayload, charset), JsonValue.class));
    }
  }
}
