package com.github.gibmir.ion.lib.netty.client.http.channel.appender;

import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.http.channel.codecs.HttpJsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.http.configuration.HttpResponseDecoderConfiguration;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public final class HttpClientChannelHandlerAppender implements ChannelHandlerAppender {
  public static final String HTTP_CLIENT_CODEC_NAME = "http-client-codec";
  public static final String HTTP_OBJECT_AGGREGATOR_NAME = "http-object-aggregator";
  public static final String HTTP_RESPONSE_DECODER_NAME = "http-response-decoder";
  public static final String RESPONSE_HANDLER_NAME = "response-handler";
  private final ResponseListenerRegistry responseListenerRegistry;
  private final Charset charset;
  private final Jsonb jsonb;
  private final HttpResponseDecoderConfiguration decoderConfiguration;
  private final int maxContentLength;
  private final Logger decoderLogger;

  public HttpClientChannelHandlerAppender(final ResponseListenerRegistry responseListenerRegistry, final Charset charset,
                                          final Jsonb jsonb, final HttpResponseDecoderConfiguration decoderConfiguration,
                                          final int maxContentLength, final Logger decoderLogger) {
    this.responseListenerRegistry = responseListenerRegistry;
    this.charset = charset;
    this.jsonb = jsonb;
    this.decoderConfiguration = decoderConfiguration;
    this.maxContentLength = maxContentLength;
    this.decoderLogger = decoderLogger;
  }

  @Override
  public void appendTo(final ChannelPipeline channelPipeline) {
    channelPipeline
      .addLast(HTTP_CLIENT_CODEC_NAME, new HttpClientCodec(decoderConfiguration.getMaxInitialLineLength(),
        decoderConfiguration.getMaxHeaderSize(), decoderConfiguration.getMaxChunkSize()))
      .addLast(HTTP_OBJECT_AGGREGATOR_NAME, new HttpObjectAggregator(maxContentLength))
      .addLast(HTTP_RESPONSE_DECODER_NAME, new HttpJsonRpcResponseDecoder(jsonb, charset, decoderLogger))
      //handler
      .addLast(RESPONSE_HANDLER_NAME, new JsonRpcResponseHandler(responseListenerRegistry));
  }
}
