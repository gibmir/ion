package com.github.gibmir.ion.lib.netty.client.tcp.appender;

import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder.JsonRpcResponseDecoder;
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
  public static final String FRAME_DECODER_NAME = "frameDecoder";
  public static final String BYTES_DECODER_NAME = "bytesDecoder";
  public static final String JSON_RPC_RESPONSE_DECODER_NAME = "jsonRpcResponseDecoder";
  public static final String FRAME_ENCODER_NAME = "frameEncoder";
  public static final String BYTES_ENCODER_NAME = "bytesEncoder";
  public static final String JSON_RPC_REQUEST_HANDLER_NAME = "jsonRpcRequestHandler";
  private final ResponseListenerRegistry responseListenerRegistry;
  private final Charset charset;
  private final Jsonb jsonb;
  private final FrameDecoderConfig frameDecoderConfig;
  private final int lengthFieldLength;

  public TcpClientChannelHandlerAppender(final ResponseListenerRegistry responseListenerRegistry, final Charset charset,
                                         final Jsonb jsonb, final FrameDecoderConfig frameDecoderConfig,
                                         final int lengthFieldLength) {
    this.responseListenerRegistry = responseListenerRegistry;
    this.charset = charset;
    this.jsonb = jsonb;
    this.frameDecoderConfig = frameDecoderConfig;
    this.lengthFieldLength = lengthFieldLength;
  }

  @Override
  public final void appendTo(final ChannelPipeline channelPipeline) {
    channelPipeline
      //decoder
      .addLast(FRAME_DECODER_NAME, new LengthFieldBasedFrameDecoder(frameDecoderConfig.getMaxFrameLength(),
        frameDecoderConfig.getLengthFieldOffset(), frameDecoderConfig.getLengthFieldLength(),
        frameDecoderConfig.getLengthAdjustment(), frameDecoderConfig.getInitialBytesToStrip()))
      .addLast(BYTES_DECODER_NAME, new ByteArrayDecoder())
      .addLast(JSON_RPC_RESPONSE_DECODER_NAME, new JsonRpcResponseDecoder(jsonb, charset))
      //encoder
      .addLast(FRAME_ENCODER_NAME, new LengthFieldPrepender(lengthFieldLength))
      .addLast(BYTES_ENCODER_NAME, new ByteArrayEncoder())
      //handler
      .addLast(JSON_RPC_REQUEST_HANDLER_NAME, new JsonRpcResponseHandler(responseListenerRegistry));
  }
}
