package com.github.gibmir.ion.lib.netty.client.sender.initializer;

import com.github.gibmir.ion.lib.netty.client.sender.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.sender.handler.JsonRpcResponseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.logging.LoggingHandler;

public class JsonRpcNettyChannelInitializer extends ChannelInitializer<Channel> {
  private final ChannelHandler[] channelHandlers;


  private JsonRpcNettyChannelInitializer(ChannelHandler... channelHandlers) {
    this.channelHandlers = channelHandlers;
  }

  public static JsonRpcNettyChannelInitializer withLogging(LoggingHandler loggingHandler,
                                                           JsonRpcRequestEncoder jsonRpcRequestEncoder,
                                                           JsonRpcResponseDecoder jsonRpcResponseDecoder,
                                                           JsonRpcResponseHandler jsonRpcResponseHandler) {
    return new JsonRpcNettyChannelInitializer(loggingHandler, jsonRpcRequestEncoder, jsonRpcResponseDecoder, jsonRpcResponseHandler);
  }

  public static JsonRpcNettyChannelInitializer withoutLogging(JsonRpcRequestEncoder jsonRpcRequestEncoder,
                                                              JsonRpcResponseDecoder jsonRpcResponseDecoder,
                                                              JsonRpcResponseHandler jsonRpcResponseHandler) {
    return new JsonRpcNettyChannelInitializer(jsonRpcRequestEncoder, jsonRpcResponseDecoder, jsonRpcResponseHandler);
  }

  @Override
  protected void initChannel(Channel ch) {
    ch.pipeline().addLast(channelHandlers);
  }
}
