package com.github.gibmir.ion.lib.netty.client.sender.initializer;

import com.github.gibmir.ion.lib.netty.client.sender.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.sender.handler.JsonRpcResponseHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class JsonRpcNettyClientInitializer extends ChannelInitializer<SocketChannel> {
  private final LoggingHandler loggingHandler;
  private final JsonRpcRequestEncoder jsonRpcRequestEncoder;
  private final JsonRpcResponseDecoder jsonRpcResponseDecoder;

  public JsonRpcNettyClientInitializer(LoggingHandler loggingHandler, JsonRpcRequestEncoder jsonRpcRequestEncoder,
                                       JsonRpcResponseDecoder jsonRpcResponseDecoder) {
    this.loggingHandler = loggingHandler;
    this.jsonRpcRequestEncoder = jsonRpcRequestEncoder;
    this.jsonRpcResponseDecoder = jsonRpcResponseDecoder;
  }

  @Override
  protected void initChannel(SocketChannel ch) {
    ch.pipeline().addLast(loggingHandler).addLast(jsonRpcRequestEncoder)
      .addLast(jsonRpcResponseDecoder).addLast(new JsonRpcResponseHandler());
  }
}
