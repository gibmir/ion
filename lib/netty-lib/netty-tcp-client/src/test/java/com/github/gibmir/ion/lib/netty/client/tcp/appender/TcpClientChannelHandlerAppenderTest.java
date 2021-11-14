package com.github.gibmir.ion.lib.netty.client.tcp.appender;

import com.github.gibmir.ion.lib.netty.client.common.channel.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.common.configuration.decoder.FrameDecoderConfig;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import org.junit.jupiter.api.Test;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TcpClientChannelHandlerAppenderTest {

  @Test
  void smoke() {
    ResponseListenerRegistry registry = mock(ResponseListenerRegistry.class);
    Jsonb jsonb = mock(Jsonb.class);
    Charset charset = Charset.defaultCharset();
    FrameDecoderConfig decoderConfig = mock(FrameDecoderConfig.class);
    //max length > 0 check
    doAnswer(__ -> 1).when(decoderConfig).getMaxFrameLength();
    int lengthFieldLength = 1;
    TcpClientChannelHandlerAppender tcpClientChannelHandlerAppender = new TcpClientChannelHandlerAppender(registry,
      charset, jsonb, decoderConfig, lengthFieldLength);

    ChannelPipeline channelPipeline = mock(ChannelPipeline.class);
    doAnswer(__ -> channelPipeline).when(channelPipeline).addLast(anyString(), any());
    tcpClientChannelHandlerAppender.appendTo(channelPipeline);
    verify(channelPipeline).addLast(eq(TcpClientChannelHandlerAppender.FRAME_DECODER_NAME),
      isA(LengthFieldBasedFrameDecoder.class));
    verify(channelPipeline).addLast(eq(TcpClientChannelHandlerAppender.BYTES_DECODER_NAME),
      isA(ByteArrayDecoder.class));
    verify(channelPipeline).addLast(eq(TcpClientChannelHandlerAppender.JSON_RPC_RESPONSE_DECODER_NAME),
      isA(JsonRpcResponseDecoder.class));
    verify(channelPipeline).addLast(eq(TcpClientChannelHandlerAppender.FRAME_ENCODER_NAME),
      isA(LengthFieldPrepender.class));
    verify(channelPipeline).addLast(eq(TcpClientChannelHandlerAppender.BYTES_ENCODER_NAME),
      isA(ByteArrayEncoder.class));
    verify(channelPipeline).addLast(eq(TcpClientChannelHandlerAppender.JSON_RPC_REQUEST_HANDLER_NAME),
      isA(JsonRpcResponseHandler.class));

    verify(decoderConfig).getLengthAdjustment();
    verify(decoderConfig).getInitialBytesToStrip();
    verify(decoderConfig).getLengthFieldLength();
    verify(decoderConfig).getLengthFieldOffset();
    verify(decoderConfig).getMaxFrameLength();
  }
}
