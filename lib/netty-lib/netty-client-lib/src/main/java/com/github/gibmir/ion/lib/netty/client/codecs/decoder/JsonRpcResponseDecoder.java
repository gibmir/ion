package com.github.gibmir.ion.lib.netty.client.codecs.decoder;

import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class JsonRpcResponseDecoder extends ReplayingDecoder<JsonRpcResponse> {
  private final Jsonb jsonb;
  private final Charset charset;
  private CompletableFuture<JsonObject> completableFuture;

  public JsonRpcResponseDecoder(Jsonb jsonb, Charset charset) {
    this.jsonb = jsonb;
    this.charset = charset;
  }

  public void setCompletableFuture(CompletableFuture<JsonObject> jsonObjectFuture) {
    this.completableFuture = jsonObjectFuture;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
    JsonObject jsonObject = jsonb.fromJson(in.toString(charset), JsonObject.class);
    completableFuture.complete(jsonObject);
    out.add(jsonObject);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    super.exceptionCaught(ctx, cause);
    completableFuture.completeExceptionally(cause);
  }
}
