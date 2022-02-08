package com.github.gibmir.ion.lib.netty.server.appender;

import com.github.gibmir.ion.lib.netty.common.configuration.decoder.FrameDecoderConfig;
import com.github.gibmir.ion.lib.netty.server.common.channel.codecs.decoder.JsonRpcRequestDecoder;
import com.github.gibmir.ion.lib.netty.server.common.channel.handler.RequestMessageHandler;
import com.github.gibmir.ion.lib.netty.server.common.processor.ServerProcessor;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TcpServerChannelHandlerAppenderTest {
  @Test
  void smoke() {
    ServerProcessor serverProcessor = mock(ServerProcessor.class);
    Charset charset = mock(Charset.class);
    Jsonb jsonb = mock(Jsonb.class);
    Logger logger = mock(Logger.class);
    FrameDecoderConfig config = mock(FrameDecoderConfig.class);
    //max length > 0 check
    doAnswer(__ -> 1).when(config).getMaxFrameLength();

    TcpServerChannelHandlerAppender appender = new TcpServerChannelHandlerAppender(serverProcessor,
      charset, jsonb, config, 1, logger);

    ChannelPipeline channelPipeline = mock(ChannelPipeline.class);
    doAnswer(__ -> channelPipeline).when(channelPipeline).addLast(anyString(), any());

    appender.appendTo(channelPipeline);

    verify(channelPipeline).addLast(eq(TcpServerChannelHandlerAppender.FRAME_DECODER_NAME),
      isA(LengthFieldBasedFrameDecoder.class));
    verify(channelPipeline).addLast(eq(TcpServerChannelHandlerAppender.BYTES_DECODER_NAME),
      isA(ByteArrayDecoder.class));
    verify(channelPipeline).addLast(eq(TcpServerChannelHandlerAppender.JSON_DECODER_NAME),
      isA(JsonRpcRequestDecoder.class));
    verify(channelPipeline).addLast(eq(TcpServerChannelHandlerAppender.FRAME_ENCODER_NAME),
      isA(LengthFieldPrepender.class));
    verify(channelPipeline).addLast(eq(TcpServerChannelHandlerAppender.BYTES_ENCODER_NAME),
      isA(ByteArrayEncoder.class));
    verify(channelPipeline).addLast(eq(TcpServerChannelHandlerAppender.JSON_RPC_REQUEST_HANDLER_NAME),
      isA(RequestMessageHandler.class));

    verify(config).getLengthAdjustment();
    verify(config).getInitialBytesToStrip();
    verify(config).getLengthFieldLength();
    verify(config).getLengthFieldOffset();
    verify(config).getMaxFrameLength();
  }
}
