package com.github.gibmir.ion.lib.netty.server.common.channel.handler.appender;

import com.github.gibmir.ion.api.server.cache.processor.ServerProcessor;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.server.common.channel.codecs.decoder.JsonRpcRequestDecoder;
import com.github.gibmir.ion.lib.netty.server.common.channel.codecs.encoder.JsonRpcResponseEncoder;
import com.github.gibmir.ion.lib.netty.server.common.channel.handler.JsonRpcRequestHandler;
import io.netty.channel.ChannelPipeline;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class JsonRpcServerChannelHandlerAppender implements ChannelHandlerAppender {
  private final ServerProcessor serverProcessor;
  private final Jsonb jsonb;
  private final Charset charset;

  public JsonRpcServerChannelHandlerAppender(ServerProcessor serverProcessor, Charset charset, Jsonb jsonb) {
    this.serverProcessor = serverProcessor;
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  public void appendTo(ChannelPipeline channelPipeline) {
    channelPipeline.addLast(new JsonRpcRequestDecoder(jsonb, charset), new JsonRpcResponseEncoder(),
      new JsonRpcRequestHandler(serverProcessor, jsonb, charset));
  }
}
