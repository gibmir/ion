package com.github.gibmir.ion.lib.netty.server.appender;

import com.github.gibmir.ion.lib.netty.server.common.processor.ServerProcessor;
import com.github.gibmir.ion.lib.netty.common.channel.initializer.appender.ChannelHandlerAppender;
import com.github.gibmir.ion.lib.netty.common.configuration.decoder.FrameDecoderConfig;
import com.github.gibmir.ion.lib.netty.server.common.channel.codecs.decoder.JsonRpcRequestDecoder;
import com.github.gibmir.ion.lib.netty.server.common.channel.codecs.decoder.RequestMessageDecoder;
import com.github.gibmir.ion.lib.netty.server.common.channel.codecs.encoder.ResponseEncoder;
import com.github.gibmir.ion.lib.netty.server.common.channel.handler.RequestMessageHandler;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public final class JsonRpcServerChannelHandlerAppender implements ChannelHandlerAppender {
  private final ServerProcessor serverProcessor;
  private final Jsonb jsonb;
  private final Charset charset;
  private final FrameDecoderConfig frameDecoderConfig;
  private final int lengthFieldLength;
  private final Logger responseEncoderLogger;

  public JsonRpcServerChannelHandlerAppender(final ServerProcessor serverProcessor, final Charset charset,
                                             final Jsonb jsonb, final FrameDecoderConfig frameDecoderConfig,
                                             final int encoderFieldLength, final Logger responseEncoderLogger) {
    this.serverProcessor = serverProcessor;
    this.jsonb = jsonb;
    this.charset = charset;
    this.frameDecoderConfig = frameDecoderConfig;
    this.lengthFieldLength = encoderFieldLength;
    this.responseEncoderLogger = responseEncoderLogger;
  }

  @Override
  public void appendTo(final ChannelPipeline channelPipeline) {
    channelPipeline
      //decoder
      .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(frameDecoderConfig.getMaxFrameLength(),
        frameDecoderConfig.getLengthFieldOffset(), frameDecoderConfig.getLengthFieldLength(),
        frameDecoderConfig.getLengthAdjustment(), frameDecoderConfig.getInitialBytesToStrip()))
      .addLast("bytesDecoder", new ByteArrayDecoder())
      .addLast("jsonDecoder", new JsonRpcRequestDecoder(jsonb, charset))
      .addLast("messageDecoder", new RequestMessageDecoder())
      //encoder
      .addLast("frameEncoder", new LengthFieldPrepender(lengthFieldLength))
      .addLast("bytesEncoder", new ByteArrayEncoder())
      .addLast("responseEncoder", new ResponseEncoder(responseEncoderLogger, jsonb, charset))
      //handler
      .addLast("jsonRpcRequestHandler", new RequestMessageHandler(serverProcessor));
  }
}
