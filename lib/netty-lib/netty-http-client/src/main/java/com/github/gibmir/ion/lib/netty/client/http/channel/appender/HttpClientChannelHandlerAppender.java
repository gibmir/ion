package com.github.gibmir.ion.lib.netty.client.http.channel.appender;

import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.http.channel.codecs.HttpJsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.http.configuration.HttpResponseDecoderConfiguration;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class HttpClientChannelHandlerAppender implements ChannelHandlerAppender {
  private final ResponseListenerRegistry responseListenerRegistry;
  private final Charset charset;
  private final Jsonb jsonb;
  private final HttpResponseDecoderConfiguration decoderConfiguration;
  private final int maxContentLength;

  public HttpClientChannelHandlerAppender(ResponseListenerRegistry responseListenerRegistry, Charset charset,
                                          Jsonb jsonb, HttpResponseDecoderConfiguration decoderConfiguration,
                                          int maxContentLength) {
    this.responseListenerRegistry = responseListenerRegistry;
    this.charset = charset;
    this.jsonb = jsonb;
    this.decoderConfiguration = decoderConfiguration;
    this.maxContentLength = maxContentLength;
  }

  @Override
  public void appendTo(ChannelPipeline channelPipeline) {
    channelPipeline
      .addLast("client codec", new HttpClientCodec(decoderConfiguration.getMaxInitialLineLength(),
        decoderConfiguration.getMaxHeaderSize(), decoderConfiguration.getMaxChunkSize()))
      .addLast("aggregator", new HttpObjectAggregator(maxContentLength))
      .addLast("http response decoder", new HttpJsonRpcResponseDecoder(jsonb, charset))
      //handler
      .addLast("response handler", new JsonRpcResponseHandler(responseListenerRegistry));
  }
}
