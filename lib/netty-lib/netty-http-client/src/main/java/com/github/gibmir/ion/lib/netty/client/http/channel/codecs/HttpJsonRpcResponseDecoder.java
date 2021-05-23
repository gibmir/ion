package com.github.gibmir.ion.lib.netty.client.http.channel.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.http.FullHttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.List;

public class HttpJsonRpcResponseDecoder extends MessageToMessageDecoder<FullHttpResponse> {
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpJsonRpcResponseDecoder.class);
  private final Jsonb jsonb;
  private final Charset charset;

  public HttpJsonRpcResponseDecoder(Jsonb jsonb, Charset charset) {
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, FullHttpResponse msg, List<Object> out) {
    ByteBuf body = msg.content();
    int readableBytes = body.readableBytes();
    LOGGER.debug("Response body is empty");
    if (readableBytes > 0) {
      byte[] bodyPayload = new byte[readableBytes];
      body.readBytes(bodyPayload);
      LOGGER.debug("HTTP response body with size [{}] was read", readableBytes);
      out.add(jsonb.fromJson(new String(bodyPayload, charset), JsonValue.class));
    }
  }
}
