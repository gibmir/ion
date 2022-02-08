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

public final class TcpServerChannelHandlerAppender implements ChannelHandlerAppender {
  public static final String FRAME_DECODER_NAME = "frameDecoder";
  public static final String BYTES_DECODER_NAME = "bytesDecoder";
  public static final String JSON_DECODER_NAME = "jsonDecoder";
  public static final String MESSAGE_DECODER_NAME = "messageDecoder";
  public static final String FRAME_ENCODER_NAME = "frameEncoder";
  public static final String BYTES_ENCODER_NAME = "bytesEncoder";
  public static final String RESPONSE_ENCODER_NAME = "responseEncoder";
  public static final String JSON_RPC_REQUEST_HANDLER_NAME = "jsonRpcRequestHandler";
  private final ServerProcessor serverProcessor;
  private final Jsonb jsonb;
  private final Charset charset;
  private final FrameDecoderConfig frameDecoderConfig;
  private final int lengthFieldLength;
  private final Logger responseEncoderLogger;

  public TcpServerChannelHandlerAppender(final ServerProcessor serverProcessor, final Charset charset,
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
      .addLast(FRAME_DECODER_NAME, new LengthFieldBasedFrameDecoder(frameDecoderConfig.getMaxFrameLength(),
        frameDecoderConfig.getLengthFieldOffset(), frameDecoderConfig.getLengthFieldLength(),
        frameDecoderConfig.getLengthAdjustment(), frameDecoderConfig.getInitialBytesToStrip()))
      .addLast(BYTES_DECODER_NAME, new ByteArrayDecoder())
      .addLast(JSON_DECODER_NAME, new JsonRpcRequestDecoder(jsonb, charset))
      .addLast(MESSAGE_DECODER_NAME, new RequestMessageDecoder())
      //encoder
      .addLast(FRAME_ENCODER_NAME, new LengthFieldPrepender(lengthFieldLength))
      .addLast(BYTES_ENCODER_NAME, new ByteArrayEncoder())
      .addLast(RESPONSE_ENCODER_NAME, new ResponseEncoder(responseEncoderLogger, jsonb, charset))
      //handler
      .addLast(JSON_RPC_REQUEST_HANDLER_NAME, new RequestMessageHandler(serverProcessor));
  }
}
