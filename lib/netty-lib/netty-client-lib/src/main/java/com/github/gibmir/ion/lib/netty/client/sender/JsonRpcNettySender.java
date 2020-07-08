package com.github.gibmir.ion.lib.netty.client.sender;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import com.github.gibmir.ion.api.dto.serialization.SerializationUtils;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.sender.initializer.JsonRpcNettyClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;

import javax.json.JsonObject;
import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public class JsonRpcNettySender {

  private final Class<? extends Channel> channelClass;
  private final EventLoopGroup group;

  public JsonRpcNettySender(Class<? extends Channel> channelClass, EventLoopGroup group) {
    this.channelClass = channelClass;
    this.group = group;
  }

  //todo channel pool
  public <R> CompletableFuture<R> send(JsonRpcRequest request, Jsonb jsonb, Charset charset, Class<R> returnType, SocketAddress socketAddress) {
    CompletableFuture<JsonObject> completableFuture = new CompletableFuture<>();
    Channel channel = new Bootstrap()
      .group(group)
      .channel(channelClass)
      .handler(new JsonRpcNettyClientInitializer(jsonb, charset))
      .connect(socketAddress)
      .channel();

    channel.pipeline().get(JsonRpcResponseDecoder.class).setCompletableFuture(completableFuture);
    channel.writeAndFlush(request);
    return completableFuture.thenApply(jsonObject -> {
      JsonRpcResponse jsonRpcResponse = SerializationUtils.extractResponseFrom(jsonObject, returnType, jsonb);
      NettyResponseProcessor<R> responseProcessor = new NettyResponseProcessor<>(returnType);
      jsonRpcResponse.processWith(responseProcessor);
      return responseProcessor.result;
    });
  }

  private static class NettyResponseProcessor<R> implements JsonRpcResponseProcessor {
    private final Class<R> returnType;

    private R result;

    private NettyResponseProcessor(Class<R> returnType) {
      this.returnType = returnType;
    }

    @Override
    public void process(ErrorResponse errorResponse) {
      throw new JsonRpcProcessingException(errorResponse);
    }

    @Override
    public void process(SuccessResponse successResponse) {
      result = returnType.cast(successResponse.getResult());
    }
  }
}