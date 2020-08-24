package com.github.gibmir.ion.lib.netty.client.sender.initializer;

import com.github.gibmir.ion.lib.netty.client.sender.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.sender.handler.JsonRpcResponseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;

import java.util.ArrayList;
import java.util.List;

public class JsonRpcNettyChannelInitializer extends ChannelInitializer<Channel> {
  private final ChannelHandler[] channelHandlers;

  public static class Builder {
    private final JsonRpcRequestEncoder jsonRpcRequestEncoder;
    private final JsonRpcResponseDecoder jsonRpcResponseDecoder;
    private final JsonRpcResponseHandler jsonRpcResponseHandler;
    private LoggingHandler loggingHandler;
    private SslHandler sslHandler;

    public Builder(JsonRpcRequestEncoder jsonRpcRequestEncoder,
                   JsonRpcResponseDecoder jsonRpcResponseDecoder,
                   JsonRpcResponseHandler jsonRpcResponseHandler) {
      this.jsonRpcRequestEncoder = jsonRpcRequestEncoder;
      this.jsonRpcResponseDecoder = jsonRpcResponseDecoder;
      this.jsonRpcResponseHandler = jsonRpcResponseHandler;
    }

    public Builder withLogging(LoggingHandler loggingHandler) {
      this.loggingHandler = loggingHandler;
      return this;
    }

    public Builder withSsl(SslHandler sslHandler) {
      this.sslHandler = sslHandler;
      return this;
    }

    public JsonRpcNettyChannelInitializer build() {
      List<ChannelHandler> builderHandlers = new ArrayList<>();
      if (sslHandler != null) {
        builderHandlers.add(sslHandler);
      }
      if (loggingHandler != null) {
        builderHandlers.add(loggingHandler);
      }
      builderHandlers.add(jsonRpcRequestEncoder);
      builderHandlers.add(jsonRpcResponseDecoder);
      builderHandlers.add(jsonRpcResponseHandler);
      ChannelHandler[] channelHandlers = new ChannelHandler[builderHandlers.size()];
      return new JsonRpcNettyChannelInitializer(builderHandlers.toArray(channelHandlers));
    }
  }

  private JsonRpcNettyChannelInitializer(ChannelHandler... channelHandlers) {
    this.channelHandlers = channelHandlers;
  }
  //todo redo usage in tests on builder
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

  public static Builder builder(JsonRpcRequestEncoder jsonRpcRequestEncoder,
                                JsonRpcResponseDecoder jsonRpcResponseDecoder,
                                JsonRpcResponseHandler jsonRpcResponseHandler) {
    return new Builder(jsonRpcRequestEncoder, jsonRpcResponseDecoder, jsonRpcResponseHandler);
  }

  @Override
  protected void initChannel(Channel ch) {
    ch.pipeline().addLast(channelHandlers);
  }
}
