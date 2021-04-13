package com.github.gibmir.ion.lib.netty.client.channel.appender;

import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.common.configuration.decoder.FrameDecoderConfig;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class TcpClientChannelHandlerAppender implements ChannelHandlerAppender {
  private final ResponseListenerRegistry responseListenerRegistry;
  private final Charset charset;
  private final Jsonb jsonb;
  private final FrameDecoderConfig frameDecoderConfig;
  private final int lengthFieldLength;

  public TcpClientChannelHandlerAppender(ResponseListenerRegistry responseListenerRegistry, Charset charset,
                                         Jsonb jsonb, FrameDecoderConfig frameDecoderConfig, int lengthFieldLength) {
    this.responseListenerRegistry = responseListenerRegistry;
    this.charset = charset;
    this.jsonb = jsonb;
    this.frameDecoderConfig = frameDecoderConfig;
    this.lengthFieldLength = lengthFieldLength;
  }

  @Override
  public void appendTo(ChannelPipeline channelPipeline) {
    channelPipeline
      //decoder
      .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(frameDecoderConfig.getMaxFrameLength(),
        frameDecoderConfig.getLengthFieldOffset(), frameDecoderConfig.getLengthFieldLength(),
        frameDecoderConfig.getLengthAdjustment(), frameDecoderConfig.getInitialBytesToStrip()))
      .addLast("bytesDecoder", new ByteArrayDecoder())
      .addLast(new JsonRpcResponseDecoder(jsonb, charset))
      //encoder
      .addLast("frameEncoder", new LengthFieldPrepender(lengthFieldLength))
      .addLast("bytesEncoder", new ByteArrayEncoder())
      //handler
      .addLast("jsonRpcRequestHandler", new JsonRpcResponseHandler(responseListenerRegistry));
  }
}
