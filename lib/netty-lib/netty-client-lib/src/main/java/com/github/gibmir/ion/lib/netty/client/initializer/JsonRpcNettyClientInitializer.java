package com.github.gibmir.ion.lib.netty.client.initializer;

import com.github.gibmir.ion.lib.netty.client.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.codecs.encoder.JsonRpcRequestEncoder;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

import javax.json.bind.Jsonb;
import java.nio.charset.Charset;

public class JsonRpcNettyClientInitializer extends ChannelInitializer<SocketChannel> {
  private final Jsonb jsonb;
  private final Charset charset;

  public JsonRpcNettyClientInitializer(Jsonb jsonb, Charset charset) {
    this.jsonb = jsonb;
    this.charset = charset;
  }

  @Override
  protected void initChannel(SocketChannel ch) {
    ChannelPipeline pipeline = ch.pipeline();
    pipeline.addLast(new JsonRpcRequestEncoder(jsonb, charset))
      .addLast(new JsonRpcResponseDecoder(jsonb, charset));
  }
}
