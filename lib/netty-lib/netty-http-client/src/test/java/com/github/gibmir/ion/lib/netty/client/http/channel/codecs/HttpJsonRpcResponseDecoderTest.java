package com.github.gibmir.ion.lib.netty.client.http.channel.codecs;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.json.JsonValue;
import javax.json.bind.Jsonb;

import java.nio.charset.Charset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class HttpJsonRpcResponseDecoderTest {

  @Test
  void testDecodeWithoutBody() {
    Jsonb jsonb = mock(Jsonb.class);
    Charset charset = mock(Charset.class);
    Logger logger = mock(Logger.class);
    HttpJsonRpcResponseDecoder httpJsonRpcResponseDecoder = new HttpJsonRpcResponseDecoder(jsonb, charset, logger);
    EmbeddedChannel channel = new EmbeddedChannel(httpJsonRpcResponseDecoder);
    DefaultFullHttpResponse defaultHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    channel.writeInbound(defaultHttpResponse);

    verify(jsonb, never()).fromJson(anyString(), any());
    verify(logger).debug(anyString());
  }

  @Test
  void testDecoderWithBody() {
    Jsonb jsonb = mock(Jsonb.class);
    Charset charset = Charset.defaultCharset();
    Logger logger = mock(Logger.class);
    HttpJsonRpcResponseDecoder httpJsonRpcResponseDecoder = new HttpJsonRpcResponseDecoder(jsonb, charset, logger);
    EmbeddedChannel channel = new EmbeddedChannel(httpJsonRpcResponseDecoder);
    FullHttpResponse defaultHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    byte[] bytes = "{}".getBytes();
    doAnswer(__ -> mock(JsonValue.class)).when(jsonb).fromJson(anyString(), any());
    defaultHttpResponse.content().clear().writeBytes(bytes);

    channel.writeInbound(defaultHttpResponse);

    verify(jsonb).fromJson(eq(new String(bytes)), eq(JsonValue.class));
    verify(logger).debug(anyString(), eq(bytes.length));
  }
}
