package com.github.gibmir.ion.lib.netty.client.sender.initializer;

import com.github.gibmir.ion.lib.netty.client.sender.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.sender.handler.JsonRpcResponseHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class JsonRpcNettyClientInitializer extends ChannelInitializer<SocketChannel> {
  private final JsonRpcRequestEncoder jsonRpcRequestEncoder;
  private final JsonRpcResponseDecoder jsonRpcResponseDecoder;

  public JsonRpcNettyClientInitializer(JsonRpcRequestEncoder jsonRpcRequestEncoder, JsonRpcResponseDecoder jsonRpcResponseDecoder) {
    this.jsonRpcRequestEncoder = jsonRpcRequestEncoder;
    this.jsonRpcResponseDecoder = jsonRpcResponseDecoder;
  }

  @Override
  protected void initChannel(SocketChannel ch) {
    ch.pipeline().addLast(new LoggingHandler(LogLevel.TRACE)).addLast(jsonRpcRequestEncoder)
      .addLast(jsonRpcResponseDecoder).addLast(new JsonRpcResponseHandler());
  }

  public JsonRpcRequestEncoder getJsonRpcRequestEncoder() {
    return jsonRpcRequestEncoder;
  }

  public JsonRpcResponseDecoder getJsonRpcResponseDecoder() {
    return jsonRpcResponseDecoder;
  }
}
