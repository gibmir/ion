package com.github.gibmir.ion.lib.netty.server.codecs.decoder;

import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.server.cache.signature.SignatureRegistry;
import com.github.gibmir.ion.api.server.serialization.SerializationUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import javax.json.JsonStructure;
import javax.json.bind.Jsonb;
import java.nio.charset.Charset;
import java.util.List;

public class JsonRpcRequestDecoder extends ReplayingDecoder<JsonRpcRequest> {
  private final Jsonb jsonb;
  private final Charset charset;
  private final SignatureRegistry signatureRegistry;

  public JsonRpcRequestDecoder(Jsonb jsonb, Charset charset, SignatureRegistry signatureRegistry) {
    this.jsonb = jsonb;
    this.charset = charset;
    this.signatureRegistry = signatureRegistry;
  }

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
    byte[] bytes = new byte[in.writerIndex()];
    in.readBytes(bytes);
    JsonStructure jsonObject = jsonb.fromJson(new String(bytes, charset), JsonStructure.class);
    JsonRpcRequest jsonRpcRequest = SerializationUtils.extractRequestFrom(jsonObject, signatureRegistry, jsonb);
    out.add(jsonRpcRequest);
  }
}
