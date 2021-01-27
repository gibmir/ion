package com.github.gibmir.ion.lib.netty.server.http.channel.appender;

import com.github.gibmir.ion.api.server.cache.processor.ServerProcessor;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.server.common.channel.handler.JsonRpcRequestHandler;
import com.github.gibmir.ion.lib.netty.server.http.channel.codecs.HttpJsonRpcRequestDecoder;
import com.github.gibmir.ion.lib.netty.server.http.channel.codecs.HttpJsonRpcResponseEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class HttpJsonRpcServerChannelHandlerAppender implements ChannelHandlerAppender {
  private final ServerProcessor serverProcessor;
  private final Jsonb jsonb;
  private final Charset charset;

  public HttpJsonRpcServerChannelHandlerAppender(ServerProcessor serverProcessor, Jsonb jsonb, Charset charset) {
    this.serverProcessor = serverProcessor;
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  public void appendTo(ChannelPipeline channelPipeline) {
    channelPipeline.addLast(
      //decoders
      //Decodes incoming http request
      new HttpRequestDecoder(),
      //Consumes http request body and decode it to json
      new HttpJsonRpcRequestDecoder(jsonb, charset),
      //encoders
      //Encodes http response and send it
      new HttpResponseEncoder(),
      //Creates http response with json-rpc body
      new HttpJsonRpcResponseEncoder(),
      //Handle request
      new JsonRpcRequestHandler(serverProcessor, jsonb, charset));
  }
}
