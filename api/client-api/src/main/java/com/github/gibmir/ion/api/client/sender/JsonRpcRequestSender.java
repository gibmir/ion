package com.github.gibmir.ion.api.client.sender;

import com.github.gibmir.ion.api.client.callback.JsonRpcResponseCallback;
import com.github.gibmir.ion.api.client.context.RequestContext;

public interface JsonRpcRequestSender {
  void sendAsync(RequestContext requestContext, JsonRpcResponseCallback callback);
}
