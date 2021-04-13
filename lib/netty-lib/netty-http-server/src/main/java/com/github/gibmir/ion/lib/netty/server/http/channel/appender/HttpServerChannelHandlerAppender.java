package com.github.gibmir.ion.lib.netty.server.http.channel.appender;

import com.github.gibmir.ion.api.server.cache.processor.ServerProcessor;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.server.common.channel.codecs.decoder.JsonRpcRequestDecoder;
import com.github.gibmir.ion.lib.netty.server.common.channel.codecs.decoder.RequestMessageDecoder;
import com.github.gibmir.ion.lib.netty.server.common.channel.codecs.encoder.ResponseEncoder;
import com.github.gibmir.ion.lib.netty.server.common.channel.handler.RequestMessageHandler;
import com.github.gibmir.ion.lib.netty.server.http.channel.codecs.HttpBodyDecoder;
import com.github.gibmir.ion.lib.netty.server.http.channel.codecs.HttpJsonRpcResponseEncoder;
import com.github.gibmir.ion.lib.netty.server.http.configuration.HttpRequestDecoderConfiguration;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class HttpServerChannelHandlerAppender implements ChannelHandlerAppender {
  private final ServerProcessor serverProcessor;
  private final Jsonb jsonb;
  private final Charset charset;
  private final Logger responseEncoderLogger;
  private final int aggregatorMaxContentLength;
  private final HttpRequestDecoderConfiguration httpRequestDecoderConfiguration;

  public HttpServerChannelHandlerAppender(ServerProcessor serverProcessor, Jsonb jsonb,
                                          Charset charset, Logger responseEncoderLogger, int aggregatorMaxContentLength,
                                          HttpRequestDecoderConfiguration httpRequestDecoderConfiguration) {
    this.serverProcessor = serverProcessor;
    this.jsonb = jsonb;
    this.charset = charset;
    this.responseEncoderLogger = responseEncoderLogger;
    this.aggregatorMaxContentLength = aggregatorMaxContentLength;
    this.httpRequestDecoderConfiguration = httpRequestDecoderConfiguration;
  }

  @Override
  public void appendTo(ChannelPipeline channelPipeline) {
    channelPipeline
      //decoders
      //Decodes incoming http request
      .addLast("http decoder", new HttpRequestDecoder(httpRequestDecoderConfiguration.getMaxInitialLineLength(),
        httpRequestDecoderConfiguration.getMaxHeaderSize(), httpRequestDecoderConfiguration.getMaxChunkSize()))
      .addLast("body aggregator", new HttpObjectAggregator(aggregatorMaxContentLength))
      //Consumes http request body and decode it to json
      .addLast("http body decoder", new HttpBodyDecoder())
      .addLast("request decoder", new JsonRpcRequestDecoder(jsonb, charset))
      .addLast("request message decoder", new RequestMessageDecoder())
      //encoders
      //Encodes http response and send it
      .addLast("netty http response encoder", new HttpResponseEncoder())
      //Creates http response with json-rpc body
      .addLast("http response encoder", new HttpJsonRpcResponseEncoder())
      .addLast("response encoder", new ResponseEncoder(responseEncoderLogger, jsonb, charset))
      //Handler request
      .addLast("handler", new RequestMessageHandler(serverProcessor));

  }
}
