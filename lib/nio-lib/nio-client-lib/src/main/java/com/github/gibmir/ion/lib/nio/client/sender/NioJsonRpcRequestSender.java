package com.github.gibmir.ion.lib.nio.client.sender;

import com.github.gibmir.ion.api.client.callback.JsonRpcResponseCallback;
import com.github.gibmir.ion.api.client.context.RequestContext;
import com.github.gibmir.ion.api.client.sender.JsonRpcRequestSender;
import com.github.gibmir.ion.lib.nio.client.sender.channel.resolver.ChannelResolver;
import com.github.gibmir.ion.lib.nio.client.sender.selector.IonClientSelector;

import java.nio.channels.SocketChannel;

public class NioJsonRpcRequestSender implements JsonRpcRequestSender {
  private final IonClientSelector ionClientSelector;
  private final ChannelResolver channelResolver;

  public NioJsonRpcRequestSender(IonClientSelector ionClientSelector, ChannelResolver channelResolver) {
    this.ionClientSelector = ionClientSelector;
    this.channelResolver = channelResolver;
  }

  @Override
  public void sendAsync(RequestContext requestContext, JsonRpcResponseCallback callback) {
    try {
      SocketChannel channel = channelResolver.resolveFor(requestContext);
      ionClientSelector.sendRequest(requestContext, callback, channel);
    } catch (Throwable throwable) {
      callback.onComplete(null, throwable);
    }
  }
}
