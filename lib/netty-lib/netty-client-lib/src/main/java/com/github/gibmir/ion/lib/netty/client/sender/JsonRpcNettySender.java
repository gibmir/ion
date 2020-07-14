package com.github.gibmir.ion.lib.netty.client.sender;

import com.github.gibmir.ion.api.dto.processor.JsonRpcResponseProcessor;
import com.github.gibmir.ion.api.dto.processor.exception.JsonRpcProcessingException;
import com.github.gibmir.ion.api.dto.request.JsonRpcRequest;
import com.github.gibmir.ion.api.dto.response.JsonRpcResponse;
import com.github.gibmir.ion.api.dto.response.transfer.error.ErrorResponse;
import com.github.gibmir.ion.api.dto.response.transfer.success.SuccessResponse;
import com.github.gibmir.ion.api.dto.serialization.SerializationUtils;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.decoder.JsonRpcResponseDecoder;
import com.github.gibmir.ion.lib.netty.client.sender.codecs.encoder.JsonRpcRequestEncoder;
import com.github.gibmir.ion.lib.netty.client.sender.initializer.JsonRpcNettyClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import javax.json.JsonStructure;
import javax.json.bind.Jsonb;
import java.net.SocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

public class JsonRpcNettySender {
  private final LogLevel logLevel;
  private final Class<? extends Channel> channelClass;
  private final EventLoopGroup group;

  public JsonRpcNettySender(LogLevel logLevel, Class<? extends Channel> channelClass, EventLoopGroup group) {
    this.logLevel = logLevel;
    this.channelClass = channelClass;
    this.group = group;
  }

  //todo channel pool
  public <R> CompletableFuture<R> send(JsonRpcRequest request, Jsonb jsonb, Charset charset, Class<R> returnType,
                                       SocketAddress socketAddress) {
    CompletableFuture<JsonStructure> completableFuture = new CompletableFuture<>();
    try {
      Channel channel = new Bootstrap()
        .group(group)
        .channel(channelClass)
        .option(ChannelOption.SO_KEEPALIVE, true)
        .handler(new JsonRpcNettyClientInitializer(new LoggingHandler(logLevel),
          new JsonRpcRequestEncoder(jsonb, charset), new JsonRpcResponseDecoder(jsonb, charset)))
        .connect(socketAddress).sync()
        .channel();
      channel.pipeline().get(JsonRpcResponseDecoder.class).setCompletableFuture(completableFuture);
      channel.writeAndFlush(request);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      completableFuture.completeExceptionally(e);
    }
    return completableFuture.thenApply(jsonObject -> {
      JsonRpcResponse jsonRpcResponse = SerializationUtils.extractResponseFrom(jsonObject, returnType, jsonb);
      NettyResponseProcessor<R> responseProcessor = new NettyResponseProcessor<>(returnType);
      jsonRpcResponse.processWith(responseProcessor);
      return responseProcessor.result;
    });
  }

  public void sendNotification(JsonRpcRequest request, Jsonb jsonb, Charset charset, SocketAddress socketAddress) {
    Channel channel = new Bootstrap()
      .group(group)
      .channel(channelClass)
      //todo only send. Do not await result
      .handler(new JsonRpcNettyClientInitializer(new LoggingHandler(logLevel),
        new JsonRpcRequestEncoder(jsonb, charset), new JsonRpcResponseDecoder(jsonb, charset)))
      .connect(socketAddress)
      .channel();
    channel.writeAndFlush(request);
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
