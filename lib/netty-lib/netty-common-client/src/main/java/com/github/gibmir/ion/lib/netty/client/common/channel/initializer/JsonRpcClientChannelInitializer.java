package com.github.gibmir.ion.lib.netty.client.common.channel.initializer;

import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslHandler;

import java.util.ArrayList;
import java.util.List;

public final class JsonRpcClientChannelInitializer extends ChannelInitializer<Channel> {
  private final ChannelHandler[] channelHandlers;

  public static class Builder {
    private final JsonRpcRequestEncoder jsonRpcRequestEncoder;
    private final JsonRpcResponseDecoder jsonRpcResponseDecoder;
    private final JsonRpcResponseHandler jsonRpcResponseHandler;
    private LoggingHandler loggingHandler;
    private SslHandler sslHandler;

    public Builder(final JsonRpcRequestEncoder jsonRpcRequestEncoder,
                   final JsonRpcResponseDecoder jsonRpcResponseDecoder,
                   final JsonRpcResponseHandler jsonRpcResponseHandler) {
      this.jsonRpcRequestEncoder = jsonRpcRequestEncoder;
      this.jsonRpcResponseDecoder = jsonRpcResponseDecoder;
      this.jsonRpcResponseHandler = jsonRpcResponseHandler;
    }

    /**
     * Sets specified logging handler.
     *
     * @param loggingHandler logging handler
     * @return this
     */
    public final Builder withLogging(final LoggingHandler loggingHandler) {
      this.loggingHandler = loggingHandler;
      return this;
    }

    /**
     * Sets specified ssl handler.
     *
     * @param sslHandler ssl handler
     * @return this
     */
    public final Builder withSsl(final SslHandler sslHandler) {
      this.sslHandler = sslHandler;
      return this;
    }

    /**
     * @return channel initializer with configured handlers
     */
    public JsonRpcClientChannelInitializer build() {
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
      return new JsonRpcClientChannelInitializer(builderHandlers.toArray(channelHandlers));
    }
  }

  private JsonRpcClientChannelInitializer(final ChannelHandler... channelHandlers) {
    this.channelHandlers = channelHandlers;
  }

  /**
   * @param jsonRpcRequestEncoder  encoder
   * @param jsonRpcResponseDecoder decoder
   * @param jsonRpcResponseHandler handler
   * @return builder
   */
  public static Builder builder(final JsonRpcRequestEncoder jsonRpcRequestEncoder,
                                final JsonRpcResponseDecoder jsonRpcResponseDecoder,
                                final JsonRpcResponseHandler jsonRpcResponseHandler) {
    return new Builder(jsonRpcRequestEncoder, jsonRpcResponseDecoder, jsonRpcResponseHandler);
  }

  @Override
  protected void initChannel(final Channel ch) {
    ch.pipeline().addLast(channelHandlers);
  }
}
