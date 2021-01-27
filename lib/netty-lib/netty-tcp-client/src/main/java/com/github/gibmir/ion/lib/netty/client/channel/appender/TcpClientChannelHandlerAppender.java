package com.github.gibmir.ion.lib.netty.client.channel.appender;

import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import io.netty.channel.ChannelPipeline;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class TcpClientChannelHandlerAppender implements ChannelHandlerAppender {
  private final ResponseListenerRegistry responseListenerRegistry;
  private final Charset charset;
  private final Jsonb jsonb;

  public TcpClientChannelHandlerAppender(ResponseListenerRegistry responseListenerRegistry, Charset charset,
                                         Jsonb jsonb) {
    this.responseListenerRegistry = responseListenerRegistry;
    this.charset = charset;
    this.jsonb = jsonb;
  }

  @Override
  public void appendTo(ChannelPipeline channelPipeline) {
    channelPipeline.addLast(new JsonRpcRequestEncoder(), new JsonRpcResponseDecoder(jsonb, charset),
      new JsonRpcResponseHandler(jsonb, responseListenerRegistry));
  }
}
