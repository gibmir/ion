package com.github.gibmir.ion.lib.netty.client.http.channel.appender;

import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.http.channel.codecs.HttpJsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class HttpClientChannelHandlerAppender implements ChannelHandlerAppender {
  private final ResponseListenerRegistry responseListenerRegistry;
  private final Charset charset;
  private final Jsonb jsonb;

  public HttpClientChannelHandlerAppender(ResponseListenerRegistry responseListenerRegistry, Charset charset, Jsonb jsonb) {
    this.responseListenerRegistry = responseListenerRegistry;
    this.charset = charset;
    this.jsonb = jsonb;
  }

  @Override
  public void appendTo(ChannelPipeline channelPipeline) {
    channelPipeline.addLast(new HttpRequestEncoder(),
      new HttpResponseDecoder(), new HttpJsonRpcResponseDecoder(jsonb, charset),
      new JsonRpcResponseHandler(jsonb, responseListenerRegistry));
  }
}
