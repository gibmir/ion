package com.github.gibmir.ion.lib.netty.server.common.channel.codecs.encoder;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.List;

public final class ResponseEncoder extends MessageToMessageEncoder<JsonRpcResponse> {
  private final Logger logger;
  private final Jsonb jsonb;
  private final Charset charset;

  public ResponseEncoder(final Logger logger, final Jsonb jsonb, final Charset charset) {
    this.logger = logger;
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  protected void encode(final ChannelHandlerContext ctx, final JsonRpcResponse msg, final List<Object> out) {
    msg.processWith(new NettyJsonRpcResponseProcessor(logger, jsonb, charset, out));
  }
}
