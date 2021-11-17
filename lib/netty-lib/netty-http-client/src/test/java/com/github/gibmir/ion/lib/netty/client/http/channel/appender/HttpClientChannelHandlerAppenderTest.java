package com.github.gibmir.ion.lib.netty.client.http.channel.appender;

import com.github.gibmir.ion.lib.netty.client.common.channel.handler.JsonRpcResponseHandler;
import com.github.gibmir.ion.lib.netty.client.common.channel.handler.response.registry.ResponseListenerRegistry;
import com.github.gibmir.ion.lib.netty.client.http.channel.codecs.HttpJsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.http.configuration.HttpResponseDecoderConfiguration;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

import static com.github.gibmir.ion.lib.netty.client.http.channel.appender.HttpClientChannelHandlerAppender.HTTP_CLIENT_CODEC_NAME;
import static com.github.gibmir.ion.lib.netty.client.http.channel.appender.HttpClientChannelHandlerAppender.HTTP_OBJECT_AGGREGATOR_NAME;
import static com.github.gibmir.ion.lib.netty.client.http.channel.appender.HttpClientChannelHandlerAppender.HTTP_RESPONSE_DECODER_NAME;
import static com.github.gibmir.ion.lib.netty.client.http.channel.appender.HttpClientChannelHandlerAppender.RESPONSE_HANDLER_NAME;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class HttpClientChannelHandlerAppenderTest {

  @Test
  void smoke() {
    ResponseListenerRegistry registry = mock(ResponseListenerRegistry.class);
    Charset charset = mock(Charset.class);
    Jsonb jsonb = mock(Jsonb.class);
    HttpResponseDecoderConfiguration configuration = new HttpResponseDecoderConfiguration(1, 1, 1);
    int maxContentLength = 1;
    HttpClientChannelHandlerAppender appender = new HttpClientChannelHandlerAppender(registry, charset, jsonb,
      configuration, maxContentLength, mock(Logger.class));
    ChannelPipeline pipeline = mock(ChannelPipeline.class);
    doAnswer(__ -> pipeline).when(pipeline).addLast(anyString(), any());
    appender.appendTo(pipeline);

    verify(pipeline).addLast(eq(HTTP_CLIENT_CODEC_NAME), isA(HttpClientCodec.class));
    verify(pipeline).addLast(eq(HTTP_OBJECT_AGGREGATOR_NAME), isA(HttpObjectAggregator.class));
    verify(pipeline).addLast(eq(HTTP_RESPONSE_DECODER_NAME), isA(HttpJsonRpcResponseDecoder.class));
    verify(pipeline).addLast(eq(RESPONSE_HANDLER_NAME), isA(JsonRpcResponseHandler.class));
  }
}
